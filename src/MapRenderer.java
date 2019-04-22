import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import static java.lang.StrictMath.cos;

public class MapRenderer {
    Map map;
    DirectDraw screen;
    private Player player;
    private double POV = Math.PI/4+0.3;
    private int castLimit;

    BufferedImage[][] textures;
    double textureSize = 400;
    public MapRenderer (Map m, Player p) {
        map = m;
        player = p;
        screen = new DirectDraw(Game.SCREEN_WIDTH,Game.SCREEN_HEIGHT);
        castLimit = m.getSize()*2;

        loadTextures(new String[]{"stonebrick.png","wood.png","diamond.png","iron.png"});
    }

    private void loadTextures (String[] filenames) {
        float[] shading = new float[]{1f,0.75f,0.80f,1f};

        textures = new BufferedImage[filenames.length][4];
        for (int f = 0; f < filenames.length; f++) {
            try {
                File pathToFile = new File("textures/" + filenames[f]);
                BufferedImage tex = ImageIO.read(pathToFile);

                for (int s = 0; s < 4; s++) {
                    BufferedImage shaded = deepCopy(tex);

                    for (int y = 0; y < tex.getHeight(); y++) {
                        for (int x = 0; x < tex.getWidth(); x++) {
                            Color col = new Color(tex.getRGB(x,y));
                            float[] component = new float[3];
                            col.getColorComponents(component);

                            shaded.setRGB(x,y,new Color(component[0]*shading[s],component[1]*shading[s],component[2]*shading[s]).getRGB());
                        }
                    }
                    System.out.println(shading[s]);
                    textures[f][s] = shaded;

                }

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

            drawTextureSlice(hitPoint.texture,w,texAcross,(int)rectHeight,hitPoint.shading);

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


    private void drawTextureSlice(int texture, int pos,int xTex, int size,int shading) {

        int[] slice = new int[(int) constrain(size, 0, Game.SCREEN_HEIGHT)];
        double texRatio = textureSize / size;


        if (size > Game.SCREEN_HEIGHT) { // if the wallHeight is greater than the height of the screen then only use part of the size value.
            int start = size / 2 - Game.SCREEN_HEIGHT / 2;
            for (int y = 0; y < Game.SCREEN_HEIGHT; y++) {
                slice[y] = textures[texture][shading].getRGB(xTex, (int) (texRatio * (start + y)));
            }
        } else {
            for (int y = 0; y < size; y++)
                slice[y] = textures[texture][shading].getRGB(xTex, (int) (texRatio * (y)));
        }
        screen.canvas.setRGB(pos,Game.SCREEN_HEIGHT/2-slice.length/2,1,slice.length,slice,0,1);

//        for (int s = 0; s < slice.length; s++) {
//            screen.setRGB(pos,Game.SCREEN_HEIGHT/2-slice.length/2+s,slice[s]);
//        }

    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }








}
