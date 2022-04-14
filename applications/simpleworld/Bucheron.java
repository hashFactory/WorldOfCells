
package applications.simpleworld;

import com.jogamp.opengl.util.CustomGLEventListener;
import util.Case;

import com.jogamp.opengl.GL2;

import util.Couleur;
import worlds.World;

public class Bucheron extends Citoyen{
	
	int numero;

	int vie;

	int cptVie;
	
	int cpt;
	
	int cptBois;
	int bois;

	Ville ville;

	Case tree;
	public Bucheron(int num, int _x, int _y, WorldOfTrees _world, Ville _ville){

		super(_x,_y,_world);
		this.numero = num;
		
		this.vie = 100;

		this.ville = _ville;

		this.tree = null;

		cptVie = 0;
		cpt = 0;
		
		bois = 0;
		cptBois = 0;
		nou = 100;
	}
	
	public int getRessources(){
		int temp = this.bois;
		this.bois = 0;
		
		//System.out.println("temp "+temp);
		return temp;
	}
	
	public int getVie(){
		return this.vie;
	}
	
	public void nourrir(int nourriture){
		this.vie+=nourriture;
	}

	public void step(){
		if(cptVie == 10){
			this.vie--;
			cptVie = 0;
		}
		if(cpt == 5){
			//System.out.println(this.vie);
			if(world.estJour()){	//action pendant la journée

				if(tree == null){	//Si je n'ai pas d'arbre en vue, j'en trouve un

					tree = this.ville.rechercheRandomCaseParNumero(1);
				}
				else{							// sinon

					if (tree.distance(this.x, this.y) < 1.0){	//Si je suis sur l'arbre, je le coupe 10 fois
						if(cptBois != 10){
							bois+=5;
							cptBois++;
						}

						else {		//après avoir coupé 10 fois, l'arbre est détruit
							tree = null;
							cptBois = 0;
							world.setCell(numero,x,y);
						}
					}else{			//Si je ne suis pas sur l'arbre, je me dirige vers lui
						this.goTo(tree.x, tree.y);
					}
				}
			}
			else {	//action pendant la nuit, ce diriger vers la ville (sauf si on y est déjà
				if(this.x != ville.coordoVille.x ||	this.y != ville.coordoVille.y){
					this.goTo(ville.coordoVille.x, ville.coordoVille.y);
				}
			}
			cpt = 0;
		}
		cpt++;
		cptVie++;
	}

	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){
		Couleur c = Couleur.getBaseColor(11); // couleur ferme
		c = Couleur.mix(c, new Couleur(0.2f, 0.2f, 0.2f), 0.2f);
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
