package util;

import applications.simpleworld.Ville;
import applications.simpleworld.WorldOfTrees;
import cellularautomata.CellularAutomataDouble;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Couleur {
    public static final Couleur[] tableCouleurs = {
            new Couleur(0.0f, 0.8f, 0.1f),      // 0: couleur ville
            new Couleur(0.0f, 0.6f, 0.0f),      // 1: arbre
            new Couleur(0.9f, 0.f, 0.f),       // 2: feu
            new Couleur(0.05f, 0.05f, 0.05f),   // 3: mort
            new Couleur(1.f, 1.f, 0.5f),        // 4: sable
            new Couleur(0.f, 0.f, 0.8f),         // 5: eau
            new Couleur(0.f, 0.f, 0.f),         // 6: --
            new Couleur(0.f, 0.f, 0.f),         // 7: --
            new Couleur(0.f, 0.f, 0.f),         // 8: --
            new Couleur(0.f, 0.f, 0.f),         // 9: --
            new Couleur(0.f, 0.f, 0.f),         // 10: ville
            new Couleur(0.2f, 0.04f, 0.05f)         // 11: ferme
    };

    public static WorldOfTrees world;

    public float r;
    public float g;
    public float b;

    public Couleur() {
        r = g = b = 1.f;
    }

    public Couleur(float color[]) {
        assert(color.length == 3);
        r = color[0];
        g = color[1];
        b = color[2];
    }

    public Couleur(float _r, float _g, float _b) {
        r = _r;
        g = _g;
        b = _b;
    }

    public Couleur(Couleur c) {
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
    }

    public float[] toArray() {
        return new float[]{this.r, this.g, this.b};
    }

    public String toString() {
        return "rgb(" + r + ", " + g + ", " + b + ")";
    }

    public Couleur mix(Couleur c) {
        float _r = 1.f - (1.f - c.r) * (1.f - this.r);
        float _g = 1.f - (1.f - c.g) * (1.f - this.g);
        float _b = 1.f - (1.f - c.b) * (1.f - this.b);
        return new Couleur(_r, _g, _b);
    }

    public static Couleur mix(Couleur c1, Couleur c2, float factor) {
        float _r = 1.f - (1.f - c1.r) * (1.f - c2.r);
        float _g = 1.f - (1.f - c1.g) * (1.f - c2.g);
        float _b = 1.f - (1.f - c1.b) * (1.f - c2.b);
        /*float _r = (c1.r + c2.r) / 2.f;
        float _g = (c1.g + c2.g) / 2.f;
        float _b = (c1.b + c2.b) / 2.f;*/
        return new Couleur(_r, _g, _b);
        //return c1.mix(c2);
    }

    public static Couleur mix(Couleur c1, Couleur c2) {
        return c1.mix(c2);
    }

    public static Couleur rand() {
        return new Couleur((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public static void setGLCouleur(GL2 gl, Couleur c) {
        gl.glColor3f(c.r, c.g, c.b);
        gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, c.toArray(), 0 );
        gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, c.toArray(), 0 );
        gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, c.toArray(), 0 );
        gl.glMateriali( GL.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 2 );
        float[] colorBlack  = {0.0f,0.0f,0.0f,1.0f};
        gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL2.GL_EMISSION, colorBlack, 0 );
    }

    public static Couleur heureToCouleur(double heure) {
        if (heure < 6)
            return new Couleur(0.f, 0.f, 0.1f);
        else if (heure < 8)
            return new Couleur(0.f, 0.f, 0.7f - (float)((8.0 - heure) / 4.0));
        else if (heure < 18)
            return new Couleur(0.f, 0.f, 0.7f);
        else if (heure < 20)
            return new Couleur(0.f, 0.f, 0.1f + (float)((20.0 - heure) / 4.0));
        else
            return new Couleur(0.f, 0.f, 0.1f);
    }

    public static Couleur getBaseColor(int id) {
        return switch(id % 100) {
            case 1: new Couleur(0.f, 0.6f - (float) (0.1 * Math.random()), 0.f);
            case 2: new Couleur(1.f - (float) (0.1 * Math.random()), 0.f, 0.f);
            case 3:
                    new Couleur(0.f + (float) (0.1 * Math.random()), 0.f + (float) (0.1 * Math.random()), 0.f + (float) (0.2 * Math.random()));
            case 4: new Couleur(1.f - (float) (0.1 * Math.random()), 1.f - (float) (0.1 * Math.random()), 0.5f);
            default: tableCouleurs[id % 100];
        };
    }

    public static Couleur intToCouleur(int id, int x, int y) {
        if (id % 100 == 0) {
            float pourcentage = (float)(world.getCellHeight(x, y) / world.getMaxEverHeight());
            Couleur c = new Couleur(pourcentage, 0.9f + 0.1f * pourcentage, pourcentage);
            if (Ville.mapCouleurs.containsKey((id/100)*100))
                return Couleur.mix(c, Ville.mapCouleurs.get((id/100)*100), 0.97f);
            else
                return c;
        }
        return intToCouleur(id);
    }

    public static Couleur intToCouleur(int id) {
        if (id >= 100 && Ville.mapCouleurs.containsKey((id/100)*100))
            return Couleur.mix(Couleur.getBaseColor(id % 100), Ville.mapCouleurs.get((id/100)*100), 0.97f);

        return Couleur.getBaseColor(id % 100);
    }
}
