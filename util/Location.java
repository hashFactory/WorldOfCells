package util;

public class Location {
    public int x;
    public int y;

    public Location(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }

    public double distance(int _x, int _y) {
        return Math.sqrt(Math.pow(_y - this.y, 2.0) + Math.pow(_x - this.x, 2.0));
    }

    public double distance(Location l) {
        return this.distance(l.x, l.y);
    }
}
