package util;

public class Case {
    public int x;
    public int y;
    public static int dxCA;
    public static int dyCA;
	public boolean libre;

    public Case(int _x, int _y,boolean l) {
        this.x = _x;
        this.y = _y;
		this.libre=l;
    }

    public double distance(int _x, int _y) {
        return Math.sqrt(Math.pow(_y - this.y, 2.0) + Math.pow(_x - this.x, 2.0));
    }

    public double distance(Case l) {
        return this.distance(l.x, l.y);
    }

	public void setLibre(boolean l){
		this.libre = l;
	}
}
