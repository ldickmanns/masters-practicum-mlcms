package exercise1.visualization;

import exercise1.model.Notifications.Addition;
import exercise1.model.Notifications.Deletion;
import exercise1.model.Notifications.Notification;
import exercise1.model.Notifications.PedestrianMovement;
import exercise1.model.CellStateObjects.StateSpace;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.Observer;

public class StateCanvas implements Observer {

    private static final int LINE_WIDTH = 2;
    private static final int CELL_SIZE = 20;
    private static final int PADDING = 20;
    private static final Color E_COLOR = Color.WHITE;
    private static final Color P_COLOR = Color.RED;
    private static final Color O_COLOR = Color.BLUE;
    private static final Color T_COLOR = Color.YELLOW;

    private Canvas canvas;
    private GraphicsContext gc;

    public StateCanvas(StateSpace[][] state) {
        int width = PADDING + state.length * CELL_SIZE + PADDING;
        int height = PADDING + state[0].length * CELL_SIZE + PADDING;
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        drawState(state);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Notification) {
            if (arg instanceof PedestrianMovement) {
                PedestrianMovement m = (PedestrianMovement) arg;
                colorCell(m.x, m.y, E_COLOR);
                colorCell(m.newX, m.newY, P_COLOR);
                return;
            }
            if (arg instanceof Deletion) {
                Deletion d = (Deletion)arg;
                colorCell(d.x, d.y, E_COLOR);
                return;
            }
            if (arg instanceof Addition) {
                Addition a = (Addition)arg;
                Color c = colorOfState(a.state);
                colorCell(a.x, a.y, c);
                return;
            }
        }
    }

    private void drawState(StateSpace[][] state) {
        for (int x = 0; x < state.length; x++) {
            for (int y = 0; y < state[0].length; y++) {
                drawCell(x, y);
                Color color = colorOfState(state[x][y]);
                colorCell(x, y, color);
            }
        }
    }

    private void drawCell(int xGrid, int yGrid) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(LINE_WIDTH);
        int x = PADDING + xGrid * CELL_SIZE;
        int y = PADDING + yGrid * CELL_SIZE;
        gc.strokeRect(x, y, CELL_SIZE, CELL_SIZE);
    }

    private void colorCell(int xGrid, int yGrid, Color color) {
        gc.setFill(color);
        int x = PADDING + LINE_WIDTH / 2 + xGrid * CELL_SIZE;
        int y = PADDING + LINE_WIDTH / 2 + yGrid * CELL_SIZE;
        int w = CELL_SIZE - LINE_WIDTH;
        int h = CELL_SIZE - LINE_WIDTH;
        gc.fillRect(x, y, w, h);
    }

    private Color colorOfState(StateSpace state) {
        Color color;
        switch (state) {
            case E:
                color = E_COLOR;
                break;
            case P:
                color = P_COLOR;
                break;
            case O:
                color = O_COLOR;
                break;
            case T:
                color = T_COLOR;
                break;
            default:
                // Indicates an error.
                color = Color.PURPLE;
                break;
        }
        return color;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
