// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import cellularautomata.CellularAutomataDouble;
import cellularautomata.CellularAutomataInteger;
import util.Couleur;
import worlds.World;

public class ForestCA extends CellularAutomataInteger {

	CellularAutomataDouble _cellsHeightValuesCA;
	
	WorldOfTrees world;
	
	public ForestCA ( WorldOfTrees __world, int __dx , int __dy, CellularAutomataDouble cellsHeightValuesCA )
	{
		super(__dx,__dy,true ); // buffering must be true.
		
		_cellsHeightValuesCA = cellsHeightValuesCA;
		
		this.world = __world;
		Couleur.world = this.world;
	}
	
	public void init()
	{
		for ( int x = 0 ; x != _dx ; x++ )
    		for ( int y = 0 ; y != _dy ; y++ )
    		{
    			if ( _cellsHeightValuesCA.getCellState(x,y) >= 0.05 ) {
					if (Math.random() < 0.53) {// was: 0.71 {
						//this.setCellState(x, y, 1); // tree
						this.world.setCell(1, Couleur.intToCouleur(1, x, y).toArray(), x, y);
						//this.setCellState(x, y, 1);
					}
    				else {
						this.world.setCell(0, Couleur.intToCouleur(0, x, y).toArray(), x, y);
						//this.setCellState(x, y, 0); // empty
					}
    			}
    			else
    			{
    				this.setCellState(x, y, 4); // water (ignore)
    			}
    		}

    	this.swapBuffer();
	}

	public int getCellState2(int x, int y)
	{
		return this.getCurrentBuffer()[x][y];
	}

	public void step()
	{
    	for ( int i = 0 ; i != _dx ; i++ )
    		for ( int j = 0 ; j != _dy ; j++ )
    		{
				boolean changed = false;
				int state = this.getCellState(i,j) % 100;
				int code_ville = 100 * ((this.getCellState(i,j) / 100));
				int nouveau_state = state + code_ville;
    			if ( state >= 0 && state < 4 )
    			{
	    			if ( state == 1 ) // tree?
	    			{
	    				// check if neighbors are burning
	    				if (this.world.rechercheVonNeumann(2, new Case(i, j)) != null)
							nouveau_state = code_ville + 2;
							changed = true;
	    				}
	    				else
	    					if ( Math.random() < 0.0001 ) // spontaneously take fire ?
	    					{
								nouveau_state = code_ville + 2;
								changed = true;
	    					}
	    					else
	    					{
								nouveau_state = code_ville + state; // copied unchanged
								changed = false;
							}
	    			}
					else if (state == 3) {
						if (Math.random() < 0.0001) {
							nouveau_state = code_ville;
							changed = true;
						}
						else {
							nouveau_state = code_ville + 3;
							changed = false;
						}
					}
	    			else
	    			{
	        				if ( state == 2 ) // burning?
	        				{
								nouveau_state = code_ville + 3; // burnt
								changed = true;
							}
	        				else
	        				{
								nouveau_state = code_ville + state; // copied unchanged
								changed = false;
							}
					}
					changed = true;
	    			
	    			float color[] = new float[3];

					if (changed) {
						color = Couleur.intToCouleur(nouveau_state, i, j).toArray();
						//this.setCellState(i,j,nouveau_state);
						this.world.setCell(nouveau_state, i, j);
						//this.world.cellsColorValues.setCellState(i, j, color);
					}
    			}
    		}
    	this.swapBuffer();
		//this.world.cellsColorValues.swapBuffer();
	}

	
}
