package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;

import java.util.ArrayList;

import applications.simpleworld.Structure;

import util.Case;

public class Ville extends Agent{

	private Case coordoVille;	//coordonnées de la ville

	private int nourriture;		//Quantité de nourriture, nécessaire à la création d'agents
	private int bois;		//Quantité de bois, nécessaire à la création de villages
	private int fer;		//Quantité de fer, nécessaire à la création de soldats
	private int or;			//Quantité d'or, sert de monnais, obtenus dans les mines, ou par transactions avec d'autres villes
	private int nbAgents;

	private int numero;		//identifiant de la ville

	private ArrayList<Agent> agents;	 //Liste de tous les agents (fermiers, mineurs, bûcherons et soldats)
	private ArrayList<Structure> structures; //Liste de tous les villages, fermes et mines
	
	private ArrayList<Case> frontiere;

	public Ville(int num, int __x, int __y, World __world) 
	{
		super(__x,__y,__world);
		
		coordoVille = new Case(__x,__y);
		numero = num;

		nourriture = 10; 
		bois = 0;
		fer = 0;
		
		nbAgents = 0;
		agents = new ArrayList<Agent>();
		structures = new ArrayList<Structure>();

		frontiere = new ArrayList<Case>();
		frontiere.add(new Case(__x,__y));		//Cas de Base : la frontiere est la ville
		
	}

	//fonction récupère ressource

	public void step(){
	
	//toutes les X itérations, récupère les ressources + étendre influence si possible (étendre l'influence doit coûter qqch) + nourrir population

	//condition si envahie -> détruire ville, le vainqueur récupère villages, fermes et mines
		
	}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){
		int x2 = (x-(offsetCA_x%myWorld.getWidth()));
		if ( x2 < 0) x2+=myWorld.getWidth();
		int y2 = (y-(offsetCA_y%myWorld.getHeight()));
		if ( y2 < 0) y2+=myWorld.getHeight();

		float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );

		gl.glColor3f(1.f,0.f,1.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight);

		gl.glColor3f(1.f,0.f,1.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight);

		gl.glColor3f(1.f,0.f,1.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight);

		gl.glColor3f(1.f,0.f,1.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight);

		gl.glColor3f(1.0f,0.f,0.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + 5.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + 5.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + 5.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + 5.f);
	}
}
