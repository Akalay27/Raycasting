import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.tan;

public class Map {
    private int[][] wallGrid;

    int tilesX;
    int tilesY;
    public Map() {
        loadMap("maps/map1.txt");
    }

    private void generateMap () {

        tilesX = 32;
        tilesY = 32;
        wallGrid = new int[tilesX][tilesY];
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

    private void loadMap(String filename) {
        File file = new File(filename);

        try {
            tilesY = 0;
            String w;
            BufferedReader ctLines = new BufferedReader(new FileReader(file));
            boolean firstLn = true;
            while ((w=ctLines.readLine()) != null) {
                if (firstLn) {
                    tilesX = w.length();
                    firstLn = false;
                }
                tilesY++;
            }
            wallGrid = new int[tilesX][tilesY];
            ctLines.close();
            BufferedReader map = new BufferedReader(new FileReader(file));

            String ln;
            int y = 0;
            System.out.println("Tiles " + tilesX + "x" + tilesY);
            while ((ln = map.readLine()) != null) {
                System.out.println(ln);
               for (int x = 0; x < tilesX; x++) {
                   wallGrid[x][y] = Character.getNumericValue(ln.charAt(x));
               }
               y++;

            }



        } catch (IOException ex) {
            ex.printStackTrace();
        }






    }

    public HitPoint castRay(double x, double y, double angle, int castLimit) {

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
                int hitTile = wallGrid[(int)yIntercept[0]-(1-shiftX)][(int)yIntercept[1]];
                if (hitTile > 0) return new HitPoint(yIntercept,1,hitTile,directionX);

                yIntercept[0]+=directionX;
                yIntercept[1]+=yStep;

            }
            while ((directionX == 1 && xIntercept[0] <= yIntercept[0]) || (directionX == -1 && xIntercept[0] >=  yIntercept[0])) {
                int hitTile = wallGrid[(int)xIntercept[0]][(int)xIntercept[1]-(1-shiftY)];
                if (hitTile > 0) return new HitPoint(xIntercept,0,hitTile,directionY);

                xIntercept[0]+=xStep;
                xIntercept[1]+=directionY;

            }

        }

        return new HitPoint(new double[]{0.0},0,0,0);
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

    public void preventCollisions (Player player) {
        double[] pushCenter = {0.5,0.5};
        double pushStep = 0.01;

        if (wallGrid[(int)player.x][(int)player.y] > 0) {
            double[] difference = {player.x%1-pushCenter[0],player.y%1-pushCenter[1]};
            double pushAngle = Math.atan2(difference[1],difference[0]);
            while (wallGrid[(int)player.x][(int)player.y] > 0) {
                player.x+=Math.cos(pushAngle)*pushStep;
                player.y+=Math.sin(pushAngle)*pushStep;
            }
        }
    }

    class HitPoint {
        double[] point;
        int side;
        int texture;
        int shading;
        public HitPoint (double[] pt, int s, int tex, int sh) {
            point = pt;
            side = s;
            texture = tex-1;
            // 0 +y  1 +x  2  -y  3 -x

        }
    }

}
