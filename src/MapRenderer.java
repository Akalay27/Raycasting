import java.awt.Color;

import static java.lang.StrictMath.cos;

public class MapRenderer {
    Map map;
    DirectDraw screen;
    private Player player;
    private double POV = Math.PI/4;
    private int castLimit;
    public MapRenderer (Map m, Player p) {
        map = m;
        player = p;
        screen = new DirectDraw(Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT);
        castLimit = m.getSize()*2;
    }

    public void renderMap() {

        double angleStep = POV/Game.SCREEN_WIDTH;

        //make sure to bound angle in Player Class
        for (int w = 0; w < Game.SCREEN_WIDTH; w++) {
            double rayAngle = boundAngle(player.angle-POV/2+angleStep*w);
            double[] hitPoint = map.castRay(player.x,player.y,rayAngle,castLimit);
            double deltaX = player.x-hitPoint[0];
            double deltaY = player.y-hitPoint[1];
            double deltaAngle = rayAngle-player.angle;

            double distance = cos(deltaAngle)*(Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2)));

            double rectHeight = (1/distance+0.0000000000001)*Game.SCREEN_HEIGHT; //TODO: don't do it this way it is so bad, return negative or positive infinity

            Color rectColor = new Color(0,0,(int)(255-constrain(distance,1,12)*255/12));
            //Color rectColor = new Color(0,0,255);
            screen.drawRect(w,(int)((double)(Game.SCREEN_HEIGHT/2)-(rectHeight/2.0)),1,(int)rectHeight,rectColor);


        }
        screen.repaint();
    }

    public static double boundAngle(double a) {
        if (a < 0) {
            return a+2*Math.PI;
        }
        if (a > 2*Math.PI) {
            return a-2*Math.PI;
        }
        return a;
    }

    public static double constrain (double n, double min, double max) {
        if (n < min) return min;
        if (n > max) return max;
        return n;
    }






}
