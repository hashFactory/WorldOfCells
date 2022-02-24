package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;

import java.util.ArrayList;

import objects.UniqueDynamicObject;

import applications.simpleworld.Structure;

public class Ville extends UniqueDynamicObject{

	private int nourriture;		//Quantité de nourriture, nécessaire à la création d'agents
	private int bois;		//Quantité de bois, nécessaire à la création de villages
	private int fer;		//Quantité de fer, nécessaire à la création de soldats
	private int or;			//Quantité d'or, sert de monnais, obtenus dans les mines, ou par transactions avec d'autres villes

		

	private ArrayList<Agent> agents;	//Liste de tous les agents (fermiers, mineurs, bûcherons et soldats)
	private ArrayList<Structure> structures; //Liste de tous les villages, fermes et mines
	
	private ArrayList<int[][]> frontiere;

	public Ville(int __x, int __y, World __world) 
	{
		super(__x,__y,__world);

		nourriture = 10; 
		bois = 0;
		fer = 0;
		
		agents = new ArrayList<Agent>();
		structures = new ArrayList<Structure>();

		frontiere = new ArrayList<int[][]>();
		frontiere.add(new int[__x][__y]);		//Cas de Base : la frontiere est la ville
		
	}

	//fonction récupère ressource

	public void step(){
	
	//toutes les X itérations, récupère les ressources + étendre influence si possible (étendre l'influence doit coûter qqch) + nourrir population

	//condition si envahie -> détruire ville, le vainqueur récupère villages, fermes et mines
		
	}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){

	}
}
