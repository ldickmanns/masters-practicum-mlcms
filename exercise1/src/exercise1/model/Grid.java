package exercise1.model;

import exercise1.model.CellStateObjects.Obstacle;
import exercise1.model.CellStateObjects.Pedestrian;
import exercise1.model.CellStateObjects.StateSpace;
import exercise1.model.CellStateObjects.Target;
import exercise1.model.Notifications.Addition;
import exercise1.model.Notifications.Deletion;
import exercise1.model.Notifications.PedestrianMovement;
import exercise1.model.Notifications.Reset;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

public class Grid extends Observable {

    /** {@link StateSpace} matrix representing the state state. */
    private StateSpace[][] state;

    private List<Pedestrian> pedestrians = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Target> targets = new ArrayList<>();

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
        this.pedestrians.add(new Pedestrian(x, y));
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
        this.obstacles.add(new Obstacle(x, y));
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
        this.targets.add(new Target(x, y));
    }

    public void movePedestrian(int x, int y, Direction d) {
        if (!inGrid(x, y)) return;
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
            StateSpace newPosition = state[newX][newY];
            if (newPosition == StateSpace.T || newPosition == StateSpace.O || newPosition == StateSpace.P) return;
            if (newX >= state.length || newY >= state[0].length) return;
            this.state[newX][newY] = StateSpace.P;
            this.state[x][y] = StateSpace.E;
            for (Pedestrian pedestrian : this.pedestrians) {
                if (x == pedestrian.x && y == pedestrian.y) {
                    pedestrian.x = newX;
                    pedestrian.y = newY;
                    setChanged();
                    notifyObservers(new PedestrianMovement(x, y, newX, newY));
                    return;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public void emptyCell(int x, int y) {
        if (!inGrid(x, y)) return;
        switch (this.state[x][y]) {
            case E:
                return;
            case P:
                pedestrians = pedestrians.stream().filter(p -> p.x != x || p.y != y).collect(Collectors.toList());
                break;
            case O:
                obstacles = obstacles.stream().filter(o -> o.x != x || o.y != y).collect(Collectors.toList());
                break;
            case T:
                targets = targets.stream().filter(t -> t.x != x || t.y != y).collect(Collectors.toList());
                break;
        }
        this.state[x][y] = StateSpace.E;
        setChanged();
        notifyObservers(new Deletion(x, y));
    }

    /** Internal helper to shorten the code. */
    private void addObject(int x, int y, StateSpace state) {
        if (!inGrid(x, y)) return;
        if (this.state[x][y] != StateSpace.E) return;

        this.state[x][y] = state;
        setChanged();
        notifyObservers(new Addition(x, y, state));
    }

    /** Returns the state. */
    public StateSpace[][] getState() {
        return state;
    }

    public void reset(Grid other) {
        this.state = other.getState();
        this.pedestrians = other.getPedestrians();
        this.obstacles = other.getObstacles();
        this.targets = other.getTargets();

        setChanged();
        notifyObservers(new Reset(state));
    }

    public int getColumnCount() {
        if (this.state == null) return 0;
        return this.state.length;
    }

    public int getRowCount() {
        if (this.state == null || this.state[0] == null) return 0;
        return this.state[0].length;
    }

    public boolean inGrid(int x, int y) {
        if (state == null || state[0] == null) return false;
        if (x >= 0 && x < state.length && y >= 0 && y < state[0].length) return true;
        return false;
    }

    public List<Pedestrian> getPedestrians() {
        return pedestrians;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Target> getTargets() {
        return targets;
    }
}
