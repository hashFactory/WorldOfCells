// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import com.jogamp.opengl.GL2;

import objects.*;
import util.Case;
import util.Couleur;
import worlds.World;

public class WorldOfTrees extends World {

    protected ForestCA cellularAutomata;

    public void init ( int __dxCA, int __dyCA, double[][] landscape )
    {
    	super.init(__dxCA, __dyCA, landscape);
    	
    	// add colors

		Case.dxCA = __dxCA;
		Case.dyCA = __dyCA;
    	
    	for ( int x = 0 ; x < __dxCA ; x++ )
    		for ( int y = 0 ; y < __dyCA ; y++ )
    		{
	        	float color[] = new float[3];

	        	//float height = (float) this.getCellHeight(x, y);
				float height = (float)landscape[x][y];
		    	
		        if ( height >= 0 )
		        {
		        	// snowy mountains
		        	/*
		        	color[0] = height / (float)this.getMaxEverHeight();
					color[1] = height / (float)this.getMaxEverHeight();
					color[2] = height / (float)this.getMaxEverHeight();
					/**/

					// green mountains
					/**/
					color[0] = height / ( (float)this.getMaxEverHeight() );
					color[1] = 0.9f + 0.1f * height / ( (float)this.getMaxEverHeight() );
					color[2] = height / ( (float)this.getMaxEverHeight() );
					this.setCellValue(x, y, 0);
					/**/

					// sand
					if (height <= 0.05) {
						color = Couleur.intToCouleur(4).toArray();
						this.setCellValue(x, y, 4);
					}
		        }
		        else
		        {
		        	// water
					color[0] = -height;
					color[1] = -height;
					color[2] = 1.f;
					this.setCellValue(x, y, 5);
		        }
		        this.cellsColorValues.setCellState(x, y, color);
    		}
    	
    	// add some objects
    	for ( int i = 0 ; i < 11 ; i++ )
    	{
    		if ( i%10 == 0 )
    			uniqueObjects.add(new Monolith(5,5+i,this));
    		else
    			uniqueObjects.add(new BridgeBlock(10,10+i,this));
    	}
    	
    	uniqueDynamicObjects.add(new Agent(62,62,this));
    	
    }

	public void setCell(int num, int x, int y) {
		//this.cellsColorValues.setCellState();
	}

	public void setCell(int num, float[] color, int x,int y){
		//préparation couleur
		/*color[0] = 0.6f;
		color[1] = 1.f;
		color[2] = 1.f;*/

		this.cellularAutomata.setCellState(x, y, num);
		this.cellsColorValues.setCellState(x, y, color);
	}
    
	public Case rechercheVoisin(int numero, Case c){
		int resX = -1;
		int resY = -1;
		boolean libre = false;
		int coordoX = c.x+dxCA;
		int coordoY = c.y+dyCA;
		int currentCell = cellularAutomata.getCellState2((coordoX+1)%dxCA,(coordoY)%dyCA);
		//libre = currentCell < 100;
		System.out.println("Cell State :"+currentCell + ", numero: " + numero);
		if((currentCell < numero) || (currentCell > numero+99)){
		//	System.out.println("x+1");
			if(currentCell < 100){
				//System.out.println("neutre");
				resX = (coordoX+1)%dxCA;
				resY = (coordoY)%dyCA;
				libre = true;
				return new Case(resX, resY, libre);
			} else {
				if(resX == -1 && !libre){
					resX = (coordoX+1)%dxCA;
					resY = (coordoY)%dyCA;
				}
			}
		}

		currentCell = cellularAutomata.getCellState2((coordoX)%dxCA,(coordoY+1)%dyCA);
		//libre = currentCell < 100;
		if((currentCell < numero) || (currentCell > numero+99)){
		//	System.out.println("y+1");
			if(currentCell < 100){
			//	System.out.println("neutre");
				resX = (coordoX)%dxCA;
				resY = (coordoY+1)%dyCA;
				libre = true;
				return new Case(resX, resY, libre);
			} else {
				if(resX == -1 && !libre){
					resX = (coordoX)%dxCA;
					resY = (coordoY+1)%dyCA;
				}
			}
		}

		currentCell = cellularAutomata.getCellState2((coordoX-1)%dxCA,(coordoY)%dyCA);
		//libre = currentCell < 100;
		if((currentCell < numero) || (currentCell > numero+99)){
			//System.out.println("x-1");
			if(currentCell < 100){
				//System.out.println("neutre");
				resX = (coordoX-1)%dxCA;
				resY = (coordoY)%dyCA;
				libre = true;
				return new Case(resX, resY, libre);
			} else {
				if(resX == -1 && !libre){
					resX = (coordoX-1)%dxCA;
					resY = (coordoY)%dyCA;
				}
			}
		}

		currentCell = cellularAutomata.getCellState2((coordoX)%dxCA,(coordoY-1)%dyCA);
		//libre = currentCell < 100;
		if((currentCell < numero) || (currentCell > numero+99)){
		//	System.out.println("y-1");
			if(currentCell < 100){
			//	System.out.println("neutre");
				resX = (coordoX)%dxCA;
				resY = (coordoY-1)%dyCA;
				libre = true;
				return new Case(resX, resY, libre);
			} else {
				if(resX == -1 && !libre){
					resX = (coordoX)%dxCA;
					resY = (coordoY-1)%dyCA;
				}
			}
		}
		System.out.println(resX);
		return new Case(resX, resY, libre);
		//return new Case(resX,resY,libre);
	}
    protected void initCellularAutomata(int __dxCA, int __dyCA, double[][] landscape)
    {
    	cellularAutomata = new ForestCA(this,__dxCA,__dyCA,cellsHeightValuesCA);
    	cellularAutomata.init();
    }
    
    protected void stepCellularAutomata()
    {
    	if (iteration % 10 == 0) {
			cellularAutomata.step();
		}
		this.cellularAutomata.swapBuffer();

	}
    
    protected void stepAgents()
    {
    	// nothing to do.
    	for ( int i = 0 ; i < this.uniqueDynamicObjects.size() ; i++ )
    	{
    		this.uniqueDynamicObjects.get(i).step();
			//this.cellularAutomata.swapBuffer();
    	}
	}

    public int getCellValue(int x, int y) // used by the visualization code to call specific object display.
    {
    	return cellularAutomata.getCellState(x%dxCA,y%dyCA);
    }

    public void setCellValue(int x, int y, int state)
    {
    	cellularAutomata.setCellState( x%dxCA, y%dyCA, state);
    }

	public void displayObjectAt(World _myWorld, GL2 gl, int cellState, int x,
			int y, double height, float offset,
			float stepX, float stepY, float lenX, float lenY,
			float normalizeHeight) 
	{
		int val = cellState % 100;
		switch ( val )
		{
		case 1: // trees: green, fire, burnt
		case 2:
		case 3:
			Tree.displayObjectAt(_myWorld,gl,cellState, x, y, height, offset, stepX, stepY, lenX, lenY, normalizeHeight);
			break;
			case 33:
			break;

		default:
			// nothing to display at this location.
		}
	}

	//public void displayObject(World _myWorld, GL2 gl, float offset,float stepX, float stepY, float lenX, float lenY, float heightFactor, double heightBooster) { ... } 
    
   
}
