package exercise1.model.Notifications;

import exercise1.model.StateSpace;

public class Addition extends Notification {
    public StateSpace state;

    public Addition(int x, int y, StateSpace state) {
        super(x, y);
        this.state = state;
    }
}
