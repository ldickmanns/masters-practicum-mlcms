package exercise1.visualization;

import exercise1.model.Grid;
import exercise1.model.StateSpace;
import exercise1.model.System;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JavaFXVisualization extends Application {

    private static final int LINE_WIDTH = 2;
    private static final int CELL_SIZE = 20;
    private static final int PADDING = 20;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        System system = new System(null);
        Grid grid = system.getInitialState();
        StateSpace[][] state = grid.getState();

        int width = PADDING + state.length * CELL_SIZE + PADDING;
        int height = PADDING + state[0].length * CELL_SIZE + PADDING;
        final Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawState(state, gc);

        Group root = new Group(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void drawState(StateSpace[][] state, GraphicsContext gc) {
        for (int x = 0; x < state.length; x++) {
            for (int y = 0; y < state[0].length; y++) {
                drawCell(x, y, gc);
                switch (state[x][y]) {
                    case P:
                        colorCell(x, y, Color.RED, gc);
                        break;
                    case O:
                        colorCell(x, y, Color.BLUE, gc);
                        break;
                    case T:
                        colorCell(x, y, Color.YELLOW, gc);
                        break;
                    case E:
                        colorCell(x, y, Color.WHITE, gc);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static void drawCell(int xGrid, int yGrid, GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(LINE_WIDTH);
        int x = PADDING + xGrid * CELL_SIZE;
        int y = PADDING + yGrid * CELL_SIZE;
        gc.strokeRect(x, y, CELL_SIZE, CELL_SIZE);
    }

    private static void colorCell(int xGrid, int yGrid, Color color, GraphicsContext gc) {
        gc.setFill(color);
        int x = PADDING + LINE_WIDTH / 2 + xGrid * CELL_SIZE;
        int y = PADDING + LINE_WIDTH / 2 + yGrid * CELL_SIZE;
        int w = CELL_SIZE - LINE_WIDTH;
        int h = CELL_SIZE - LINE_WIDTH;
        gc.fillRect(x, y, w, h);
    }
}
