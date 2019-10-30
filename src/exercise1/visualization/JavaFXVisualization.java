package exercise1.visualization;

import exercise1.model.Grid;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
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

        GridConfiguration gridConfiguration = new GridConfiguration(grid);

        //ScrollPane scrollPane = new ScrollPane(stateCanvas.getCanvas());
//        scrollPane.setPrefViewportWidth(1000);

        //HBox root = new HBox(20, scrollPane, gridConfiguration.getContent());
        HBox root = new HBox(20, gridConfiguration.getContent());
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
