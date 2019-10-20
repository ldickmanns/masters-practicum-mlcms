package exercise1.model.Notifications;

public class PedestrianMovement extends Notification {

    public int newX;
    public int newY;

    public PedestrianMovement(int x, int y, int newX, int newY) {
        super(x, y);
        this.newX = newX;
        this.newY = newY;
    }
}
