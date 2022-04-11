package util;

import cellularautomata.CellularAutomata;
import cellularautomata.CellularAutomataDouble;
import cellularautomata.CellularAutomataInteger;

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

    public static Case rechercheVonNeumann(int numero, Case c, CellularAutomataInteger ca) {
        // retourne la case voisine de c et null sinon
        // si numero < 100 alors la methode retourne le resultat peu importe la ville
        // si numero >= 100 la methode est stricte et retourne que si type case et ville sont egaux
        int mod = (numero >= 100) ? 1 : 100;

        for (int i = -1; i <= 1; i+=2) {
            if (ca.getCellState( (c.x+dxCA+i)%(dxCA), c.y) % mod == numero)
                return new Case((c.x+dxCA+i)%(dxCA), c.y, true);
            if (ca.getCellState(c.x, (c.y+dyCA+i)%(dyCA)) % mod == numero)
                return new Case(c.x, (c.y+dyCA+i)%(dyCA), true);
        }

        return null;
    }

    public double distance(Case l) {
        return this.distance(l.x, l.y);
    }

	public void setLibre(boolean l){
		this.libre = l;
	}
}
