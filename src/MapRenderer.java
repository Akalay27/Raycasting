import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.StrictMath.cos;

public class MapRenderer {
    Map map;
    DirectDraw screen;
    private Player player;
    private double POV = Math.PI/4;
    private int castLimit;

    BufferedImage[] textures = new BufferedImage[1];
    int textureSize = 200;
    public MapRenderer (Map m, Player p) {
        map = m;
        player = p;
        screen = new DirectDraw(Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT);
        castLimit = m.getSize()*2;

        loadTextures(new String[]{"minecraft.png"});
    }

    private void loadTextures (String[] filenames) {
        for (int f = 0; f < filenames.length; f++) {
            try {
                File pathToFile = new File("minecraft.png");
                BufferedImage tex = ImageIO.read(pathToFile);
                textures[f] = tex;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void renderMap() {

        double angleStep = POV/Game.SCREEN_WIDTH;

        //make sure to bound angle in Player Class
        for (int w = 0; w < Game.SCREEN_WIDTH; w++) {
            double rayAngle = boundAngle(player.angle-POV/2+angleStep*w);
            Map.HitPoint hitPoint = map.castRay(player.x,player.y,rayAngle,castLimit);
            double deltaX = player.x-hitPoint.point[0];
            double deltaY = player.y-hitPoint.point[1];
            double deltaAngle = rayAngle-player.angle;

            double distance = cos(deltaAngle)*(Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2)));

            double rectHeight = (1/distance+0.0000000000001)*Game.SCREEN_HEIGHT; //TODO: don't do it this way it is so bad, return negative or positive infinity

            Color rectColor = new Color(0,0,(int)(255-constrain(distance,1,12)*255/12));
            //Color rectColor = new Color(0,0,255);
            //screen.drawRect(w,(int)((double)(Game.SCREEN_HEIGHT/2)-(rectHeight/2.0)),1,(int)rectHeight,rectColor);
            int texAcross = 0;
            if (hitPoint.side == 1) {
                texAcross = (int)(hitPoint.point[1]%1*textureSize);
            } else if (hitPoint.side == 0) {
                texAcross = (int)(hitPoint.point[0]%1*textureSize);
            }
            drawTextureSlice(0,w,texAcross,(int)constrain(rectHeight,0,Game.SCREEN_HEIGHT/3));

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


    private void drawTextureSlice(int texture, int pos,int xTex, int size) {

        int[] slice = new int[size];

        for (int y = 0; y < size; y++) {
            System.out.println(y);
            slice[y] = textures[texture].getRGB(xTex,y*(textureSize/size));
        }

        screen.canvas.setRGB(pos,Game.SCREEN_HEIGHT/2-size/2,1,size,slice,0,size);
    }





}
