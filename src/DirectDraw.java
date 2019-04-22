import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class DirectDraw extends JPanel {

    public BufferedImage canvas;

    public DirectDraw (int w, int h) {
        canvas = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        fill(new Color(0,0,0));
    }

    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas,null,null);
    }

    public void fill(Color c) {
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x,y,c.getRGB());
            }
        }

    }

    public void drawRect(int x1, int y1, int w, int h, Color c) {
        for (int x = x1; x < x1+w; x++) {
            for (int y = y1; y < y1+h; y++) {
                if (x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight())
                    canvas.setRGB(x,y,c.getRGB());
            }

        }
    }

    public void setRGB(int x, int y, int col) {
        if (x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight())
            canvas.setRGB(x,y,col);
    }




}
