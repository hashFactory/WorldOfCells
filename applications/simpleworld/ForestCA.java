// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import cellularautomata.CellularAutomataDouble;
import cellularautomata.CellularAutomataInteger;
import util.Case;
import util.Couleur;
import worlds.World;

public class ForestCA extends CellularAutomataInteger {

	CellularAutomataDouble _cellsHeightValuesCA;
	
	WorldOfTrees world;

	boolean lancement;
	
	public ForestCA ( WorldOfTrees __world, int __dx , int __dy, CellularAutomataDouble cellsHeightValuesCA )
	{
		super(__dx,__dy,true ); // buffering must be true.
		
		_cellsHeightValuesCA = cellsHeightValuesCA;
		
		this.world = __world;
		Couleur.world = this.world;
		this.lancement = true;
	}
	
	public void init()
	{
		for ( int x = 0 ; x != _dx ; x++ )
    		for ( int y = 0 ; y != _dy ; y++ )
    		{
    			if ( _cellsHeightValuesCA.getCellState(x,y) >= 0.05 ) {
					if (Math.random() < 0.53)
						this.world.setCell(1, x, y); // arbre
    				else
						this.world.setCell(0, x, y); // vide
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
					/*switch (state) {
						case 1:
							if (
									this.getCellState( (i+_dx-1)%(_dx) , j ) % 100 == 2 ||
									this.getCellState( (i+_dx+1)%(_dx) , j ) % 100 == 2 ||
									this.getCellState( i , (j+_dy+1)%(_dy) ) % 100 == 2 ||
									this.getCellState( i , (j+_dy-1)%(_dy) ) % 100 == 2
							   )
								nouveau_state = code_ville + 2;
							break;
						case 2:

					}*/
					// si case est un arbre, si case voisine de von neumann brule, alors elle brule aussi
					// sinon petite chance que ca prenne feu
	    			if (state == 1) {
	    				// check if neighbors are burning
	    				if (Case.rechercheVonNeumann(2, new Case(i, j), this.world.cellularAutomata) != null)
							nouveau_state = code_ville + 2;
	    				else if ( Math.random() < 0.0001 )
							nouveau_state = code_ville + 2;
	    			}
					// si case est brulee, elle peut se decomposer
					else if (state == 3) {
						if (Math.random() < 0.01)
							nouveau_state = code_ville;
					}
					// si case est vide, il peut y avoir un arbre
					else if (state == 0) {
						if (Math.random() < 0.001)
							nouveau_state += 1;
					}
					// si case brule, alors arbre devient mort
					else if (state == 2) {
						nouveau_state += 1;
					}
	    			else
	    			{
						nouveau_state = code_ville + state; // copied unchanged
					}
					changed = true;
	    			
	    			float color[] = new float[3];

					if (nouveau_state != code_ville + state || lancement) {
						//color = Couleur.intToCouleur(nouveau_state, i, j).toArray();
						//this.setCellState(i,j,nouveau_state);
						this.world.setCell(nouveau_state, i, j);
					}
					else {
						this.world.setCellValue(i, j, nouveau_state);
					}
					//this.world.cellsColorValues.setCellState(i, j, color);
    			}
    		}
		if (lancement)
			lancement = false;
    	this.swapBuffer();
		//this.world.cellsColorValues.swapBuffer();
	}

	
}
