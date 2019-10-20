package exercise1.visualization;

import exercise1.model.Grid;
import exercise1.model.Direction;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFXVisualization extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Grid initialState = new Grid(50, 50);
        initialState.addPedestrian(5, 25);
        initialState.addTarget(25, 25);
        StateCanvas stateCanvas = new StateCanvas(initialState.getState());
        initialState.addObserver(stateCanvas);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Moving!");
                initialState.movePedestrian(5, 25, Direction.RIGHT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        VBox buttons = new VBox();
        HBox root = new HBox(stateCanvas.getCanvas());
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
