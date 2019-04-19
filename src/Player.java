import java.util.Scanner;

public class Player {

    double x;
    double y;
    double angle;

    double moveSpeed = 0.1;
    double turnSpeed = 0.05;
    public static final int MOVE_FORWARD = 0;
    public static final int MOVE_BACKWARD = 1;
    public static final int MOVE_LEFT = 2;
    public static final int MOVE_RIGHT = 3;
    public static final int TURN_LEFT = 4;
    public static final int TURN_RIGHT = 5;

    public Player(double xPos,double yPos,double a) {
        x = xPos;
        y = yPos;
        angle = a;

    }

    public void move(int action) {

        switch (action) {
            case MOVE_FORWARD:
                translateDirection(0,moveSpeed);
                break;
            case MOVE_BACKWARD:
                translateDirection(0,-moveSpeed);
                break;
            case MOVE_LEFT:
                translateDirection(-Math.PI/2,moveSpeed);
                break;
            case MOVE_RIGHT:
                translateDirection(Math.PI/2,moveSpeed);
                break;
            case TURN_LEFT:
                angle-=turnSpeed;
                break;
            case TURN_RIGHT:
                angle+=turnSpeed;
                break;
        }
        angle = MapRenderer.boundAngle(angle);
    }

    private void translateDirection(double offset, double magnitude) {
        y += Math.sin(angle+offset)*magnitude;
        x += Math.cos(angle+offset)*magnitude;
    }





}
