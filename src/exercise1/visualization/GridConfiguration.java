package exercise1.visualization;

import exercise1.Utils;
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
                    for (int i = 0; i < amount; i++) {
                        cc.nextStep();
                        Thread.sleep(500);
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }).start();
        });
        vBox.getChildren().add(button);

        content.getChildren().add(vBox);
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
