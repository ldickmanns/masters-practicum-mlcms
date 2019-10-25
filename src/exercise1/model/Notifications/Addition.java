package exercise1.model.Notifications;

import exercise1.model.CellStateObjects.StateSpace;

public class Addition extends CoordinateNotification {
    public StateSpace state;

    public Addition(int x, int y, StateSpace state) {
        super(x, y);
        this.state = state;
    }
}
