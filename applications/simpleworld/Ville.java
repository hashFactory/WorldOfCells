package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;

import java.util.ArrayList;

import applications.simpleworld.Structure;

import util.Case;
import util.Couleur;

import applications.simpleworld.WorldOfTrees;

import javax.swing.plaf.metal.MetalIconFactory;

public class Ville extends Agent{

	public static ArrayList<Couleur> tableCouleurs = new ArrayList<>();

	private Case coordoVille;	//coordonnées de la ville

	private int nourriture;		//Quantité de nourriture, nécessaire à la création d'agents
	private int bois;		//Quantité de bois, nécessaire à la création de villages
	private int fer;		//Quantité de fer, nécessaire à la création de soldats
	private int or;			//Quantité d'or, sert de monnais, obtenus dans les mines, ou par transactions avec d'autres villes
	private int nbAgents;

	private int numero;		//identifiant de la ville
	private float[] color;
	private Couleur c;

	private ArrayList<Agent> agents;	 //Liste de tous les agents (fermiers, mineurs, bûcherons et soldats)
	private ArrayList<Structure> structures; //Liste de tous les villages, fermes et mines
	
	private ArrayList<Case> frontiere;

	private WorldOfTrees world;

	private int cpt;

	public Ville(int num, int __x, int __y, WorldOfTrees __world) 
	{
		super(__x,__y,__world);
		
		coordoVille = new Case(__x,__y,false);
		numero = num;
		color = new float[3];
		c = Couleur.rand();
		Ville.tableCouleurs.add(c);

		nourriture = 10; 
		bois = 0;
		fer = 0;
		
		nbAgents = 0;
		agents = new ArrayList<Agent>();
		structures = new ArrayList<Structure>();

		frontiere = new ArrayList<Case>();
		frontiere.add(coordoVille);		//Cas de Base : la frontiere est la ville
		
		world = __world;

		cpt=0;
	}

	//fonction récupère ressource

	public void step(){

		//world.setCell(numero,color,1,1);
		if(cpt == 100) {
			Case plusProche=null;
			//choix de la case la plus proche de la ville
			double distanceMini = 50000;
			for(int i=0; i < frontiere.size() ; i++ ){
				//recherche du voisinage pour remove la case
				Case voisin	= world.rechercheVoisin(numero,frontiere.get(i));

				
				//System.out.println(voisin);
				if(voisin == null){
					System.out.println("frontière removed");
					frontiere.remove(i);
					i--;
				}
				else {
					//geCellState + check numero pour savoir si c'est libre ou non (à faire quand le problème avec ForestCA est fixé)
					for(int j=0; j < frontiere.size() ; j++ ){
						if((voisin.x == frontiere.get(j).x) && (voisin.y == frontiere.get(j).y)){
							voisin.setLibre(false);
							break;
						}
					}
					//System.out.println(voisin.libre+"distanceMini"++"        "+);
					if((voisin.libre) && (distanceMini > frontiere.get(i).distance(coordoVille))){
						System.out.println("distance");
						distanceMini = voisin.distance(coordoVille);
						System.out.println("i = "+i+"       "+distanceMini);
						plusProche = voisin;
					}
				}
			}
			
			if(plusProche != null){
				
				//System.out.println("couleur changer");
				world.setCell(numero*100,Couleur.intToCouleur(numero * 100).toArray(),plusProche.x,plusProche.y);
				plusProche.setLibre(false);
				frontiere.add(plusProche);
			}
			cpt = 0;
		}
		//toutes les X itérations, récupère les ressources + étendre influence si possible (étendre l'influence doit coûter qqch) + nourrir population

		//condition si envahie -> détruire ville, le vainqueur récupère villages, fermes et mines
		
		cpt++;
	}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){
		int x2 = (x-(offsetCA_x%myWorld.getWidth()));
		if ( x2 < 0) x2+=myWorld.getWidth();
		int y2 = (y-(offsetCA_y%myWorld.getHeight()));
		if ( y2 < 0) y2+=myWorld.getHeight();

		float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );

		gl.glColor3f(c.r, c.g, c.b);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight);

		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight);

		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight);

		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + 4.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight);

		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY-lenY, height*normalizeHeight + 5.f);
		gl.glVertex3f( offset+x2*stepX-lenX, offset+y2*stepY+lenY, height*normalizeHeight + 5.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY+lenY, height*normalizeHeight + 5.f);
		gl.glVertex3f( offset+x2*stepX+lenX, offset+y2*stepY-lenY, height*normalizeHeight + 5.f);
	}
}
