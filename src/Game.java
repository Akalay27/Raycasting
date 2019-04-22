import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Game extends JFrame {

    static int SCREEN_WIDTH = 900;
    static int SCREEN_HEIGHT = 600;

    private MapRenderer renderer;
    private Player player;
    private Map map;

    private InputManager inputManager = new InputManager();

    public void begin() {
        ActionListener listener = new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                gameLoop();
            }
        };

        Timer timer = new Timer(0,listener);

        timer.start();

    }


    public Game () {


        map = new Map();
        player = new Player(16,16,Math.PI/4);
        renderer = new MapRenderer(map,player);

        addKeyListener(inputManager);
        setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        add(renderer.screen);
        pack();
        setVisible(true);
        
    }

    public static void main (String[] args) {

        Game g = new Game();

        g.begin();

    }

    private void gameLoop(){
        renderer.screen.fill(Color.BLACK);
        renderer.renderMap();
        inputManager.movePlayer(player);

        map.preventCollisions(player);

    }



}
