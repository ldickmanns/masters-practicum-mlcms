package exercise1.visualization;

import exercise1.Utils;
import exercise1.model.CellStateObjects.Pedestrian;
import exercise1.model.CellStateObjects.StateSpace;
import exercise1.model.Grid;
import exercise1.simulation.CostCalculation;
import exercise1.simulation.DijkstraDistanceCalculationStrategy;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.BiConsumer;

import static exercise1.Utils.isTextFieldTextInt;
import static exercise1.Utils.predicateTextField;
import static exercise1.model.PropertiesUtils.*;
import static java.lang.Integer.parseInt;

public class GridConfiguration {

    // Total width.
    private static final int WIDTH = 230;
    // Quarter width (WIDTH - 3 * SPACING).
    private static final int Q_WIDTH = 50;
    // Spacing.
    private static final int SPACING = 10;

    private Grid grid;
    private VBox content = new VBox(SPACING * 2);

    public GridConfiguration(Grid grid) {
        this.grid = grid;
        init();
    }

    private void init() {
        initSimulationConfiguration();

        content.getChildren().add(new Line(0, 0, WIDTH, 0));
        content.getChildren().add(new Line(0, 0, WIDTH, 0));

        initAdding();

        content.getChildren().add(new Line(0, 0, WIDTH, 0));
        content.getChildren().add(new Line(0, 0, WIDTH, 0));

        initPersistency();
    }

    private void initSimulationConfiguration() {
        VBox vBox = new VBox(SPACING);
        Label simulationLabel = new Label("Simulation");

        vBox.getChildren().add(simulationLabel);

        HBox hBox = new HBox(SPACING);
        hBox.setPrefWidth(WIDTH);
        hBox.setAlignment(Pos.CENTER_LEFT);
        Label stepsLabel = new Label("Steps:");
        stepsLabel.setPrefWidth(Q_WIDTH);
        TextField stepsTF = predicateTextField(Utils::isTextFieldTextInt);
        stepsTF.setPrefWidth(WIDTH - SPACING - Q_WIDTH);
        hBox.getChildren().addAll(stepsLabel, stepsTF);
        vBox.getChildren().add(hBox);

        Button button = new Button("Simulate");
        button.setPrefWidth(WIDTH);
        button.setOnAction(e -> {
            if (!isTextFieldTextInt(stepsTF)) return;
            int amount = parseInt(stepsTF.getText());

            new Thread(() -> {
                try {
                    CostCalculation cc = new CostCalculation(grid, new DijkstraDistanceCalculationStrategy());
                    Thread.sleep(500);

                    // 180 steps * 0.33 sec/step = 60 sec (measuring time)
                    int sAmount = 180;
                    double sSpeedSum450 = 0.0;
                    double sSpeedSum500 = 0.0;

                    for (int i = 0; i < amount; i++) {
                        System.out.println("Step: " + i);
                        System.out.println("Last Pedestrian x: " + grid.getPedestrians().get(grid.getPedestrians().size() - 1).x);
                        // Store positions before step.
                        List<Pedestrian> pedestriansBefore450 = pedestriansAt450mMeasuringPoint();
                        List<Pedestrian> pedestriansBefore500 = pedestriansAt500mMeasuringPoints();

                        // Perform next step.
                        cc.nextStep();

                        // Store positions after step.
                        List<Pedestrian> pedestriansAfter450 = pedestriansAt450mMeasuringPoint();
                        List<Pedestrian> pedestriansAfter500 = pedestriansAt500mMeasuringPoints();


                        // 30 steps * 0.33 sec/step = 10 sec (transient response)
                        if (i >= 30) {
                            int pAmount450 = 0;
                            int pAmount500 = 0;
                            double pSpeedSum450 = 0.0;
                            double pSpeedSum500 = 0.0;

                            for (Pedestrian p : pedestriansAfter450) {
                                if (pedestriansBefore450.contains(p)) {
                                    pAmount450++;
                                    Pedestrian pBefore450 = pedestriansBefore450.get(pedestriansBefore450.indexOf(p));
                                    Pedestrian pAfter450 = pedestriansAfter450.get(pedestriansAfter450.indexOf(p));
                                    // One step is performed in 0.33 seconds
                                    // 0.33 sec * 0.4m = 1.2 m/s
                                    if (pBefore450.x != pAfter450.x || pBefore450.y != pAfter450.y) pSpeedSum450 += 1.2;
                                }
                            }

                            for (Pedestrian p : pedestriansAfter500) {
                                if (pedestriansBefore500.contains(p)) {
                                    pAmount500++;
                                    Pedestrian pBefore500 = pedestriansBefore500.get(pedestriansBefore500.indexOf(p));
                                    Pedestrian pAfter500 = pedestriansAfter500.get(pedestriansAfter500.indexOf(p));
                                    // One step is performed in 0.33 seconds
                                    // 0.33 sec * 0.4m = 1.2 m/s
                                    if (pBefore500.x != pAfter500.x || pBefore500.y != pAfter500.y) pSpeedSum500 += 1.2;
                                }
                            }


                            if (i >= 30 + sAmount) {
                                System.out.println("Average Speed 450: " + (sSpeedSum450 / sAmount));
                                System.out.println("Average Speed 450: " + (sSpeedSum500 / sAmount));
                            } else {
                                if (pAmount450 > 0) sSpeedSum450 += (pSpeedSum450 / pAmount450);
                                if (pAmount500 > 0) sSpeedSum500 += (pSpeedSum500 / pAmount500);
                                System.out.println("Current Speed 450: " + (pSpeedSum450 / pAmount450));
                                System.out.println("Current Speed 500: " + (pSpeedSum500 / pAmount500));
                            }
                        }
                        System.out.println();
                        System.out.println();
                        //Thread.sleep(333);
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }).start();
        });

        vBox.getChildren().add(button);

        content.getChildren().add(vBox);
    }

    private List<Pedestrian> pedestriansAt500mMeasuringPoints() {
        List<Pedestrian> pedestriansInArea = new ArrayList<>();

        StateSpace[][] state = grid.getState();

        for (int x = 1249; x < 1254; x++) {
            // Above measuring points.
            for (int y = 6; y < 16; y++) {
                if (state[x][y] == StateSpace.P) {
                    for (Pedestrian p : grid.getPedestrians()) {
                        if (p.x == x && p.y == y) {
                            pedestriansInArea.add(p.copy());
                        }
                    }
                }
            }
            // Below measuring points.
            for (int y = 26; y < 31; y++) {
                if (state[x][y] == StateSpace.P) {
                    for (Pedestrian p : grid.getPedestrians()) {
                        if (p.x == x && p.y == y) {
                            pedestriansInArea.add(p.copy());
                        }
                    }
                }
            }
        }

        return pedestriansInArea;
    }

    private List<Pedestrian> pedestriansAt450mMeasuringPoint() {
        List<Pedestrian> pedestriansInArea = new ArrayList<>();

        StateSpace[][] state = grid.getState();

        for (int x = 1124; x < 1129; x++) {
            // Above measuring points.
            for (int y = 6; y < 16; y++) {
                if (state[x][y] == StateSpace.P) {
                    for (Pedestrian p : grid.getPedestrians()) {
                        if (p.x == x && p.y == y) {
                            pedestriansInArea.add(p.copy());
                        }
                    }
                }
            }
            // Below measuring points.
            for (int y = 21; y < 31; y++) {
                if (state[x][y] == StateSpace.P) {
                    for (Pedestrian p : grid.getPedestrians()) {
                        if (p.x == x && p.y == y) {
                            pedestriansInArea.add(p.copy());
                        }
                    }
                }
            }
        }

        return pedestriansInArea;
    }

    private void initAdding() {
        content.getChildren().add(addingGridPane(StateSpace.P));
        content.getChildren().add(new Line(0, 0, WIDTH, 0));
        content.getChildren().add(addingGridPane(StateSpace.O));
        content.getChildren().add(new Line(0, 0, WIDTH, 0));
        content.getChildren().add(addingGridPane(StateSpace.T));
        content.getChildren().add(new Line(0, 0, WIDTH, 0));
        content.getChildren().add(addingGridPane(StateSpace.E));
    }
    
    private void initPersistency() {
        GridPane gridPane = new GridPane();
        ColumnConstraints cc = new ColumnConstraints(Q_WIDTH * 2 + SPACING);
        gridPane.getColumnConstraints().addAll(cc, cc);
        gridPane.setHgap(SPACING);
        gridPane.setVgap(SPACING);
        gridPane.setAlignment(Pos.CENTER);

        Button save = new Button("Save");
        save.setPrefWidth(Q_WIDTH * 2 + SPACING);
        save.setOnAction(e -> {
            Properties properties = toProperties(grid.getState());
            saveProperties(properties);
        });
        gridPane.add(save, 0, 0);

        Button load = new Button("Load");
        load.setPrefWidth(Q_WIDTH * 2 + SPACING);
        load.setOnAction(e -> {
            Properties properties = loadProperties();
            if (properties == null) return;
            Grid loadedGrid = fromProperties(properties);
            if (loadedGrid == null) return;
            grid.reset(loadedGrid);
        });
        gridPane.add(load, 1, 0);
        content.getChildren().add(gridPane);
    }

    private GridPane addingGridPane(StateSpace type) {
        // Name and creation function.
        String name;
        BiConsumer<Integer, Integer> creation;
        switch (type) {
            case P:
                name = "Pedestrian";
                creation = (x, y) -> grid.addPedestrian(x, y);
                break;
            case O:
                name = "Obstacle";
                creation = (x, y) -> grid.addObstacle(x, y);
                break;
            case T:
                name = "Target";
                creation = (x, y) -> grid.addTarget(x, y);
                break;
            case E:
                name = "Deletion";
                creation = (x, y) -> grid.emptyCell(x, y);
                break;
            default:
                return null;
        }

        // Grid pane for visualization.
        GridPane gridPane = new GridPane();
        ColumnConstraints cc = new ColumnConstraints(Q_WIDTH);
        gridPane.getColumnConstraints().addAll(cc, cc, cc, cc);
        gridPane.setHgap(SPACING);
        gridPane.setVgap(SPACING);
        gridPane.setAlignment(Pos.CENTER_LEFT);

        // Name.
        Label label = new Label(name);
        gridPane.add(label, 0, 0, 4, 1);

        // x- and y-coordinate configuration
        Label xLabel = new Label("x:");
        xLabel.setPrefWidth(Q_WIDTH);
        TextField xTF = predicateTextField(Utils::isTextFieldTextInt);
        xTF.setPrefWidth(Q_WIDTH);
        Label yLabel = new Label("y:");
        yLabel.setPrefWidth(Q_WIDTH);
        TextField yTF = predicateTextField(Utils::isTextFieldTextInt);
        xTF.setPrefWidth(Q_WIDTH);
        gridPane.add(xLabel, 0, 1);
        gridPane.add(xTF, 1, 1);
        gridPane.add(yLabel, 2, 1);
        gridPane.add(yTF, 3, 1);

        // x- and y-span configuration
        Label xSpanLabel = new Label("x span:");
        xSpanLabel.setPrefWidth(Q_WIDTH);
        TextField xSpanTF = predicateTextField(Utils::isTextFieldTextInt);
        xSpanTF.setPrefWidth(Q_WIDTH);
        Label ySpanLabel = new Label("y span:");
        ySpanLabel.setPrefWidth(Q_WIDTH);
        TextField ySpanTF = predicateTextField(Utils::isTextFieldTextInt);
        ySpanTF.setPrefWidth(Q_WIDTH);
        gridPane.add(xSpanLabel, 0, 2);
        gridPane.add(xSpanTF, 1, 2);
        gridPane.add(ySpanLabel, 2, 2);
        gridPane.add(ySpanTF, 3, 2);

        // Button.
        String prefix = type != StateSpace.E ? "Add " : "";
        Button button = new Button(prefix + name);
        button.setPrefWidth(WIDTH);
        button.setOnAction(e -> {
            if (!isTextFieldTextInt(xTF) || !isTextFieldTextInt(yTF)) return;
            int x = parseInt(xTF.getText());
            int y = parseInt(yTF.getText());
            int xSpan = 1;
            int ySpan = 1;
            if (!xSpanTF.getText().isEmpty() && isTextFieldTextInt(xSpanTF))
                xSpan = parseInt(xSpanTF.getText());
            if (!ySpanTF.getText().isEmpty() && isTextFieldTextInt(ySpanTF))
                ySpan = parseInt(ySpanTF.getText());
            for (int i = x; i < x + xSpan; ++i) {
                for (int j = y; j < y + ySpan; ++j) {
                    creation.accept(i, j);
                }
            }
        });
        gridPane.add(button, 0, 3, 4, 1);

        return gridPane;
    }

    public VBox getContent() {
        return content;
    }
}
