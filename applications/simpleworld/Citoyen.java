package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;


public class Citoyen extends Agent{

	public Citoyen ( int __x , int __y, WorldOfTrees __world )
	{
		super(__x,__y,__world);
	}
	
	public int getRessources(){
		return 0;
	}
	
	public int getVie(){
		return 0;
	}
	
	public void nourrir(int nourriture){}
	
	public void step(){}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){}
}