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
	}
	
	public void init()
	{
		for ( int x = 0 ; x != _dx ; x++ )
    		for ( int y = 0 ; y != _dy ; y++ )
    		{
    			if ( _cellsHeightValuesCA.getCellState(x,y) >= 0.05 ) {
					if (Math.random() < 0.53) {// was: 0.71 {
						//this.setCellState(x, y, 1); // tree
						this.world.setCell(1, Couleur.intToCouleur(1).toArray(), x, y);
					}
    				else
    					this.setCellState(x, y, 0); // empty
						//this.world.setCell(0, Couleur.intToCouleur())
    			}
    			else
    			{
    				this.setCellState(x, y, 4); // water (ignore)
    			}
    		}
    	this.swapBuffer();

	}

	public void step()
	{
    	for ( int i = 0 ; i != _dx ; i++ )
    		for ( int j = 0 ; j != _dy ; j++ )
    		{
				boolean changed = false;
				int state = this.getCellState(i,j) % 100;
    			if ( state >= 0 && state < 4 )
    			{
	    			if ( state == 1 ) // tree?
	    			{
	    				// check if neighbors are burning
	    				if ( 
	    						this.getCellState( (i+_dx-1)%(_dx) , j ) % 100 == 2 ||
	    						this.getCellState( (i+_dx+1)%(_dx) , j ) % 100 == 2 ||
	    						this.getCellState( i , (j+_dy+1)%(_dy) ) % 100 == 2 ||
	    						this.getCellState( i , (j+_dy-1)%(_dy) ) % 100 == 2
	    					)
	    				{
	    					this.setCellState(i,j,2);
							changed = true;
	    				}
	    				else
	    					if ( Math.random() < 0.00001 ) // spontaneously take fire ?
	    					{
	    						this.setCellState(i,j,2);
								changed = true;
	    					}
	    					else
	    					{
	    						this.setCellState(i,j,this.getCellState(i,j)); // copied unchanged
								changed = true;
							}
	    			}
	    			else
	    			{
	        				if ( state == 2 ) // burning?
	        				{
	        					this.setCellState(i,j,3); // burnt
								changed = true;
	        				}
	        				else
	        				{
	        					this.setCellState(i,j,this.getCellState(i,j)); // copied unchanged
	        				}
	    			}
	    			
	    			float color[] = new float[3];
/*
	    			switch ( this.getCellState(i, j) % 100 )
	    			{
	    				case 0:
	    					break;
	    				case 1:
	    					color[0] = 0.f;
	    					color[1] = 0.3f;
	    					color[2] = 0.f;
	    					break;
	    				case 2: // burning tree
	    					color[0] = 1.f;
	    					color[1] = 0.f;
	    					color[2] = 0.f;
	    					break;
	    				case 3: // burnt tree
	    					color[0] = 0.f;
	    					color[1] = 0.f;
	    					color[2] = 0.f;
	    					break;
						case 33:
							color[0] = 1.0f;
							color[1] = 0.2f;
							color[2] = 1.0f;
							break;
	    				default:
	    					color[0] = 0.5f;
	    					color[1] = 0.5f;
	    					color[2] = 0.5f;
	    					System.out.print("cannot interpret CA state: " + this.getCellState(i, j));
	    					System.out.println(" (at: " + i + "," + j + " -- height: " + this.world.getCellHeight(i,j) + " )");
	    			}	   */

					if (changed) {
						color = Couleur.intToCouleur(this.getCellState(i, j)).toArray();
						this.world.cellsColorValues.setCellState(i, j, color);
					}
    			}
    		}
    	this.swapBuffer();
	}

	
}
