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

    public Case(int _x, int _y) {
        this.x = _x;
        this.y = _y;
        this.libre = false;
    }
    public double distance(int _x, int _y) {
        double lowest = dxCA * dyCA;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                double checked = Math.sqrt(Math.pow(_x - (this.x + (i * dxCA)), 2.0) + Math.pow(_y - (this.y + (j * dyCA)), 2.0));
                lowest = Math.min(checked, lowest);
            }
        }
        return lowest;
    }

    public double distance(Case l) {
        return this.distance(l.x, l.y);
    }

	public void setLibre(boolean l){
		this.libre = l;
	}
}
