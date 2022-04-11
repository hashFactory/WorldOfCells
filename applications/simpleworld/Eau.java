package applications.simpleworld;

import com.jogamp.opengl.GL2;
import objects.CommonObject;
import util.Couleur;
import worlds.World;

public class Eau extends CommonObject {

    public static void displayObjectAt(World myWorld, GL2 gl, int cellState, float x, float y, double height, float offset, float stepX, float stepY, float lenX, float lenY, float normalizeHeight) {
        //float smoothFactorAvg = ( smoothFactor[0] + smoothFactor[1] + smoothFactor[2] + smoothFactor[3] ) / 4.f;
        float altitude = (float)height * normalizeHeight + 0.2f ;
        //Couleur c = ;
        if (cellState == 2) {
            gl.glColor3f(0.0f, 0.2f, 0.6f);
        } else
            gl.glColor3f(0.0f, 0.0f, 0.4f);
        if (cellState > 0) {
            //float heightFactor, double heightBooster, float smoothFactor[]

            float xx = offset + (x * stepX);
            float yy = offset + (y * stepY);

            // correction de l'ordre des vecteurs
            gl.glVertex3f(xx - lenX, yy - lenY, altitude + 0.1f);
            gl.glVertex3f(xx - lenX, yy + lenY, altitude + 0.1f);
            gl.glVertex3f(xx + lenX, yy + lenY, altitude + 0.1f);
            gl.glVertex3f(xx + lenX, yy - lenY, altitude + 0.1f);
/*
            gl.glVertex3f(xx, yy - lenY / 6.f, altitude);
            gl.glVertex3f(xx, yy - lenY / 6.f, altitude + 2.f);
            gl.glVertex3f(xx, yy + lenY / 6.f, altitude);
            gl.glVertex3f(xx, yy + lenY / 6.f, altitude + 2.f);

 */
        }
    }
}

