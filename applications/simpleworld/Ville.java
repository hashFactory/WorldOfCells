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

	private boolean nourris;
	private int nourriture;		//Quantité de nourriture, nécessaire à la création d'agents
	private int bois;		//Quantité de bois, nécessaire à la création de villages
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
	private int cpt2;
	private int cptferme;
	private int cptExtention;


	public Ville(int num, int __x, int __y, WorldOfTrees __world) 
	{
		super(__x,__y,__world);
		
		coordoVille = new Case(__x,__y,false);
		numero = num*100;
		color = new float[3];
		c = Couleur.rand();
		Ville.mapCouleurs.put(numero, c);

		nourris = false;
		nourriture = 500; 
		bois = 300;
		
		
		nbFermier = 0;
		citoyens = new ArrayList<Citoyen>();
		fermes = new ArrayList<Ferme>();

		frontiere = new ArrayList<Case>();
		frontiere.add(coordoVille);		//Cas de Base : la frontiere est la ville
		
		territoire = new ArrayList<Case>();

		world = __world;
		//this.world.setCell(numero, color, __x, __y);

		cpt = 0;
		cpt2 = 0;
		cptferme = 0;
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
			//bois -= 5;
			voisin = plusProche;
			int val = world.cellularAutomata.getCellState2(plusProche.x, plusProche.y);
			world.setCell(val+numero,Couleur.intToCouleur(val+numero, voisin.x, voisin.y).toArray(),voisin.x,voisin.y);
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
		int random = (int)(Math.random()*(territoire.size()));

		while(!world.rechercheEmplacementValideMoore(territoire.get(random))){
			random = (int)(Math.random()*(territoire.size()));
		}
		return territoire.get(random);
		/*random dans la liste*/
		
		/*checker si il n'y a pas de voisin (en Moore, avec une fonction dans WorldOfTrees), si il y en a un, refaire un random*/
		
		//return null;
	}

	public void step(){
		
		//System.out.println(this.nourriture +"          size "+citoyens.size());
		
		if(cpt2 == 5){
			for(int getV = 0; getV < citoyens.size(); getV++){
				
				/*=========potentiel mort des citoyens=========*/
					
				if(citoyens.get(getV).getVie() <= -1000){
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

			if(world.getHeure() > 20.0 && !nourris){
				nourris = true;
				for(int getR = 0; getR < citoyens.size(); getR++){
					
					if(citoyens.get(getR) instanceof Bucheron){
						bois += citoyens.get(getR).getRessources();
					}
					else if(citoyens.get(getR) instanceof Fermier){
					//	System.out.println("A  "+citoyens.get(getR).getnourriture());
						nourriture += citoyens.get(getR).getRessources();
					//	System.out.println("B  "+citoyens.get(getR).getnourriture());
					}

					/*=========nourrir les citoyens=========*/
					int ration = 10;
					//for(int nour = 0; nour < citoyens.size();nour++){
						if(this.nourriture > 0){
							if(this.nourriture - ration > 0){
								nourriture -= ration;
								citoyens.get(getR).nourrir(ration);
							} else {
								citoyens.get(getR).nourrir(nourriture);
								nourriture = 0;
							}
						}

				}
			}
			if(world.getHeure() < 8) {
				nourris = false;
			}
			
			/*=========placement de structure (si possible)=========*/
			//System.out.println("cpt "+cptferme+"    territoire  "+territoire.size());
			 if(territoire.size() > 1 && cptferme > 30){
				//System.out.println("test");
				Case placeFerme = rechercheRandomCaseParNumero(0);
				//System.out.println(placeFerme);
				if (placeFerme != null && bois >= 50){
					assert placeFerme != null;
					Ferme fe = new Ferme(this.numero+11,(placeFerme.x)%world.getWidth(),(placeFerme.y)%world.getWidth(),world);
					fermes.add(fe);
					world.uniqueDynamicObjects.add(fe);
					world.setCell(this.numero+11, (placeFerme.x)%world.getWidth(), (placeFerme.y)%world.getWidth());
					bois-=50;
					//System.out.println("creation ferme   "+this.nourriture+"   nbFermier = "+nbFermier+"     size = "+fermes.size());
					if(this.nourriture > 0){
						//System.out.println("fermier 1");
						if((nbFermier < fermes.size()) && (this.nourriture > 50)){
							//System.out.println("creation fermier");
							Fermier f = new Fermier(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this,fermes.get(nbFermier));
							citoyens.add(f);
							world.uniqueDynamicObjects.add(f);
							nbFermier++;
							this.nourriture-=50;
						}
					}
				}
				cptferme = 0;
			}
			cptferme++;
			/*=========création des citoyens=========*/
			if(this.nourriture >= 0){
				if(nbFermier < fermes.size()){
					//System.out.println("creation fermier");
					Fermier f = new Fermier(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this,fermes.get(nbFermier-1));
					citoyens.add(f);
					world.uniqueDynamicObjects.add(f);
					nbFermier++;
					this.nourriture-=50;
				}
				else if((bois < 150 && nourriture > 200) || Math.random() < ((1000.0-(double)(bois)) / 1000.0)){
				//	System.out.println("creation bucheron");
					Bucheron b = new Bucheron(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this);
					citoyens.add(b);
					world.uniqueDynamicObjects.add(b);
					this.nourriture-=50;
				}
				
				
			}
			cpt2 = 0;
		}
		cpt2++;
		//System.out.println("sizeCitoyens = "+citoyens.size()+"      sizeFermes = "+fermes.size()+"        bois = "+bois+"    nourriture = "+nourriture);
		/*=========étendre territoire========*/
		
		if(cpt % 10 == 9 && bois > 5) {
			
			if((frontiere.size() != 0) ){
				this.etendreFrontiere();
				cptExtention++;
				bois-=5;
			}
		}
		cpt++;
		/*if(cptExtention == 50){
			System.out.println("test");
			Ferme fe1 =new Ferme(this.numero+11,(coordoVille.x+2)%world.getWidth(),(coordoVille.y+2)%world.getWidth(),world);
			fermes.add(fe1);
			world.uniqueDynamicObjects.add(fe1);
			Ferme fe2 =new Ferme(this.numero+11,(coordoVille.x-2)%world.getWidth(),(coordoVille.y-2)%world.getWidth(),world);
			fermes.add(fe2);
			world.uniqueDynamicObjects.add(fe2);
			world.setCell(this.numero+11, (coordoVille.x+2)%world.getWidth(), (coordoVille.y+2)%world.getWidth());
			world.setCell(this.numero+11, (coordoVille.x-2)%world.getWidth(), (coordoVille.y-2)%world.getWidth());
			Fermier f1 =new Fermier(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this,fe1);
			citoyens.add(f1);
			world.uniqueDynamicObjects.add(f1);
			Fermier f2 =new Fermier(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this,fe2);
			citoyens.add(f2);
			world.uniqueDynamicObjects.add(f2);
			Bucheron b1 =new Bucheron(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this);
			citoyens.add(b1);
			world.uniqueDynamicObjects.add(b1);
			Bucheron b2 =new Bucheron(numero,(coordoVille.x+1)%world.getWidth(),(coordoVille.y)%world.getWidth(),world,this);
			citoyens.add(b2);
			world.uniqueDynamicObjects.add(b2);
			
		}
		cptExtention++;*/
		
		
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
