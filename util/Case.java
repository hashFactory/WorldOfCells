package util;

public class Case {
    public int x;
    public int y;

    public Case(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }

    public double distance(int _x, int _y) {
        return Math.sqrt(Math.pow(_y - this.y, 2.0) + Math.pow(_x - this.x, 2.0));
    }

    public double distance(Case l) {
        return this.distance(l.x, l.y);
    }
}
