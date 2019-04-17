import static java.lang.Math.abs;
import static java.lang.Math.tan;

public class Map {
    private int[][] wallGrid;
    public int tilesX;
    public int tilesY;


    public Map(int tileX,int tileY) {
        tilesX = tileX;
        tilesY = tileY;
        wallGrid = new int[tilesX][tilesY];
        generateMap();

    }

    private void generateMap () {
        for (int x = 0; x < tilesX; x++) {
            for (int y = 0; y < tilesY; y++) {
                //Border with random walls inside
                if (x == 0 || x == tilesX-1 || y == 0 || y == tilesY-1 || Math.random() < 0.1) {
                    wallGrid[x][y] = 1;
                } else {
                    wallGrid[x][y] = 0;
                }

            }
        }

    }

    public double[] castRay(double x, double y, double angle, int castLimit) {

        int tileX = (int)x;
        int tileY = (int)y;

        double dx = x-tileX;
        double dy = y-tileY;

        int directionX, directionY;
        int shiftX, shiftY;

        if (angle > Math.PI/2 && angle < 3*Math.PI/2) {
            directionX = -1;
            shiftX = 0;
        } else {
            directionX = 1;
            shiftX = 1;
            dx = 1-dx;
        }

        if (angle > Math.PI && angle < 2*Math.PI) {
            directionY = -1;
            shiftY = 0;
        } else {
            directionY = 1;
            shiftY = 1;
            dy = 1-dy;
        }

        double[] yIntercept = {(double)tileX+shiftX,directionX*tan(angle)*dx+y};
        double[] xIntercept = {directionY*(1/tan(angle))*dy+x,tileY+shiftY};

        double yStep = abs(tan(angle)) * directionY;
        double xStep = abs(1/tan(angle)) * directionX;

        for (int i = 0; i < castLimit; i++) {
            while ((directionY == 1 && yIntercept[1] <= xIntercept[1]) || (directionY == -1 && yIntercept[1] >= xIntercept[1])) {

                if (wallGrid[(int)yIntercept[0]-(1-shiftX)][(int)yIntercept[1]] > 0) return yIntercept;

                yIntercept[0]+=directionX;
                yIntercept[1]+=yStep;

            }
            while ((directionX == 1 && xIntercept[0] <= yIntercept[0]) || (directionX == -1 && xIntercept[0] >=  yIntercept[0])) {

                if (wallGrid[(int)xIntercept[0]][(int)xIntercept[1]-(1-shiftY)] > 0) return xIntercept;

                xIntercept[0]+=xStep;
                xIntercept[1]+=directionY;

            }

        }

        return new double[]{1.0,1.0};
    }
    public String toString() {
        /* implement way to print map to console using ascii white and black blocks or just 1/0. */

        StringBuilder out = new StringBuilder();
        for (int y = 0; y < tilesY; y++) {
            for (int x = 0; x < tilesX; x++) {
                if (wallGrid[x][y] == 1) {
                    out.append("▓");
                } else {
                    out.append("░");
                }
            }
            out.append("\n");
        }
        return out.toString();
    }

    public int getSize() {
        /* used for maximum ray distance */
        return (int)Math.ceil(Math.sqrt(Math.pow(tilesX,2) + Math.pow(tilesY,2)));
    }


}
