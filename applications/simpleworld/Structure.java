package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;


public class Structure extends Agent{

	public Structure ( int __x , int __y, World __world )
	{
		super(__x,__y,__world);
	}
	
	public void step(){}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){}
}
