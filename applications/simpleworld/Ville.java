package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;

import java.util.ArrayList;
import java.util.HashMap;

import applications.simpleworld.Structure;

import util.Case;
import util.Couleur;

import applications.simpleworld.WorldOfTrees;

import javax.swing.plaf.metal.MetalIconFactory;

public class Ville extends Agent{

	public static HashMap<Integer, Couleur> mapCouleurs = new HashMap<>();

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
	private ArrayList<Case>	territoire;

	private WorldOfTrees world;

	private int cpt;


	public Ville(int num, int __x, int __y, WorldOfTrees __world) 
	{
		super(__x,__y,__world);
		
		coordoVille = new Case(__x,__y,false);
		numero = num*100;
		color = new float[3];
		c = Couleur.rand();
		Ville.mapCouleurs.put(numero, c);

		nourriture = 10; 
		bois = 0;
		fer = 0;
		
		nbAgents = 0;
		agents = new ArrayList<Agent>();
		structures = new ArrayList<Structure>();

		frontiere = new ArrayList<Case>();
		frontiere.add(coordoVille);		//Cas de Base : la frontiere est la ville
		
		territoire = new ArrayList<Case>();

		world = __world;

		cpt=0;
	}

	public void etendreFrontiere(){
		Case plusProche=null;
		//choix de la case la plus proche de la ville
		double distanceMini = 50000;
		Case voisin=null;
			
		for(int i=0; i < frontiere.size() ; i++ ){
			voisin = world.rechercheVoisin(numero,frontiere.get(i));
			//remove
			if(voisin.x == -1){
				System.out.println("frontière removed");
				territoire.add(frontiere.get(i));
				frontiere.remove(i);
				i--;
			}
			else{//distance
				if(distanceMini > voisin.distance(coordoVille)){
					distanceMini = voisin.distance(coordoVille);
					plusProche = new Case(voisin.x, voisin.y, voisin.libre);
				}
			}
			
			
		}
			
		if(plusProche!=null && plusProche.x != -1){
			voisin = plusProche;
			int val = world.cellularAutomata.getCellState2(plusProche.x, plusProche.y);
			world.setCell(val+numero,Couleur.intToCouleur(val+numero).toArray(),voisin.x,voisin.y);
			voisin.setLibre(false);
			frontiere.add(new Case(voisin.x, voisin.y, false));
		}
	}

	public void step(){
		/*=========étendre territoire========*/
		if(cpt == 5) {
			if(frontiere.size() != 0){
				this.etendreFrontiere();
			}			

			cpt = 0;

		}
		cpt++;

		/*=========placement de structure (si possible)=========*/
		
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
