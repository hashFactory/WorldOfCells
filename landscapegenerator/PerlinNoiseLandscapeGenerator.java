// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package landscapegenerator;

public class PerlinNoiseLandscapeGenerator {

    public static double[][] generatePerlinNoiseLandscape ( int dxView, int dyView, double scaling, double landscapeAltitudeRatio, int perlinLayerCount )
    {
    	double landscape[][] = new double[dxView][dyView];


    	double fac = Math.random();
    	double fac2 = Math.random();
    	double fac3 = Math.random();

    	// A ECRIRE ! 
    	// ...
    	for ( int x = 0 ; x < dxView ; x++ )
    		for ( int y = 0 ; y < dyView ; y++ )
    			landscape[x][y] = generateHeight(fac, 100, (float)x / 400.0) - generateHeight(fac2, 60, (float)y / 100.0);

        //List<>

    	// ...
    	// cf. http://freespace.virgin.net/hugo.elias/models/m_perlin.htm pour une explication


    	// scaling and polishing
    	landscape = LandscapeToolbox.scaleAndCenter(landscape, scaling, landscapeAltitudeRatio);
    	landscape = LandscapeToolbox.smoothLandscape(landscape);
    	
		return landscape;
    }

    static double generateHeight(double a, double b, double x) {
        return Math.sin(a * x) * b;
    }
}
