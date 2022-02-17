package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;

import objects.UniqueDynamicObject;

public class Mine extends Structure{


	int stockFer;
	int cpt;

	static final int STOCKMAX = 100;	
	
	public Mine(int __x, int __y, World __world) 
	{
		super(__x,__y,__world);

		stockFer = 0;
		cpt = 0;
	}

	public void resetStock(){
		stockFer = 0;
	}

	public void step(){

		if(cpt==5){
			stockFer++;
			cpt=0;
		}
		cpt++;
	}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){}
}
