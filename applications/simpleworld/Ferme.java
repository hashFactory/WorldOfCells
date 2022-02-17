package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;

import objects.UniqueDynamicObject;

public class Ferme extends Structure{


	int stockNourriture;
	int cpt;

	static final int STOCKMAX = 100;	
	
	public Ferme(int __x, int __y, World __world) 
	{
		super(__x,__y,__world);

		stockNourriture = 0;
		cpt = 0;
	}

	public void resetStock(){
		stockNourriture = 0;
	}

	public void step(){

		if(cpt==5){
			stockNourriture++;
			cpt=0;
		}
		cpt++;
	}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){}
}
