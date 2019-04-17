import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputManager extends KeyAdapter {


    private boolean[] heldKeys;

    public InputManager(){

        heldKeys = new boolean[]{false,false,false,false,false,false};
    }
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) heldKeys[Player.MOVE_FORWARD] = true;
        if (keyCode == KeyEvent.VK_A) heldKeys[Player.MOVE_LEFT] = true;
        if (keyCode == KeyEvent.VK_S) heldKeys[Player.MOVE_BACKWARD] = true;
        if (keyCode == KeyEvent.VK_D) heldKeys[Player.MOVE_RIGHT] = true;
        if (keyCode == KeyEvent.VK_LEFT) heldKeys[Player.TURN_LEFT] = true;
        if (keyCode == KeyEvent.VK_RIGHT) heldKeys[Player.TURN_RIGHT] = true;

    }
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) heldKeys[Player.MOVE_FORWARD] = false;
        if (keyCode == KeyEvent.VK_A) heldKeys[Player.MOVE_LEFT] = false;
        if (keyCode == KeyEvent.VK_S) heldKeys[Player.MOVE_BACKWARD] = false;
        if (keyCode == KeyEvent.VK_D) heldKeys[Player.MOVE_RIGHT] = false;
        if (keyCode == KeyEvent.VK_LEFT) heldKeys[Player.TURN_LEFT] = false;
        if (keyCode == KeyEvent.VK_RIGHT) heldKeys[Player.TURN_RIGHT] = false;
    }

    public void movePlayer(Player p) {
        if (heldKeys[Player.MOVE_FORWARD]) p.move(Player.MOVE_FORWARD);
        if (heldKeys[Player.MOVE_LEFT]) p.move(Player.MOVE_LEFT);
        if (heldKeys[Player.MOVE_BACKWARD]) p.move(Player.MOVE_BACKWARD);
        if (heldKeys[Player.MOVE_RIGHT]) p.move(Player.MOVE_RIGHT);
        if (heldKeys[Player.TURN_LEFT]) p.move(Player.TURN_LEFT);
        if (heldKeys[Player.TURN_RIGHT]) p.move(Player.TURN_RIGHT);
    }



}
