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

	protected EauCA eauCA;

    public void init ( int __dxCA, int __dyCA, double[][] landscape )
    {
    	super.init(__dxCA, __dyCA, landscape);
    	
    	// add colors

		Case.dxCA = __dxCA;
		Case.dyCA = __dyCA;

		Couleur.world = this;
    	
    	for ( int x = 0 ; x < __dxCA ; x++ )
    		for ( int y = 0 ; y < __dyCA ; y++ )
    		{
	        	float color[] = new float[3];

	        	//float height = (float) this.getCellHeight(x, y);
				float height = (float)landscape[x][y];
		    	
		        if ( height >= 0 )
		        {
					// montagnes vertes
					color = Couleur.intToCouleur(0, x, y).toArray();
					this.setCellValue(x, y, 0);

					// sable
					if (height <= 0.05) {
						color = Couleur.intToCouleur(4, x, y).toArray();
						this.setCellValue(x, y, 4);
					}
		        }
		        else
		        {
		        	// sinon, c'est de l'eau
					color[0] = -height;
					color[1] = -height;
					color[2] = 1.f;
					this.setCellValue(x, y, 5);
		        }
				// on affecte la couleur des cases aux cellules
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
    	
    	//uniqueDynamicObjects.add(new Agent(62,62,this));
    }

	public void setCell(int num, int x, int y) {
		// si on connait que le num et la couleur est deterministe
		this.cellularAutomata.setCellState(x, y, num);
		Couleur c = Couleur.intToCouleur(num, x, y);
		this.cellsColorValues.setCellState(x, y, c.toArray());
	}

	public void setCell(int num, float[] color, int x,int y){
		// si on connait deja le num et la couleur, on l'affecte dans automate etat et couleur
		this.cellularAutomata.setCellState(x, y, num);
		this.cellsColorValues.setCellState(x, y, color);
	}
    
	public Case rechercheVoisin(int numero, Case c){
		int resX = -1;
		int resY = -1;
		boolean libre = false;
		int coordoX = c.x+dxCA;
		int coordoY = c.y+dyCA;

		// on commence par regarder a droite
		int currentCell = cellularAutomata.getCellState2((coordoX+1)%dxCA,(coordoY)%dyCA);
		//System.out.println("Cell State :"+currentCell + ", numero: " + numero);
		if(currentCell < 100){
			//System.out.println("neutre");
			resX = (coordoX+1)%dxCA;
			resY = (coordoY)%dyCA;
			return new Case(resX, resY, true);
		}

		// ensuite en haut
		currentCell = cellularAutomata.getCellState2((coordoX)%dxCA,(coordoY+1)%dyCA);
		//	System.out.println("y+1");
		if(currentCell < 100){
			resX = (coordoX)%dxCA;
			resY = (coordoY+1)%dyCA;
			return new Case(resX, resY, true);
		}

		// et a gauche
		currentCell = cellularAutomata.getCellState2((coordoX-1)%dxCA,(coordoY)%dyCA);
		//System.out.println("x-1");
		if(currentCell < 100){
			//System.out.println("neutre");
			resX = (coordoX-1)%dxCA;
			resY = (coordoY)%dyCA;
			return new Case(resX, resY, true);
		}

		// et enfin en bas
		currentCell = cellularAutomata.getCellState2((coordoX)%dxCA,(coordoY-1)%dyCA);
		if(currentCell < 100){
			resX = (coordoX)%dxCA;
			resY = (coordoY-1)%dyCA;
			return new Case(resX, resY, true);
		}

		return new Case(resX, resY, libre);
		//return new Case(resX,resY,libre);
	}
    protected void initCellularAutomata(int __dxCA, int __dyCA, double[][] landscape)
    {
    	cellularAutomata = new ForestCA(this,__dxCA,__dyCA,cellsHeightValuesCA);
    	cellularAutomata.init();

		eauCA = new EauCA(this, __dxCA, __dyCA, cellsHeightValuesCA, cellularAutomata);
		eauCA.init();
    }
    
    protected void stepCellularAutomata()
    {
    	if (iteration % 2 == 0) {
			cellularAutomata.step();
			eauCA.step();
		}
		//this.eauCA.swapBuffer();
		//this.cellularAutomata.swapBuffer();
	}
    
    protected void stepAgents()
    {
    	// nothing to do.
    	for ( int i = 0 ; i < this.uniqueDynamicObjects.size() ; i++ )
    	{
    		this.uniqueDynamicObjects.get(i).step();
			//this.cellularAutomata.swapBuffer();
    	}
		//this.eauCA.swapBuffer();
		this.cellularAutomata.swapBuffer();
	}

    public int getCellValue(int x, int y) // used by the visualization code to call specific object display.
    {
    	return cellularAutomata.getCellState(x%dxCA,y%dyCA);
    }

	public int getWaterValue(int x, int y)
	{
		return eauCA.getCellState(x%dxCA,y%dyCA);
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

	public void displayWaterAt(World _myWorld, GL2 gl, int cellState, int x,
		int y, double height, float offset,
		float stepX, float stepY, float lenX, float lenY,
		float normalizeHeight)
	{
		int val = cellState % 100;
		switch ( val )
		{
			case 1: // eau stagnante
			case 2: // eau coulante
				Eau.displayObjectAt(_myWorld,gl,cellState, x, y, height, offset, stepX, stepY, lenX, lenY, normalizeHeight);
				break;
			default:

		}
	}

	//public void displayObject(World _myWorld, GL2 gl, float offset,float stepX, float stepY, float lenX, float lenY, float heightFactor, double heightBooster) { ... } 
    
   
}
