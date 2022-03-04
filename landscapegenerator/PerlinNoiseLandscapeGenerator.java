// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package landscapegenerator;

class SimplerNoise {
	// recuperre de https://stackoverflow.com/questions/28755988/perlin-noise-for-terrain-generation
	static float noise2(int x, int y) {
		int n = x + y * 57;
		n = (n << 13) ^ n;
		return (float) (1.0-((n*(n*n*15731+789221)+1376312589)&0x7fffffff)/1073741824.0);
	}
	static float stretchedNoise2(float x_float, float y_float, float stretch) {
		// stretch
		x_float /= stretch;
		y_float /= stretch;
		// the whole part of the coordinates
		int x = (int) Math.floor(x_float);
		int y = (int) Math.floor(y_float);
		// the decimal part - how far between the two points yours is
		float fractional_X = x_float - x;
		float fractional_Y = y_float - y;
		// we need to grab the 4x4 nearest points to do cubic interpolation
		double[] p = new double[4];
		for (int j = 0; j < 4; j++) {
			double[] p2 = new double[4];
			for (int i = 0; i < 4; i++) {
				p2[i] = noise2(x + i - 1, y + j - 1);
			}
			// interpolate each row
			p[j] = cubicInterp(p2, fractional_X);
		}
		// and interpolate the results each row's interpolation
		return (float) cubicInterp(p, fractional_Y);
	}
	public static double cubicInterp(double[] p, double x) {
		return cubicInterp(p[0],p[1],p[2],p[3], x);
	}
	public static double cubicInterp(double v0, double v1, double v2, double v3, double x) {
		double P = (v3 - v2) - (v0 - v1);
		double Q = (v0 - v1) - P;
		double R = v2 - v0;
		double S = v1;
		return P * x * x * x + Q * x * x + R * x + S;
	}
	public static double perlin2(float xx, float yy, double nn) {
		double noise = 0;
		noise += stretchedNoise2(xx, yy,  5) * 1; // sample 1
		noise += stretchedNoise2(xx, yy, 13) * 2; // twice as influential
		noise += stretchedNoise2(xx, yy, 21) * 1;
		noise += stretchedNoise2(xx, yy, 55) * 3;

		// you can keep repeating different variants of the above lines
		// some interesting variants are included below.

		return noise / (1+2+1+3); // make sure you sum the multipliers above
	}

}

class ImprovedNoise {
	// implementation originale de Ken Perlin
	// JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.
	static public double noise(double x, double y, double z) {
		int X = (int)Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
				Y = (int)Math.floor(y) & 255,                  // CONTAINS POINT.
				Z = (int)Math.floor(z) & 255;
		x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
		y -= Math.floor(y);                                // OF POINT IN CUBE.
		z -= Math.floor(z);
		double u = fade(x),                                // COMPUTE FADE CURVES
				v = fade(y),                                // FOR EACH OF X,Y,Z.
				w = fade(z);
		int A = p[X  ]+Y, AA = p[A]+Z, AB = p[A+1]+Z,      // HASH COORDINATES OF
				B = p[X+1]+Y, BA = p[B]+Z, BB = p[B+1]+Z;      // THE 8 CUBE CORNERS,

		return lerp(w, lerp(v, lerp(u, grad(p[AA  ], x  , y  , z   ),  // AND ADD
								grad(p[BA  ], x-1, y  , z   )), // BLENDED
						lerp(u, grad(p[AB  ], x  , y-1, z   ),  // RESULTS
								grad(p[BB  ], x-1, y-1, z   ))),// FROM  8
				lerp(v, lerp(u, grad(p[AA+1], x  , y  , z-1 ),  // CORNERS
								grad(p[BA+1], x-1, y  , z-1 )), // OF CUBE
						lerp(u, grad(p[AB+1], x  , y-1, z-1 ),
								grad(p[BB+1], x-1, y-1, z-1 ))));
	}
	static double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
	static double lerp(double t, double a, double b) { return a + t * (b - a); }
	static double grad(int hash, double x, double y, double z) {
		int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
		double u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
				v = h<4 ? y : h==12||h==14 ? x : z;
		return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
	}
	static final int p[] = new int[512], permutation[] = { 151,160,137,91,90,15,
			131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
			190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
			88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
			77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
			102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
			135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
			5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
			223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
			129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
			251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
			49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
			138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
	};
	static { for (int i=0; i < 256 ; i++) p[256+i] = p[i] = permutation[i]; }
}

public class PerlinNoiseLandscapeGenerator {

    public static double[][] generatePerlinNoiseLandscape ( int dxView, int dyView, double scaling, double landscapeAltitudeRatio, int perlinLayerCount )
    {
    	double landscape[][] = new double[dxView][dyView];
		ImprovedNoise im = new ImprovedNoise();

    	double fac = Math.random();
    	double fac2 = Math.random();
    	double fac3 = Math.random();

    	// A ECRIRE ! 
    	// ...
    	for ( int x = 0 ; x < dxView ; x++ )
    		for ( int y = 0 ; y < dyView ; y++ )
    			landscape[x][y] = SimplerNoise.perlin2(x, y, 0.1);
				//landscape[x][y] = ImprovedNoise.noise(x, y, 0);
				//landscape[x][y] = generateHeight(fac, 10, (float)x / 20.0) - generateHeight(fac2, 20, (float)y / 20.0)
                //     + generateHeight(fac, 10, (float)y / 20.0) - generateHeight(fac3, 2, (float)x / 40.0);

		for ( int x = 0 ; x < dxView ; x++) {
			for (int y = 0; y < dyView; y++) {
				landscape[x][y] += 0.0;
				System.out.print(landscape[x][y] + " ");
			}
			System.out.println();
		}

		// scaling and polishing
    	landscape = LandscapeToolbox.scaleAndCenter(landscape, scaling, landscapeAltitudeRatio);
    	landscape = LandscapeToolbox.smoothLandscape(landscape);
    	
		return landscape;
    }

    static double generateHeight(double a, double b, double x) {
        return Math.sin(a * x) * b;
    }
}
