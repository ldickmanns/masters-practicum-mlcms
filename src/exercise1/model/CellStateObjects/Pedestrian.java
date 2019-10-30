package exercise1.model.CellStateObjects;

public class Pedestrian {
    public int x;
    public int y;
    public int id;

    public Pedestrian(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pedestrian && ((Pedestrian) obj).id == this.id;
    }

    public Pedestrian copy() {
        return new Pedestrian(x, y, id);
    }
}
