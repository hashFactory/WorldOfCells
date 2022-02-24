package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;

import objects.UniqueDynamicObject;

import java.util.ArrayList;

public class Village extends Structure{

	private ArrayList<Agent> agents;	//Liste des agents du village, un village ne peut pas produire de soldats

	public Village (int __x, int __y, World __world) 
	{
		super(__x,__y,__world);

		agents = new ArrayList<Agent>();
	}

	public void step(){
	
	//Produit des agents

	//influence
		
	}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){

	}
}
