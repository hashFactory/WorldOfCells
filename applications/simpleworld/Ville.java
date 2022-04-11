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
	private int cptExtention;


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
		//this.world.setCell(numero, color, __x, __y);

		cpt = 0;
		cptExtention = 0;
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

	public Case rechercheRandomCaseParNumero(int num){
		ArrayList<Case> caseNum = new ArrayList<Case>();

		for(int i = 0 ; i < territoire.size() ; i++){
			//System.out.println(world.cellularAutomata.getCellState2(territoire.get(i).x,territoire.get(i).y) % 100)
			if((world.cellularAutomata.getCellState2(territoire.get(i).x,territoire.get(i).y) % 100) == num){
				//System.out.println("case trouver");
				caseNum.add(territoire.get(i));
			}
		}
		
		if(caseNum.size() != 0){
			System.out.println(caseNum.size());
			int random = (int)(Math.random()*(caseNum.size()));
			//System.out.println(caseNum.get(random).x+"    "+caseNum.get(random).y);
			return caseNum.get(random);
		}
		else {
			return null;
		}
	}

	public Case rechercheRandomCase(){

		if(territoire.size() != 0){
			int random = (int)(Math.random()*(territoire.size()));
			//System.out.println(caseNum.get(random).x+"    "+caseNum.get(random).y);
			return territoire.get(random);
		}
		else {
			return null;
		}
	}

	public void step(){
		/*=========étendre territoire========*/
		if(cpt % 10 == 9) {
			if((frontiere.size() != 0) ){
				this.etendreFrontiere();
				cptExtention++;
			}
			//System.out.println(agents.size());
		}
		cpt++;
		if(cptExtention == 50){
			Ferme f =new Ferme(this.numero+11,(coordoVille.x+2)%world.getWidth(),(coordoVille.y+2)%world.getWidth(),world);
			structures.add(f);
			world.uniqueDynamicObjects.add(f);
			world.setCell(this.numero+11, (coordoVille.x+2)%world.getWidth(), (coordoVille.y+2)%world.getWidth());
			Bucheron b =new Bucheron(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this);
			agents.add(b);
			world.uniqueDynamicObjects.add(b);
			cptExtention++;
		}
		/*=========placement de structure (si possible)=========*/
		
	}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){
		int x2 = (x-(offsetCA_x%myWorld.getWidth()));
		if ( x2 < 0) x2+=myWorld.getWidth();
		int y2 = (y-(offsetCA_y%myWorld.getHeight()));
		if ( y2 < 0) y2+=myWorld.getHeight();

		gl.glColor3f(c.r, c.g, c.b);

		if ( true )
		{
			Couleur.setGLCouleur(gl, c);
		}

		float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );

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
