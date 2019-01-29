package racingGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.*;
import java.util.Scanner;

public class RaceCart {

    private ImageView node = new ImageView();

    private double xPos;
    private double yPos;
    private double rotation;
    private boolean isMovingForward = false;
    private boolean isReversing = false;
    private boolean isRotatingLeft = false;
    private boolean isRotatingRight = false;
    private int number;

    private double accelerationTime = 0;

    public boolean disabled = false;

    public Scanner scannerx;
    public FileWriter writerx;
    PrintWriter print_linex;

    public Scanner scannery;
    public FileWriter writery;
    PrintWriter print_liney;

    public Scanner scannerr;

    public FileWriter writerr;
    PrintWriter print_liner;

    private CartMoveDelegate cartMoveDelegate;

    public RaceCart(int cartNumber, double xPos, double yPos) {
        this.xPos = xPos;
        this.xPos = xPos;
        this.yPos = yPos;
        this.number = cartNumber;
        rotation = 180;

        if (number == 0) {
            Image image = new javafx.scene.image.Image("racingGame/RedBat.png");
            node.setImage(image);
        } else if (number == 1) {
            Image image = new javafx.scene.image.Image("racingGame/GreenBat.png");
            node.setImage(image);
        } else if (number == 2) {
            Image image = new javafx.scene.image.Image("racingGame/OrangeBat.png");
            node.setImage(image);
        } else if (number == 3) {
            Image image = new javafx.scene.image.Image("racingGame/PinkBat.png");
            node.setImage(image);
        }


        try {
            scannerx = new Scanner(new File("./recordingx1.txt"));
          //  writerx = new FileWriter("./recordingx1.txt", true);
          //  print_linex = new PrintWriter( writerx );


            scannery = new Scanner( new File("./recordingy1.txt"));
           // writery = new FileWriter("./recordingy1.txt", true);
          //  print_liney = new PrintWriter( writery );

            scannerr = new Scanner( new File("./recordingr1.txt"));
           // writerr = new FileWriter("./recordingr1.txt", true);
         //   print_liner = new PrintWriter( writerr );

        } catch(Exception e) {
            System.out.println(e);
        }

        node.setFitHeight(14);
        node.setFitWidth(33);
        node.setCache(true);
        node.setTranslateX(xPos);
        node.setTranslateY(yPos);
        updateRotation();
    }

    public ImageView getNode() {
        return node;
    }

    private void updateRotation() {
        node.setRotate(rotation);
    }

    public void beginForwardMovement() {
        accelerationTime += 1/60;
        isReversing = false;
        isMovingForward = true;
    }

    public void endForwardMovement() {
        isMovingForward = false;
    }

    public void beginReverseMovement() {
        isMovingForward = false;
        isReversing = true;
    }

    public void endReverseMovement() {
        isReversing = false;
    }

    public void beginRotateLeft() {
        isRotatingRight = false;
        isRotatingLeft = true;
    }

    public void endRotateLeft() {
        isRotatingLeft = false;
    }

    public void beginRotateRight() {
        isRotatingLeft = false;
        isRotatingRight = true;    }

    public void endRotateRight() {
        isRotatingRight = false;
    }

    public void moveIfNeeded() {
        if (disabled) {
            node.setVisible(false);
            return;
        }

        if (number == 2) {
            if (scannerx.hasNextLine()) {
                String l = scannerx.nextLine();
                Double p = Double.parseDouble(l);
                if (p != null) {
                    xPos = p;
                }
            }
            if (scannery.hasNextLine()) {
                String l = scannery.nextLine();
                Double p = Double.parseDouble(l);
                if (p != null) {
                    yPos = p;
                }

            }
            if (scannerr.hasNextLine()) {
                String l = scannerr.nextLine();
                Double p = Double.parseDouble(l);
                if (p != null) {
                    rotation = p;
                }
            }
            this.move();
            this.updateRotation();
            return;
        }
//
//        if (number == 0) {
//            print_linex.println(getxPos());
//            print_liney.println(getyPos());
//            print_liner.println(rotation);
//        }

        if (isMovingForward || accelerationTime > 0) {
            if (isMovingForward) {
                if (accelerationTime < 0) {
                    accelerationTime += 1.0/60.0;
                }
                accelerationTime += 2.0/60.0;
            } else {
                accelerationTime -= 2.0/60.0;
                if (accelerationTime < 0) {
                    accelerationTime = 0;
                }
            }
        }
        if (isReversing || accelerationTime < 0) {
            if (isReversing) {
                if (accelerationTime > 0) {
                    accelerationTime -= 1.0/60.0;
                }
                accelerationTime -= 2.0/60.0;
            } else {
                accelerationTime += 2.0/60.0;
                if (accelerationTime > 0) {
                    accelerationTime = 0;
                }
            }
        }
        this.move();
    }

    public void rotateIfNeeded(){
        if (isRotatingLeft) {
            this.rotateLeft();
        } else if (isRotatingRight) {
            this.rotateRight();
        }
    }

    private void move() {
        double angleInRadian = Math.toRadians(rotation);
        double cos = Math.cos(angleInRadian);
        double sin = Math.sin(angleInRadian);
        double yMove = cos;
        double xMove = sin;

        double newX = xPos;
        double newY = yPos;

        double a = 0;
        if (cartMoveDelegate.isInTrack(getxPos(), getyPos())) {
            a = accelerationTime;
        } else {
            if (isMovingForward) {
                a = 1.0;
                accelerationTime = a;
            } else if (isReversing) {
                a = -1.0;
                accelerationTime = a;
            }
        }


        newX += Math.log(Math.pow(a, 4)+1)*xMove*(a > 0 ? 1 : -1);
        newY -= Math.log(Math.pow(a, 4)+1)*yMove*(a > 0 ? 1 : -1);

//        if (direction == Tank.MoveDirection.forward) {
//            newX += Math.log(Math.pow(accelerationTime, 4)+1)*xMove;
//            newY -= Math.log(Math.pow(accelerationTime, 4)+1)*yMove;
//        } else {
//            newX -= 2.0*xMove;
//            newY += 2.0*yMove;
//        }

        if(cartMoveDelegate.canMoveToPosition(number, newX, newY)){
            xPos = newX;
            yPos = newY;
            updatePosition();
        } else {
            accelerationTime = 0;
        }

    }

    private void updatePosition() {

        node.setTranslateX(xPos);
        node.setTranslateY(yPos);

    }

    private void rotateLeft() {
        if (accelerationTime == 0) {
            return;
        }
        double offset = Math.abs(accelerationTime)/2.0;
        rotation -= 22.5/(Math.max(offset, 1));
        updateRotation();
    }

    private void rotateRight() {
        if (accelerationTime == 0) {
            return;
        }
        double offset = Math.abs(accelerationTime)/2.0;
        rotation += 22.5/(Math.max(offset, 1));
        updateRotation();
    }



    public double getxPos(){
        return this.xPos;
    }

    public double getyPos(){
        return this.yPos;
    }

    public void setCartMoveDelegate(CartMoveDelegate cartMoveDelegate) {
        this.cartMoveDelegate = cartMoveDelegate;
    }
}
