
package applications.simpleworld;

import com.jogamp.opengl.util.CustomGLEventListener;
import util.Case;

import com.jogamp.opengl.GL2;

import util.Couleur;
import worlds.World;

public class Bucheron extends Agent{
	
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
	}

	public void step(){
		if(cptVie == 100){
			this.vie--;			
			cptVie = 0;
		}
		if(cpt == 5){
			if(tree == null){	
				//tree = this.ville.rechercheRandomCase();
				tree = this.ville.rechercheRandomCaseParNumero(1);
				System.out.println("x = "+tree.x+"     y = "+tree.y);
			}
			else{
				if (Math.random() < 0.1) {
					//System.out.println("num: " + numero + ", distance: " + tree.distance(this.x, this.y));
					if (tree.distance(this.x, this.y) < 1.0)
						if(cptBois != 5){
							bois+=10;
							cptBois++;
						}
						else {
							tree = null;
							cptBois = 0;
							world.setCell(numero,x,y);
						}
					else
						this.goTo(tree.x, tree.y, numero);
					}
				}
				cpt =0;
			}
		cpt++;
		cptVie++;
	}

	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){
		Couleur c = new Couleur(Ville.mapCouleurs.get(numero));
		c = Couleur.mix(c, new Couleur(0.5f, 0.5f, 0.5f), 0.2f);
		gl.glColor3f(c.r,c.g,c.b);

		int x2 = (x-(offsetCA_x%myWorld.getWidth()));
		if ( x2 < 0) x2+=myWorld.getWidth();
		int y2 = (y-(offsetCA_y%myWorld.getHeight()));
		if ( y2 < 0) y2+=myWorld.getHeight();

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
