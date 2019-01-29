package chessGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class ChessButton extends JButton {
    static int N = 6;
    Image img;
    String name;
    boolean destination = false;
    Color tileColor;
    Color originalTileColor;
    char side;
    boolean empty;

    public ChessButton(){

    }

    public ChessButton(int i, String name) {
        //super(i / N + "," + i % N);
        try {
            Image img = ImageIO.read(getClass().getResource("chess_pieces/"+name+".png"));
            this.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setOpaque(true);
        this.setBorderPainted(false);
        if ((i / N + i % N) % 2 == 1) { // Color 1
            this.setBackground(Color.decode("#374859"));
            tileColor = Color.decode("#374859");
            originalTileColor = Color.decode("#374859");
        }
        else{ // Color 2
            this.setBackground(Color.decode("#ffffff"));
            tileColor = Color.decode("#ffffff");
            originalTileColor = Color.decode("#ffffff");
        }
        this.setMinimumSize(new Dimension(50,50));
        this.name = name;

        //Empty tile
        if(name.equals("Empty")){
            this.empty = true;
        }
        else{
            this.empty = false;
        }

    }

    public void setImage(String name){
        try {
            Image img = ImageIO.read(getClass().getResource("chess_pieces/"+name+".png"));
            this.setIcon(new ImageIcon(img));
            this.destination = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setOpaque(true);
        this.setBorderPainted(false);

        this.setMinimumSize(new Dimension(50,50));
        this.name = name;
        //Empty tile
        if(name.equals("Empty")){
            this.empty = true;
        }
        else{
            this.empty = false;
        }
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Color getColor(){
        return this.tileColor;
    }
    public void setColor(Color color) { this.setBackground(color); }
    public void setOriginalTileColor(){ this.setBackground(originalTileColor);}
    public void setDestinationOn(){ this.destination = true; }
    public void setDestinationOff() { this.destination = false;}
    public boolean getDestination(){ return this.destination; }
    public boolean getEmpty(){ return this.empty; }

    public void setGif(){
        try {
            Image img = ImageIO.read(getClass().getResource("giphy.gif"));
            this.setIcon(new ImageIcon(img));
            this.destination = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setOpaque(true);
        this.setBorderPainted(false);
        this.setMinimumSize(new Dimension(500,500));
    }
}