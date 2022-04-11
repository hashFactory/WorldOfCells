// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package objects;

import com.jogamp.opengl.GL2;

import util.Couleur;
import worlds.World;

public class Tree extends CommonObject {

    public static void displayObjectAt(World myWorld, GL2 gl, int cellState, float x, float y, double height, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight )
    {
        //float smoothFactorAvg = ( smoothFactor[0] + smoothFactor[1] + smoothFactor[2] + smoothFactor[3] ) / 4.f;

        Couleur c = Couleur.tableCouleurs[cellState % 100];
        Couleur.setGLCouleur(gl, c);

        if ( cellState > 0 )
        {
    		float altitude = (float)height * normalizeHeight;
    		
    		//float heightFactor, double heightBooster, float smoothFactor[]

            float xx = offset+(x*stepX);
            float yy = offset+(y*stepY);

            // correction de l'ordre des vecteurs
            gl.glVertex3f(xx-lenX/4.f, yy, altitude);
            gl.glVertex3f(xx-lenX/4.f, yy, altitude+3.f);
            gl.glVertex3f(xx+lenX/4.f, yy, altitude+3.f);
            gl.glVertex3f(xx+lenX/4.f, yy, altitude);

            gl.glVertex3f(xx, yy-lenY/3.f, altitude);
            gl.glVertex3f(xx, yy-lenY/3.f, altitude+2.f);
            gl.glVertex3f(xx, yy+lenY/3.f, altitude);
            gl.glVertex3f(xx, yy+lenY/3.f, altitude+2.f);
        }
    }

}
