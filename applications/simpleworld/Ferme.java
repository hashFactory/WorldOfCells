package applications.simpleworld;

import com.jogamp.opengl.GL2;

import util.Couleur;
import worlds.World;

import objects.UniqueDynamicObject;

public class Ferme extends Agent{


	int stockNourriture;
	int cpt;

	int codeVille;

	int num;
	
	boolean produit;

	static final int STOCKMAX = 500;	
	
	public Ferme(int _num, int _x, int _y, WorldOfTrees _world)
	{
		super(_x,_y,_world);

		stockNourriture = 0;
		cpt = 0;
		num = _num;
		codeVille = (_num / 100) * 100;
		
		produit = false;
	}

	public int prendreStock(){
		int temp = stockNourriture;
		stockNourriture = 0;
		
		return temp;
	}

	public void setProduit(boolean b){
		this.produit = b;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}

	public void step(){
		if(produit){
			if( cpt == 5 ){
				stockNourriture+=20;
				cpt=0;
			}
			cpt++;
		}
	}
	
	public void displayUniqueObject(World myWorld, GL2 gl, int offsetCA_x, int offsetCA_y, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight ){

		int x2 = (x-(offsetCA_x%myWorld.getWidth()));
		if ( x2 < 0) x2+=myWorld.getWidth();
		int y2 = (y-(offsetCA_y%myWorld.getHeight()));
		if ( y2 < 0) y2+=myWorld.getHeight();

		float rawheight = (float)myWorld.getCellHeight(x, y);
		float height = Math.max ( 0 , (float)myWorld.getCellHeight(x, y) );

		Couleur c = Couleur.getBaseColor(11);
		gl.glColor3f(c.r,c.g,c.b);
		Couleur.setGLCouleur(gl, c);

		float altitude = height * normalizeHeight;

		float xx = offset + (x2 * stepX);
		float yy = offset + (y2 * stepY);

		gl.glVertex3f( xx-lenX, yy-lenY, (float)myWorld.getCellHeight((x+world.getWidth()-1)%world.getWidth(), (y+world.getWidth()-1)%world.getWidth()) * normalizeHeight + 0.4f);
		gl.glVertex3f( xx-lenX, yy+lenY, (float)myWorld.getCellHeight((x+world.getWidth()-1)%world.getWidth(), (y+world.getWidth()+1)%world.getWidth()) * normalizeHeight + 0.4f);
		gl.glVertex3f( xx+lenX, yy+lenY, (float)myWorld.getCellHeight((x+world.getWidth()+1)%world.getWidth(), (y+world.getWidth()+1)%world.getWidth()) * normalizeHeight + 0.4f);
		gl.glVertex3f( xx+lenX, yy-lenY, (float)myWorld.getCellHeight((x+world.getWidth()+1)%world.getWidth(), (y+world.getWidth()-1)%world.getWidth()) * normalizeHeight + 0.4f);
	}
}
