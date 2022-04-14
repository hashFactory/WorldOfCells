package applications.simpleworld;

import com.jogamp.opengl.util.CustomGLEventListener;
import util.Case;

import com.jogamp.opengl.GL2;

import util.Couleur;
import worlds.World;

public class Fermier extends Citoyen{
	
	int numero;

	int vie;
	int cptVie;
	
	int cpt;
	
	int nourritures;
	boolean stockPris;

	Ville ville;
	
	Ferme ferme;
	
	public Fermier(int num, int _x, int _y, WorldOfTrees _world, Ville _ville,Ferme _ferme){

		super(_x,_y,_world);
		this.numero = num;
		
		this.vie = 100;

		this.ville = _ville;
		this.ferme = _ferme;

		cptVie = 0;
		cpt = 0;
		
		nourritures = 0;

		stockPris = false;
	}
	
	public int getRessources(){
		int temp = this.nourritures;
		this.nourritures = 0;
		
		return temp;
	}
	
	public int getVie(){
		return this.vie;
	}
	
	public void nourrir(int nourriture){
		this.vie+=nourriture;
	}
	
	public void step(){
		//System.out.println(this.nourritures);
		if(cptVie == 10){
			this.vie--;
			cptVie = 0;
		}
		if(cpt == 5){
			if(world.estJour()){	//Pendant la journée
				if( (this.x != ferme.getX() )|| ( this.y != ferme.getY()) ){
					this.goTo(ferme.getX(), ferme.getY());
				}
				else{
					ferme.setProduit(true);
				}
			}
			else {								//Pendant la nuit
				if(!stockPris){
					ferme.setProduit(false);
					nourritures = ferme.prendreStock();
				}
				if(this.x != ville.coordoVille.x ||	this.y != ville.coordoVille.y){
					this.goTo(ville.coordoVille.x, ville.coordoVille.y);
				}
				else{
					
				}
			}
			cpt = 0;
		}
		cpt++;
		cptVie++;
	}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){
		Couleur c = new Couleur(0.2f, 0.1f, 0.6f);
		gl.glColor3f(c.r,c.g,c.b);
		Couleur.setGLCouleur(gl, c);

		int x2 = (x-(offsetCA_x%myWorld.getWidth()));
		if ( x2 < 0) x2+=myWorld.getWidth();
		int y2 = (y-(offsetCA_y%myWorld.getHeight()));
		if ( y2 < 0) y2+=myWorld.getHeight();

		float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );
		float altitude = height * normalizeHeight;

		float xx = offset+x2*stepX;
		float yy = offset+y2*stepY;

		lenX /= 2.f;
		lenY /= 2.f;

		gl.glVertex3f( xx-lenX, yy-lenY, altitude);
		gl.glVertex3f( xx-lenX, yy-lenY, altitude + 4.f);
		gl.glVertex3f( xx+lenX, yy-lenY, altitude + 4.f);
		gl.glVertex3f( xx+lenX, yy-lenY, altitude);

		gl.glVertex3f( xx+lenX, yy+lenY, altitude);
		gl.glVertex3f( xx+lenX, yy+lenY, altitude + 4.f);
		gl.glVertex3f( xx-lenX, yy+lenY, altitude + 4.f);
		gl.glVertex3f( xx-lenX, yy+lenY, altitude);

		gl.glVertex3f( xx+lenX, yy-lenY, altitude);
		gl.glVertex3f( xx+lenX, yy-lenY, altitude + 4.f);
		gl.glVertex3f( xx+lenX, yy+lenY, altitude + 4.f);
		gl.glVertex3f( xx+lenX, yy+lenY, altitude);

		gl.glVertex3f( xx-lenX, yy+lenY, altitude);
		gl.glVertex3f( xx-lenX, yy+lenY, altitude + 4.f);
		gl.glVertex3f( xx-lenX, yy-lenY, altitude + 4.f);
		gl.glVertex3f( xx-lenX, yy-lenY, altitude);

		c = new Couleur(Ville.mapCouleurs.get(numero));
		gl.glColor3f(c.r,c.g,c.b);
		Couleur.setGLCouleur(gl, c);

		gl.glVertex3f( xx-lenX, yy-lenY, altitude + 4.f);
		gl.glVertex3f( xx-lenX, yy+lenY, altitude + 4.f);
		gl.glVertex3f( xx+lenX, yy+lenY, altitude + 4.f);
		gl.glVertex3f( xx+lenX, yy-lenY, altitude + 4.f);
	}
}