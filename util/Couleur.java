package util;

import applications.simpleworld.Ville;

public class Couleur {
    public static final Couleur[] tableCouleurs = {
            new Couleur(0.0f, 0.8f, 0.1f),       // 0: couleur ville
            new Couleur(0.f, 0.6f, 0.f),        // 1: arbre
            new Couleur(1.f, 0.1f, 0.1f),       // 2: feu
            new Couleur(0.05f, 0.05f, 0.05f),   // 3: mort
            new Couleur(1.f, 1.f, 0.5f),        // 4: sable
            new Couleur(0.f, 0.f, 0.8f)         // 5: eau
    };

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

    public static Couleur intToCouleur(int id) {
        if (id >= 100 && Ville.tableCouleurs.size() >= id / 100)
            return Couleur.mix(tableCouleurs[id%100], Ville.tableCouleurs.get(id/100), 0.8f);
        return tableCouleurs[id%100];
    }
}