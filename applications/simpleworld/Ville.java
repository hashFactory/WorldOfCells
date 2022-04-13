package applications.simpleworld;

import com.jogamp.opengl.GL2;

import worlds.World;

import java.util.ArrayList;
import java.util.HashMap;

import applications.simpleworld.Citoyen;

import util.Case;
import util.Couleur;

import applications.simpleworld.WorldOfTrees;

import javax.swing.plaf.metal.MetalIconFactory;

public class Ville extends Agent{

	public static HashMap<Integer, Couleur> mapCouleurs = new HashMap<>();

	public Case coordoVille;	//coordonnées de la ville

	private int nourriture;		//Quantité de nourriture, nécessaire à la création d'agents
	private int bois;		//Quantité de bois, nécessaire à la création de villages
	private int fer;		//Quantité de fer, nécessaire à la création de soldats
	private int or;			//Quantité d'or, sert de monnais, obtenus dans les mines, ou par transactions avec d'autres villes
	private int nbFermier;

	private int numero;		//identifiant de la ville
	private float[] color;
	private Couleur c;

	private ArrayList<Citoyen> citoyens;	 //Liste de tous les citoyens (fermiers et bûcherons)
	private ArrayList<Ferme> fermes; //Liste des fermes
	
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

		nourriture = 500; 
		bois = 500;
		fer = 0;
		
		nbFermier = 0;
		citoyens = new ArrayList<Citoyen>();
		fermes = new ArrayList<Ferme>();

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
			//System.out.println(caseNum.size());
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
	
	public Case recherchePlaceFerme(){
		/*recherche case sans arbre (liste)*/
		
		/*random dans la liste*/
		
		/*checker si il n'y a pas de voisin (en Moore, avec une fonction dans WorldOfTrees), si il y en a un, refaire un random*/
		
		return null;
	}

	public void step(){
		
		//System.out.println(this.nourriture +"          size "+citoyens.size());
		
		
		for(int getV = 0; getV < citoyens.size(); getV++){
			
			/*=========potentiel mort des citoyens=========*/
				
			if(citoyens.get(getV).getVie() <= 0){
				for(int display = 0 ; display < world.uniqueDynamicObjects.size(); display ++){
					if(world.uniqueDynamicObjects.get(display) == citoyens.get(getV)){
						world.uniqueDynamicObjects.remove(display);
						break;
					}
				}
				if(citoyens.get(getV) instanceof Fermier){
					nbFermier--;
				}
				citoyens.remove(getV);
				getV--;
			}	
		}
		
		/*=========récolte des ressources des citoyens=========*/
		boolean nourris = false;
		if(world.getHeure() > 20.0 && world.getHeure() < 20.1){
			for(int getR = 0; getR < citoyens.size(); getR++){
				//System.out.println("A");
				if(citoyens.get(getR) instanceof Bucheron){
					bois += citoyens.get(getR).getRessources();
				}
				else if(citoyens.get(getR) instanceof Fermier){
					nourriture += citoyens.get(getR).getRessources();
				}
				
				/*=========nourrir les citoyens=========*/
				if(!nourris){
					nourris = true;
					int ration = 10;
					if(this.nourriture - ration > 0){
						nourriture -= ration*citoyens.size();
						citoyens.get(getR).nourrir(ration);
					} else {
						citoyens.get(getR).nourrir(nourriture);
						nourriture = 0;
					}
				}
			}
		}
		
		/*=========placement de structure (si possible)=========*/
		Case placeFerme = recherchePlaceFerme();
		if( ((bois % 100) == 0) && (placeFerme != null)){
			fermes.add(new Ferme(this.numero+11,(placeFerme.x+2)%world.getWidth(),(placeFerme.y+2)%world.getWidth(),world));
		}

		/*=========création des citoyens=========*/
		
		if(bois < 100 && nourriture > 100){
			
			Bucheron b = new Bucheron(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this);
			citoyens.add(b);
			world.uniqueDynamicObjects.add(b);
			this.nourriture-=100;
		}
		
		if(nbFermier < fermes.size()){
			Fermier f = new Fermier(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this,fermes.get(nbFermier));
			citoyens.add(f);
			world.uniqueDynamicObjects.add(f);
			nbFermier++;
			this.nourriture-=100;
		}
		System.out.println("size = "+citoyens.size()+"        bois = "+bois+"    nourriture = "+nourriture);
		/*=========étendre territoire========*/
		
		if(cpt % 10 == 9 && bois > 10) {
			
			if((frontiere.size() != 0) ){
				this.etendreFrontiere();
				cptExtention++;
				bois-=10;
			}
		}
		cpt++;
		/*if(cptExtention == 50){
			Ferme f =new Ferme(this.numero+11,(coordoVille.x+2)%world.getWidth(),(coordoVille.y+2)%world.getWidth(),world);
			fermes.add(f);
			world.uniqueDynamicObjects.add(f);
			world.setCell(this.numero+11, (coordoVille.x+2)%world.getWidth(), (coordoVille.y+2)%world.getWidth());
			Fermier b =new Fermier(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this,f);
			citoyens.add(b);
			world.uniqueDynamicObjects.add(b);
			cptExtention++;
		}*/
		
		
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
