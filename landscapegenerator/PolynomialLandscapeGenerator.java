package landscapegenerator;

public class PolynomialLandscapeGenerator {

    public static double h_00 = 1.0;
    public static double s3_0 = 0;
    public static double s3_1 = 0;
    public static double s3_2 = 2;
    public static double s3_3 = -2.5;

    static double h_10 = (Math.random() - 0.5) * 3.0;
    static double h_11 = (Math.random() - 0.5) * 3.0;
    static double h_01 = (Math.random() - 0.5) * 3.0;

    public static int norm(int a, int b) {
        return (a + b) % b;
    }

    public static double[][] generatePolynomialLandscape ( int dxView, int dyView, double scaling, double landscapeAltitudeRatio, int perlinLayerCount )
    {
        // selon https://arxiv.org/pdf/1610.03525
        // page 7

        double landscape[][] = new double[dxView][dyView];

        double min = 1000;
        double max = -1000;
        double fac3 = Math.random();

        landscape[0][0] = h_00;
        landscape[dxView-1][0] = h_10;;
        landscape[0][dyView-1] = h_01;
        landscape[dxView-1][dyView-1] = h_11;

        // A ECRIRE !
        // ...
        for ( int x = 0 ; x < dxView ; x++) {
            for (int y = 0; y < dyView; y++) {
                landscape[x][y] = generateHeight2(landscape, x, y);
                if (landscape[x][y] < min)
                    min = landscape[x][y];
                if (landscape[x][y] > max)
                    max = landscape[x][y];

                System.out.print(landscape[x][y] + " ");
            }
            System.out.println();
        }

        // scaling and polishing
        landscape = LandscapeToolbox.scaleAndCenter(landscape, scaling, landscapeAltitudeRatio);
        System.out.println("Min: " + min + "\nMax: " + max);
        landscape = LandscapeToolbox.smoothLandscape(landscape);

        for ( int x = 0 ; x < dxView ; x++) {
            for (int y = 0; y < dyView; y++) {
                landscape[x][y] += 0.0;
                System.out.print(landscape[x][y] + " ");
            }
            System.out.println();
        }

        return landscape;
    }

    static double s3(double x) {
        return (s3_3 * Math.pow(x, 3.0)) + (s3_2 * (x * x)) + (s3_1 * x) + s3_0;
    }

    static double generateHeight2(double[][] landscape, int x, int y) {
        double height = h_00;
        double d_x = (double)x / 256.0;
        double d_y = (double)y / 148.0;

        double s3_x = s3(d_x);
        double s3_y = s3(d_y);

        //double h_10 = landscape[norm(x+1, landscape.length)][y];
        //double h_01 = landscape[x][norm(y+1, landscape[0].length)];
        //double h_11 = landscape[norm(x+1, landscape.length)][norm(y+1, landscape[0].length)];


        double a = h_11 + h_00 - h_10 - h_01;
        //double a = -1.0;

        height += s3_x * (h_10 - h_00);
        height += s3_y * (h_01 - h_00);
        height += a * (s3_x * d_y + s3_y * d_x + d_x * d_y);

        return height;
    }

}
