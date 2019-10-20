package exercise1.model;

import exercise1.model.Notifications.Addition;
import exercise1.model.Notifications.PedestrianMovement;

import java.util.Observable;

public class Grid extends Observable {

    /** {@link StateSpace} matrix representing the state state. */
    private StateSpace[][] state = null;

    /** Creates a {@link Grid} in with x columns and y rows and initializes each cell as empty cell. */
    public Grid(int x, int y) {
        this.state = new StateSpace[x][y];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                state[i][j] = StateSpace.E;
            }
        }
    }

    /** Creates a {@link Grid} from a {@link StateSpace} matrix. */
    public Grid(StateSpace[][] state) {
        this.state = state.clone();
    }

    /**
     * Add a pedestrian into the cell at the specified coordinates.
     * The pedestrian is only added to the cell if the coordinates are inside the state and the cell is empty.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void addPedestrian(int x, int y) {
        addObject(x, y, StateSpace.P);
    }

    /**
     * Add a obstacle into the cell at the specified coordinates.
     * The obstacle is only added to the cell if the coordinates are inside the state and the cell is empty.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void addObstacle(int x, int y) {
        addObject(x, y, StateSpace.O);
    }

    /**
     * Add a target into the cell at the specified coordinates.
     * The target is only added to the cell if the coordinates are inside the state and the cell is empty.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void addTarget(int x, int y) {
        addObject(x, y, StateSpace.T);
    }

    public void movePedestrian(int x, int y, Direction d) {
        if (this.state[x][y] != StateSpace.P) return;
        try {
            int newX = x;
            int newY = y;
            switch (d) {
                case UP:
                    --newY;
                    break;
                case RIGHT:
                    ++newX;
                    break;
                case DOWN:
                    ++newY;
                    break;
                case LEFT:
                    --newX;
                    break;
            }
            this.state[newX][newY] = StateSpace.P;
            this.state[x][y] = StateSpace.E;
            setChanged();
            notifyObservers(new PedestrianMovement(x, y, newX, newY));
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
    }

    /** Internal helper to shorten the code. */
    private void addObject(int x, int y, StateSpace state) {
        if (x >= this.state.length || y >= this.state[0].length || this.state[x][y] != StateSpace.E){
            return;
        }
        this.state[x][y] = state;
        setChanged();
        notifyObservers(new Addition(x, y, state));
    }

    /** Returns the state. */
    public StateSpace[][] getState() {
        return state;
    }
}
