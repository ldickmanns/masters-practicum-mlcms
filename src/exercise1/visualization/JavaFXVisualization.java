package exercise1.visualization;

import exercise1.model.Grid;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class JavaFXVisualization extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Grid grid = new Grid(50, 50);
        StateCanvas stateCanvas = new StateCanvas(grid.getState());
        grid.addObserver(stateCanvas);
        
        grid.addPedestrianWithSpeed(5, 5, 200);
        grid.addPedestrianWithSpeed(7, 5, 200);
        grid.addPedestrianWithSpeed(9, 5, 200);
        grid.addPedestrianWithSpeed(11, 5, 200);
        grid.addPedestrianWithSpeed(5, 7, 200);
        grid.addPedestrianWithSpeed(7, 7, 200);
        grid.addPedestrianWithSpeed(9, 7, 200);
        grid.addPedestrianWithSpeed(11, 7, 200);
        grid.addPedestrianWithSpeed(5, 9, 200);
        grid.addPedestrianWithSpeed(7, 9, 200);
        grid.addPedestrianWithSpeed(9, 9, 200);
        grid.addPedestrianWithSpeed(11, 9, 200);
        grid.addPedestrianWithSpeed(5, 11, 200);
        grid.addPedestrianWithSpeed(7, 11, 200);
        grid.addPedestrianWithSpeed(9, 11, 200);
        grid.addPedestrianWithSpeed(11, 11, 200);
        grid.addPedestrianWithSpeed(38, 5, 400);
        grid.addPedestrianWithSpeed(40, 5, 400);
        grid.addPedestrianWithSpeed(42, 5, 400);
        grid.addPedestrianWithSpeed(44, 5, 400);
        grid.addPedestrianWithSpeed(38, 7, 400);
        grid.addPedestrianWithSpeed(40, 7, 400);
        grid.addPedestrianWithSpeed(42, 7, 400);
        grid.addPedestrianWithSpeed(44, 7, 400);
        grid.addPedestrianWithSpeed(38, 9, 400);
        grid.addPedestrianWithSpeed(40, 9, 400);
        grid.addPedestrianWithSpeed(42, 9, 400);
        grid.addPedestrianWithSpeed(44, 9, 400);
        grid.addPedestrianWithSpeed(38, 11, 400);
        grid.addPedestrianWithSpeed(40, 11, 400);
        grid.addPedestrianWithSpeed(42, 11, 400);
        grid.addPedestrianWithSpeed(44, 11, 400);
        grid.addPedestrianWithSpeed(5, 38, 600);
        grid.addPedestrianWithSpeed(7, 38, 600);
        grid.addPedestrianWithSpeed(9, 38, 600);
        grid.addPedestrianWithSpeed(11, 38, 600);
        grid.addPedestrianWithSpeed(5, 40, 600);
        grid.addPedestrianWithSpeed(7, 40, 600);
        grid.addPedestrianWithSpeed(9, 40, 600);
        grid.addPedestrianWithSpeed(11, 40, 600);
        grid.addPedestrianWithSpeed(5, 42, 600);
        grid.addPedestrianWithSpeed(7, 42, 600);
        grid.addPedestrianWithSpeed(9, 42, 600);
        grid.addPedestrianWithSpeed(11, 42, 600);
        grid.addPedestrianWithSpeed(5, 44, 600);
        grid.addPedestrianWithSpeed(7, 44, 600);
        grid.addPedestrianWithSpeed(9, 44, 600);
        grid.addPedestrianWithSpeed(11, 44, 600);
        grid.addPedestrianWithSpeed(38, 38, 600);
        grid.addPedestrianWithSpeed(40, 38, 600);
        grid.addPedestrianWithSpeed(42, 38, 600);
        grid.addPedestrianWithSpeed(44, 38, 600);
        grid.addPedestrianWithSpeed(38, 40, 800);
        grid.addPedestrianWithSpeed(40, 40, 800);
        grid.addPedestrianWithSpeed(42, 40, 800);
        grid.addPedestrianWithSpeed(44, 40, 800);
        grid.addPedestrianWithSpeed(38, 42, 800);
        grid.addPedestrianWithSpeed(40, 42, 800);
        grid.addPedestrianWithSpeed(42, 42, 800);
        grid.addPedestrianWithSpeed(44, 42, 800);
        grid.addPedestrianWithSpeed(38, 44, 800);
        grid.addPedestrianWithSpeed(40, 44, 800);
        grid.addPedestrianWithSpeed(42, 44, 800);
        grid.addPedestrianWithSpeed(44, 44, 800);

        GridConfiguration gc = new GridConfiguration(grid);

        HBox root = new HBox(20, stateCanvas.getCanvas(), gc.getContent());
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
