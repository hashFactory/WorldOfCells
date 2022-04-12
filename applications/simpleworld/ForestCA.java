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
				double height = _cellsHeightValuesCA.getCellState(x,y);
    			if ( height >= 0.05 ) {
					if (Math.random() < ((height) / world.getMaxEverHeight()) / 4.0)
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
		// pour chaque case de ForestCA
    	for ( int i = 0 ; i != _dx ; i++ )
    		for ( int j = 0 ; j != _dy ; j++ )
    		{
				// on recupere l'etat neutre et le code de la ville
				int state = this.getCellState(i,j) % 100;
				int code_ville = 100 * ((this.getCellState(i,j) / 100));
				int nouveau_state = state + code_ville;
				double height = this._cellsHeightValuesCA.getCellState(i, j);
    			if ( state >= 0 && state < 4 )
    			{
					switch (state) {
						// si case est vide, il peut y avoir un arbre
						case 0:
							if (this.world.estJour() && Math.random() < (((world.getMaxEverHeight() - height) / world.getMaxEverHeight()) / 20.0) * world.getRate())
								nouveau_state = code_ville + 1;
						break;

						// si case est un arbre, si case voisine de von neumann brule, alors elle brule aussi
						// sinon petite chance que ca prenne feu
						case 1:
							if (Case.rechercheVonNeumann(2, new Case(i, j), this.world.cellularAutomata) != null)
								nouveau_state += 1;
							else if (Math.random() < 0.001 * world.getRate())
								nouveau_state = code_ville + 2;
						break;

						// si case brule, alors arbre devient mort
						case 2:
							nouveau_state = code_ville + 3;
						break;

						// si case est brulee, elle peut se decomposer
						case 3:
							if (Math.random() < world.getRate())
								nouveau_state = code_ville;
						break;

						// par defaut on garde la meme valeur
						default:
					}

					// si la case a change ou si on doit peindre pour la premiere fois
					if (nouveau_state != code_ville + state || lancement) {
						this.world.setCell(nouveau_state, i, j);
					}
					// sinon on ne met pas a jour la couleur
					else
						this.world.setCellValue(i, j, nouveau_state);
    			}
    		}
		// si on vient de finir de colorier pour la premiere fois
		if (lancement)
			lancement = false;
    	this.swapBuffer();
	}

	
}
