package applications.simpleworld;

import cellularautomata.CellularAutomataDouble;
import cellularautomata.CellularAutomataInteger;
import util.Case;
import util.Couleur;

import static util.Case.dxCA;
import static util.Case.dyCA;

public class EauCA extends CellularAutomataInteger {
    protected ForestCA forestCA;

    protected WorldOfTrees world;

    CellularAutomataDouble _cellsHeightValuesCA;

    public EauCA(WorldOfTrees __world, int __dx, int __dy, CellularAutomataDouble cellHeightValuesCA, ForestCA _forestCA) {
        super(__dx, __dy, true);

        forestCA = _forestCA;
        world = __world;
        _cellsHeightValuesCA = cellHeightValuesCA;
    }

    public void init() {
        for ( int x = 0 ; x != _dx ; x++ )
            for ( int y = 0 ; y != _dy ; y++ )
            {
                if (_cellsHeightValuesCA.getCellState(x,y) < 0) {
                    this.setCellState(x, y, 1); // eau stagnante
                }
                else if ( _cellsHeightValuesCA.getCellState(x,y) >= 0.4 ) {
                    if (Math.random() < 0.008) {
                        this.setCellState(x, y, 2); // eau coulante
                    }
                }
                else
                {
                    this.setCellState(x, y, 0); // pas d'eau
                }
            }
        this.swapBuffer();
    }

    public Case trouveDownstream(int numero, Case c) {
        double altitudeActu = _cellsHeightValuesCA.getCellState(c.x, c.y);

        double min = altitudeActu;
        Case plusBas = new Case(c.x, c.y);

        for (int i = -1; i <= 1; i+=2) {
            int testx = (c.x+dxCA+i)%(dxCA);
            int testy = (c.y + dyCA + i) % (dyCA);
            if (_cellsHeightValuesCA.getCellState(testx, c.y) < min && this.getCellState(testx, c.y) == numero ) {
                min = _cellsHeightValuesCA.getCellState( testx, c.y);
                plusBas = new Case(testx, c.y, true);
            }
            if (_cellsHeightValuesCA.getCellState(c.x, testy) < min && this.getCellState(c.x, testy) == numero ) {
                min = _cellsHeightValuesCA.getCellState(c.x, testy);
                plusBas = new Case(c.x, testy, true);
            }
        }

        return plusBas;
    }

    public void step() {
        for ( int i = 0 ; i != _dx ; i++ )
            for ( int j = 0 ; j != _dy ; j++ )
            {
                int state = this.getCellState(i,j);
                if (state == 2) {
                    Case c = trouveDownstream(0, new Case(i, j));
                    if (c.x != i || c.y != j) {
                        this.setCellState(i, j, 1);
                        this.setCellState(c.x, c.y, 2);
                    }
                    else
                        this.setCellState(i, j, 1);
                }
                else
                    this.setCellState(i, j, state);
            }

        this.swapBuffer();
    }
}
