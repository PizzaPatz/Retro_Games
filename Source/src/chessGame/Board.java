package chessGame;

import javafx.scene.control.Alert;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Board extends JPanel implements ActionListener {

    ChessButton[][] chessGrid;
    int activeX=0, activeY=0;
    boolean whiteSide = true; // true = white's turn; false = black's turn
    boolean validMove = false;
    boolean lock = false; // Lock king if it is in checked position
    boolean moved = true;
    JButton button;

    public Board(int x, int y, int size) {

        GridLayout gridBoard = new GridLayout(x, y, x, y);
        this.setLayout(gridBoard);
//        super(new GridLayout(x,y));
//        for (int i = 0; i < x * x; i++) {
//            this.add(new ChessButton(i));
//        }
        this.chessGrid = new ChessButton[6][6];
        initPieces(chessGrid);
        initGame();
        //Assign all king
//        for (int i=0; i < x*x; i++){
//            this.add(new ChessButton(i, "W_King"));
//        }

        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                this.chessGrid[i][j].addActionListener(this);
            }
        }

    }

    public void initPieces(ChessButton[][] chessGrid ){

        chessGrid[0][0] = new ChessButton(0,"B_Rabbit");
        chessGrid[0][1] = new ChessButton(1,"B_Bishop");
        chessGrid[0][2] = new ChessButton(2,"B_Queen");
        chessGrid[0][3] = new ChessButton(3,"B_King");
        chessGrid[0][4] = new ChessButton(4,"B_Bishop");
        chessGrid[0][5] = new ChessButton(5,"B_Rabbit");
        chessGrid[1][0] = new ChessButton(6,"B_Pawn");
        chessGrid[1][1] = new ChessButton(7,"B_Pawn");
        chessGrid[1][2] = new ChessButton(8,"B_Pawn");
        chessGrid[1][3] = new ChessButton(9,"B_Pawn");
        chessGrid[1][4] = new ChessButton(10,"B_Pawn");
        chessGrid[1][5] = new ChessButton(11,"B_Pawn");
        chessGrid[2][0] = new ChessButton(12,"Empty");
        chessGrid[2][1] = new ChessButton(13,"Empty");
        chessGrid[2][2] = new ChessButton(14,"Empty");
        chessGrid[2][3] = new ChessButton(15,"Empty");
        chessGrid[2][4] = new ChessButton(16,"Empty");
        chessGrid[2][5] = new ChessButton(17,"Empty");
        chessGrid[3][0] = new ChessButton(18,"Empty");
        chessGrid[3][1] = new ChessButton(19,"Empty");
        chessGrid[3][2] = new ChessButton(20,"Empty");
        chessGrid[3][3] = new ChessButton(21,"Empty");
        chessGrid[3][4] = new ChessButton(22,"Empty");
        chessGrid[3][5] = new ChessButton(23,"Empty");
        chessGrid[4][0] = new ChessButton(24,"W_Pawn");
        chessGrid[4][1] = new ChessButton(25,"W_Pawn");
        chessGrid[4][2] = new ChessButton(26,"W_Pawn");
        chessGrid[4][3] = new ChessButton(27,"W_Pawn");
        chessGrid[4][4] = new ChessButton(28,"W_Pawn");
        chessGrid[4][5] = new ChessButton(29,"W_Pawn");
        chessGrid[5][0] = new ChessButton(30,"W_Rabbit");
        chessGrid[5][1] = new ChessButton(31,"W_Bishop");
        chessGrid[5][2] = new ChessButton(32,"W_Queen");
        chessGrid[5][3] = new ChessButton(33,"W_King");
        chessGrid[5][4] = new ChessButton(34,"W_Bishop");
        chessGrid[5][5] = new ChessButton(35,"W_Rabbit");


    }

    public void initGame(){
        for (int x = 0; x < chessGrid.length; x++)
        {
            for (int y = 0; y < chessGrid[0].length; y++)
            {
                this.add(chessGrid[x][y]);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e){
        ChessButton tmpChess = new ChessButton();
        boolean found = false;
        int i=0,j=0;
        for(i=0; i < 6 && !found; i++){
            for(j=0; j < 6 && !found; j++){
                if(e.getSource() == chessGrid[i][j]){
                    tmpChess = chessGrid[i][j];
                    found = true;
                }
            }
        }

        //current offset
        int curI = i-1;
        int curJ = j-1;

        //Got the button tile

        if(tmpChess.getDestination() == true){
            moved = false;
            if(chessGrid[curI][curJ].getName().equals("B_King")) {
                playSound("winningSound.wav");
                chessGrid[curI][curJ].setGif();
                ImageIcon icon1 = new ImageIcon("giphy.gif");
                JOptionPane.showMessageDialog(null, "White Side won!", "(Overkill) Checkmate " + "Test2", JOptionPane.INFORMATION_MESSAGE,icon1);

            }
            else if(chessGrid[curI][curJ].getName().equals("W_King")){
                playSound("winningSound.wav");
                JOptionPane.showMessageDialog(null, "Black Side won!", "(Overkill) Checkmate " + "Test2", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(curI==0 && chessGrid[activeY][activeX].getName().equals("W_Pawn")){
                System.out.println("CLICK" + curI + " " + curJ + " " + activeY + " " + activeX + " ");


                Object[] options = {"Bishop", "Rabbit", "Queen", "Pawn (Why tho?)"};
                Object l = JOptionPane.showInputDialog(null, "Just pick something already!",
                        "Mix & Match", JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                if(l.equals("Bishop")){
                    chessGrid[curI][curJ].setImage("W_Bishop");
                    chessGrid[curI][curJ].setName("W_Bishop");
                    chessGrid[activeY][activeX].setImage("Empty");
                    resetColor();
                }else if(l.equals("Rabbit")){
                    chessGrid[curI][curJ].setImage("W_Rabbit");
                    chessGrid[curI][curJ].setName("W_Rabbit");
                    chessGrid[activeY][activeX].setImage("Empty");
                    resetColor();
                }else if(l.equals("Queen")){
                    chessGrid[curI][curJ].setImage("W_Queen");
                    chessGrid[curI][curJ].setName("W_Queen");
                    chessGrid[activeY][activeX].setImage("Empty");
                    resetColor();
                }else if(l.equals("Pawn (Why tho?)")){
                    chessGrid[curI][curJ].setImage("W_Pawn");
                    chessGrid[curI][curJ].setName("W_Pawn");
                    chessGrid[activeY][activeX].setImage("Empty");
                    resetColor();
                }

                moved = true;
            }
            else if(curI==5 && chessGrid[activeY][activeX].getName().equals("B_Pawn")){
                System.out.println("CLICK" + curI + " " + curJ + " " + activeY + " " + activeX + " ");
                Object[] options = {"Bishop", "Rabbit", "Queen", "Pawn (Why tho?)"};
                Object l = JOptionPane.showInputDialog(null, "Just pick something already!",
                        "Mix & Match", JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                if(l.equals("Bishop")){
                    chessGrid[curI][curJ].setImage("B_Bishop");
                    chessGrid[curI][curJ].setName("B_Bishop");
                    chessGrid[activeY][activeX].setImage("Empty");
                    resetColor();
                }else if(l.equals("Rabbit")){
                    chessGrid[curI][curJ].setImage("B_Rabbit");
                    chessGrid[curI][curJ].setName("B_Rabbit");
                    chessGrid[activeY][activeX].setImage("Empty");
                    resetColor();
                }else if(l.equals("Queen")){
                    chessGrid[curI][curJ].setImage("B_Queen");
                    chessGrid[curI][curJ].setName("B_Queen");
                    chessGrid[activeY][activeX].setImage("Empty");
                    resetColor();
                }else if(l.equals("Pawn (Why tho?)")){
                    chessGrid[curI][curJ].setImage("B_Pawn");
                    chessGrid[curI][curJ].setName("B_Pawn");
                    chessGrid[activeY][activeX].setImage("Empty");
                    resetColor();
                }
                moved = true;


            }


            else {
                System.out.println("CLICK" + curI + " " + curJ + " " + activeY + " " + activeX + " ");
                chessGrid[curI][curJ].setImage(chessGrid[activeY][activeX].getName());
                chessGrid[curI][curJ].setName(chessGrid[activeY][activeX].getName());
                chessGrid[activeY][activeX].setImage("Empty");
                resetColor();
                moved = true;
            }
            validMove = false;
            //Checkmates

            if (chessGrid[curI][curJ].getName().equals("W_Pawn")){
                if(checkValidGridW(chessGrid,curI-1,curJ-1) ||
                        checkValidGridB(chessGrid,curI-1, curJ+1)){
                    lock = true;
                    JOptionPane.showMessageDialog(null, "White Checks!", "Check", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (chessGrid[curI][curJ].getName().equals("W_Bishop")){
                if(
                        checkValidGridW(chessGrid,curI-1,curJ-1) ||
                                checkValidGridW(chessGrid,curI-1,curJ+1) ||
                                checkValidGridW(chessGrid,curI-2,curJ-2) ||
                                checkValidGridW(chessGrid,curI-2,curJ+2) ||
                                checkValidGridW(chessGrid,curI-1,curJ+1) ||
                                checkValidGridW(chessGrid,curI+1,curJ-1) ||
                                checkValidGridW(chessGrid,curI-1,curJ-1) ||
                                checkValidGridW(chessGrid,curI+2,curJ-2) ||
                                checkValidGridW(chessGrid,curI+1,curJ+1) ||
                                checkValidGridW(chessGrid,curI+2,curJ+2)
                        ){
                    lock = true;
                    JOptionPane.showMessageDialog(null, "White Checks!", "Check", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (chessGrid[curI][curJ].getName().equals("W_Rabbit")){
                if(
                        checkValidGridW(chessGrid,curI-2, curJ) ||
                                checkValidGridW(chessGrid,curI-1, curJ+1) ||
                                checkValidGridW(chessGrid,curI, curJ+2) ||
                                checkValidGridW(chessGrid,curI+1, curJ+1) ||
                                checkValidGridW(chessGrid,curI+2, curJ) ||
                                checkValidGridW(chessGrid,curI-1, curJ-1) ||
                                checkValidGridW(chessGrid,curI, curJ-2) ||
                                checkValidGridW(chessGrid,curI-1, curJ-1)
                        ){
                    lock = true;
                    JOptionPane.showMessageDialog(null, "White Checks!", "Check", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (chessGrid[curI][curJ].getName().equals("W_Queen")){
                if(
                        checkValidGridW(chessGrid,curI-2, curJ) ||
                                checkValidGridW(chessGrid,curI-1, curJ+1) ||
                                checkValidGridW(chessGrid,curI, curJ+2) ||
                                checkValidGridW(chessGrid,curI+1, curJ+1) ||
                                checkValidGridW(chessGrid,curI+2, curJ) ||
                                checkValidGridW(chessGrid,curI-1, curJ-1) ||
                                checkValidGridW(chessGrid,curI, curJ-2) ||
                                checkValidGridW(chessGrid,curI-1, curJ-1) ||
                                checkValidGridW(chessGrid,curI-1, curJ) ||
                                checkValidGridW(chessGrid,curI+1, curJ) ||
                                checkValidGridW(chessGrid,curI, curJ+1) ||
                                checkValidGridW(chessGrid,curI, curJ-1)
                        ){
                    lock = true;
                    JOptionPane.showMessageDialog(null, "White Checks!", "Check", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            //Black Checkmate
            if (chessGrid[curI][curJ].getName().equals("B_Pawn")){
                if(checkValidGridB(chessGrid,curI+1,curJ-1) ||
                        checkValidGridB(chessGrid,curI+1, curJ+1)){
                    lock = true;
                    JOptionPane.showMessageDialog(null, "Black Checks!", "Check", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (chessGrid[curI][curJ].getName().equals("B_Bishop")){
                if(
                        checkValidGridB(chessGrid,curI-1,curJ-1) ||
                                checkValidGridB(chessGrid,curI-1,curJ+1) ||
                                checkValidGridB(chessGrid,curI-2,curJ-2) ||
                                checkValidGridB(chessGrid,curI-2,curJ+2) ||
                                checkValidGridB(chessGrid,curI-1,curJ+1) ||
                                checkValidGridB(chessGrid,curI+1,curJ-1) ||
                                checkValidGridB(chessGrid,curI-1,curJ-1) ||
                                checkValidGridB(chessGrid,curI+2,curJ-2) ||
                                checkValidGridB(chessGrid,curI+1,curJ+1) ||
                                checkValidGridB(chessGrid,curI+2,curJ+2)
                        ){
                    lock = true;
                    JOptionPane.showMessageDialog(null, "Black Checks!", "Check", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (chessGrid[curI][curJ].getName().equals("B_Rabbit")){
                if(
                        checkValidGridB(chessGrid,curI-2, curJ) ||
                                checkValidGridB(chessGrid,curI-1, curJ+1) ||
                                checkValidGridB(chessGrid,curI, curJ+2) ||
                                checkValidGridB(chessGrid,curI+1, curJ+1) ||
                                checkValidGridB(chessGrid,curI+2, curJ) ||
                                checkValidGridB(chessGrid,curI-1, curJ-1) ||
                                checkValidGridB(chessGrid,curI, curJ-2) ||
                                checkValidGridB(chessGrid,curI-1, curJ-1)
                        ){
                    lock = true;
                    JOptionPane.showMessageDialog(null, "Black Checks!", "Check", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (chessGrid[curI][curJ].getName().equals("B_Queen")){
                if(
                        checkValidGridB(chessGrid,curI-2, curJ) ||
                                checkValidGridB(chessGrid,curI-1, curJ+1) ||
                                checkValidGridB(chessGrid,curI, curJ+2) ||
                                checkValidGridB(chessGrid,curI+1, curJ+1) ||
                                checkValidGridB(chessGrid,curI+2, curJ) ||
                                checkValidGridB(chessGrid,curI-1, curJ-1) ||
                                checkValidGridB(chessGrid,curI, curJ-2) ||
                                checkValidGridB(chessGrid,curI-1, curJ-1) ||
                                checkValidGridB(chessGrid,curI-1, curJ) ||
                                checkValidGridB(chessGrid,curI+1, curJ) ||
                                checkValidGridB(chessGrid,curI, curJ+1) ||
                                checkValidGridB(chessGrid,curI, curJ-1)
                        ){
                    lock = true;
                    JOptionPane.showMessageDialog(null, "Black Checks!", "Check", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        else if(lock && moved){
            if (tmpChess.getName().equals("B_King") && !whiteSide) {
                try {
                    if (chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ - 1].getEmpty()) {
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ].getEmpty()) {
                        chessGrid[curI - 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ + 1].getEmpty()) {
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI][curJ - 1].getEmpty()) {
                        chessGrid[curI][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI][curJ + 1].getEmpty()) {
                        chessGrid[curI][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ - 1].getEmpty()) {
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ].getEmpty()) {
                        chessGrid[curI + 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ + 1].getEmpty()) {
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = true;
                moved = false;
            }


            lock = false;
        }
        else { // Not a target tile
            if(!moved){
                resetColor();
                whiteSide = !whiteSide;
            }
            //========= White Pawn ============// Done
            if (tmpChess.getName().equals("W_Pawn") && whiteSide) {
                //Show target [y-1][x+0]
                try {
                    if (chessGrid[curI - 1][curJ].getEmpty() == true) {
                        chessGrid[curI - 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ - 1].getEmpty() == false &&
                            chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'B') {
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ + 1].getEmpty() == false &&
                            chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'B') {
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = false;
                moved = false;
            }

            //========= White King ============// Done
            if (tmpChess.getName().equals("W_King") && whiteSide) {
                try {
                    if (chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ - 1].getEmpty()) {
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ].getEmpty()) {
                        chessGrid[curI - 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ + 1].getEmpty()) {
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI][curJ - 1].getEmpty()) {
                        chessGrid[curI][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI][curJ + 1].getEmpty()) {
                        chessGrid[curI][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ - 1].getEmpty()) {
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ].getEmpty()) {
                        chessGrid[curI + 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ + 1].getEmpty()) {
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = false;
                moved = false;
            }

            //========= White Bishop ============//
            if (tmpChess.getName().equals("W_Bishop") && whiteSide) {
                //Show target [y-1][x+0]
                try {
                    if (chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ - 1].getEmpty()) {
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ - 1].getEmpty() &&
                            (chessGrid[curI - 2][curJ - 2].getName().charAt(0) == 'B' ||
                                    chessGrid[curI - 2][curJ - 2].getEmpty())) {
                        chessGrid[curI - 2][curJ - 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 2][curJ - 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ + 1].getEmpty()) {
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ + 1].getEmpty() &&
                            (chessGrid[curI - 2][curJ + 2].getName().charAt(0) == 'B' ||
                                    chessGrid[curI - 2][curJ + 2].getEmpty())) {
                        chessGrid[curI - 2][curJ + 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 2][curJ + 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ - 1].getEmpty()) {
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ - 1].getEmpty() &&
                            (chessGrid[curI + 2][curJ - 2].getName().charAt(0) == 'B' ||
                                    chessGrid[curI + 2][curJ - 2].getEmpty())) {
                        chessGrid[curI + 2][curJ - 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 2][curJ - 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ + 1].getEmpty()) {
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ + 1].getEmpty() &&
                            (chessGrid[curI + 2][curJ + 2].getName().charAt(0) == 'B' ||
                                    chessGrid[curI + 2][curJ + 2].getEmpty())) {
                        chessGrid[curI + 2][curJ + 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 2][curJ + 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI + 2][curJ - 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI + 2][curJ - 2].setDestinationOn();
//                }catch(Exception ex){}

                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = false;
                moved = false;
            }

            //========= White Queen ============//
            if (tmpChess.getName().equals("W_Queen") && whiteSide) {
                //Show target [y-1][x+0]
                try {
                    if (chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ - 1].getEmpty()) {
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI - 2][curJ - 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI - 2][curJ - 2].setDestinationOn();
//                }catch(Exception ex){}
                try {
                    if (chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ + 1].getEmpty()) {
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI - 2][curJ + 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI - 2][curJ + 2].setDestinationOn();
//                }catch(Exception ex){}
                try {
                    if (chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ - 1].getEmpty()) {
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI + 2][curJ + 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI + 2][curJ + 2].setDestinationOn();
//                }catch(Exception ex){}
                try {
                    if (chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ + 1].getEmpty()) {
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI + 2][curJ + 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI + 2][curJ + 2].setDestinationOn();
//                }catch(Exception ex){}
//                try {
//                    chessGrid[curI + 2][curJ - 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI + 2][curJ - 2].setDestinationOn();
//                }catch(Exception ex){}


                //---
                try {
                    if (chessGrid[curI - 1][curJ].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ].getEmpty()) {
                        chessGrid[curI - 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ].getEmpty() &&
                            (chessGrid[curI - 2][curJ].getName().charAt(0) == 'B' ||
                                    chessGrid[curI - 2][curJ].getEmpty())) {
                        chessGrid[curI - 2][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 2][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI][curJ + 1].getEmpty()) {
                        chessGrid[curI][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ + 1].getEmpty() &&
                            (chessGrid[curI][curJ + 2].getName().charAt(0) == 'B' ||
                                    chessGrid[curI][curJ + 2].getEmpty())) {
                        chessGrid[curI][curJ + 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ + 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ].getEmpty()) {
                        chessGrid[curI + 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ].getEmpty() &&
                            (chessGrid[curI + 2][curJ].getName().charAt(0) == 'B' ||
                                    chessGrid[curI + 2][curJ].getEmpty())) {
                        chessGrid[curI + 2][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 2][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI][curJ - 1].getEmpty()) {
                        chessGrid[curI][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ - 1].getEmpty() &&
                            (chessGrid[curI][curJ - 2].getName().charAt(0) == 'B' ||
                                    chessGrid[curI][curJ - 2].getEmpty())) {
                        chessGrid[curI][curJ - 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ - 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = false;
                moved = false;
            }

            //======= White Rabbit ======//
            if (tmpChess.getName().equals("W_Rabbit") && whiteSide) {
                try {
                    if (chessGrid[curI][curJ - 2].getName().charAt(0) == 'B' ||
                            chessGrid[curI][curJ - 2].getEmpty()) {
                        chessGrid[curI][curJ - 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ - 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ + 2].getName().charAt(0) == 'B' ||
                            chessGrid[curI][curJ + 2].getEmpty()) {
                        chessGrid[curI][curJ + 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ + 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 2][curJ].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 2][curJ].getEmpty()) {
                        chessGrid[curI - 2][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 2][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 2][curJ].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 2][curJ].getEmpty())
                        chessGrid[curI + 2][curJ].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI + 2][curJ].setDestinationOn();
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ + 1].getEmpty())
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI + 1][curJ + 1].setDestinationOn();
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ - 1].getEmpty())
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI - 1][curJ - 1].setDestinationOn();
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI + 1][curJ - 1].getEmpty())
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI + 1][curJ - 1].setDestinationOn();
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'B' ||
                            chessGrid[curI - 1][curJ + 1].getEmpty())
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI - 1][curJ + 1].setDestinationOn();
                } catch (Exception ex) {
                }


                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = false;
                moved = false;
            }


            // BLACK SIDE

            //========= Black Pawn ============//
            if (tmpChess.getName().equals("B_Pawn") && !whiteSide) {
                //Show target [y+1][x+0]
                try {
                    if (chessGrid[curI + 1][curJ].getEmpty() == true) {
                        chessGrid[curI + 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ].setDestinationOn();
                    }
                }catch(Exception ex){}
                try {
                    if (chessGrid[curI + 1][curJ - 1].getEmpty() == false &&
                            chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'W') {
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ - 1].setDestinationOn();
                    }
                }catch(Exception ex){}
                try {
                    if (chessGrid[curI + 1][curJ + 1].getEmpty() == false &&
                            chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'W') {
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ + 1].setDestinationOn();
                    }
                }catch(Exception ex){}

                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = true;
                moved = false;
            }

            //========= Black King ============// Done
            if (tmpChess.getName().equals("B_King") && !whiteSide) {
                try {
                    if (chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ - 1].getEmpty()) {
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ].getEmpty()) {
                        chessGrid[curI - 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ + 1].getEmpty()) {
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI][curJ - 1].getEmpty()) {
                        chessGrid[curI][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI][curJ + 1].getEmpty()) {
                        chessGrid[curI][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ - 1].getEmpty()) {
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ].getEmpty()) {
                        chessGrid[curI + 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ + 1].getEmpty()) {
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = true;
                moved = false;
            }

            //==========Black Bishop==========//
            if (tmpChess.getName().equals("B_Bishop") && !whiteSide) {
                //Show target [y-1][x+0]
                try {
                    if (chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ - 1].getEmpty()) {
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ - 1].getEmpty() &&
                            (chessGrid[curI - 2][curJ - 2].getName().charAt(0) == 'W' ||
                                    chessGrid[curI - 2][curJ - 2].getEmpty())) {
                        chessGrid[curI - 2][curJ - 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 2][curJ - 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ + 1].getEmpty()) {
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ + 1].getEmpty() &&
                            (chessGrid[curI - 2][curJ + 2].getName().charAt(0) == 'W' ||
                                    chessGrid[curI - 2][curJ + 2].getEmpty())) {
                        chessGrid[curI - 2][curJ + 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 2][curJ + 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ - 1].getEmpty()) {
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ - 1].getEmpty() &&
                            (chessGrid[curI + 2][curJ - 2].getName().charAt(0) == 'W' ||
                                    chessGrid[curI + 2][curJ - 2].getEmpty())) {
                        chessGrid[curI + 2][curJ - 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 2][curJ - 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ + 1].getEmpty()) {
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ + 1].getEmpty() &&
                            (chessGrid[curI + 2][curJ + 2].getName().charAt(0) == 'W' ||
                                    chessGrid[curI + 2][curJ + 2].getEmpty())) {
                        chessGrid[curI + 2][curJ + 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 2][curJ + 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI + 2][curJ - 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI + 2][curJ - 2].setDestinationOn();
//                }catch(Exception ex){}

                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = true;
                moved = false;
            }

            //========= Black Queen ============//
            if (tmpChess.getName().equals("B_Queen") && !whiteSide) {
                //Show target [y-1][x+0]
                try {
                    if (chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ - 1].getEmpty()) {
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI - 2][curJ - 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI - 2][curJ - 2].setDestinationOn();
//                }catch(Exception ex){}
                try {
                    if (chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ + 1].getEmpty()) {
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI - 2][curJ + 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI - 2][curJ + 2].setDestinationOn();
//                }catch(Exception ex){}
                try {
                    if (chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ - 1].getEmpty()) {
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI + 2][curJ + 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI + 2][curJ + 2].setDestinationOn();
//                }catch(Exception ex){}
                try {
                    if (chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ + 1].getEmpty()) {
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
//                try {
//                    chessGrid[curI + 2][curJ + 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI + 2][curJ + 2].setDestinationOn();
//                }catch(Exception ex){}
//                try {
//                    chessGrid[curI + 2][curJ - 2].setColor(Color.decode("#CD3E47"));
//                    chessGrid[curI + 2][curJ - 2].setDestinationOn();
//                }catch(Exception ex){}


                //---
                try {
                    if (chessGrid[curI - 1][curJ].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ].getEmpty()) {
                        chessGrid[curI - 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 1][curJ].getEmpty() &&
                            (chessGrid[curI - 2][curJ].getName().charAt(0) == 'W' ||
                                    chessGrid[curI - 2][curJ].getEmpty())) {
                        chessGrid[curI - 2][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 2][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI][curJ + 1].getEmpty()) {
                        chessGrid[curI][curJ + 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ + 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ + 1].getEmpty() &&
                            (chessGrid[curI][curJ + 2].getName().charAt(0) == 'W' ||
                                    chessGrid[curI][curJ + 2].getEmpty())) {
                        chessGrid[curI][curJ + 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ + 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ].getEmpty()) {
                        chessGrid[curI + 1][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 1][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 1][curJ].getEmpty() &&
                            (chessGrid[curI + 2][curJ].getName().charAt(0) == 'W' ||
                                    chessGrid[curI + 2][curJ].getEmpty())) {
                        chessGrid[curI + 2][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI + 2][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI][curJ - 1].getEmpty()) {
                        chessGrid[curI][curJ - 1].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ - 1].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ - 1].getEmpty() &&
                            (chessGrid[curI][curJ - 2].getName().charAt(0) == 'W' ||
                                    chessGrid[curI][curJ - 2].getEmpty())) {
                        chessGrid[curI][curJ - 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ - 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }

                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = true;
                moved = false;
            }


            //======= Black Rabbit ======//
            if (tmpChess.getName().equals("B_Rabbit") && !whiteSide) {
                try {
                    if (chessGrid[curI][curJ - 2].getName().charAt(0) == 'W' ||
                            chessGrid[curI][curJ - 2].getEmpty()) {
                        chessGrid[curI][curJ - 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ - 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI][curJ + 2].getName().charAt(0) == 'W' ||
                            chessGrid[curI][curJ + 2].getEmpty()) {
                        chessGrid[curI][curJ + 2].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI][curJ + 2].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI - 2][curJ].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 2][curJ].getEmpty()) {
                        chessGrid[curI - 2][curJ].setColor(Color.decode("#CD3E47"));
                        chessGrid[curI - 2][curJ].setDestinationOn();
                    }
                } catch (Exception ex) {
                }
                try {
                    if (chessGrid[curI + 2][curJ].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 2][curJ].getEmpty())
                        chessGrid[curI + 2][curJ].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI + 2][curJ].setDestinationOn();
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ + 1].getEmpty())
                        chessGrid[curI + 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI + 1][curJ + 1].setDestinationOn();
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ - 1].getEmpty())
                        chessGrid[curI - 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI - 1][curJ - 1].setDestinationOn();
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI + 1][curJ - 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI + 1][curJ - 1].getEmpty())
                        chessGrid[curI + 1][curJ - 1].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI + 1][curJ - 1].setDestinationOn();
                } catch (Exception ex) {
                }

                try {
                    if (chessGrid[curI - 1][curJ + 1].getName().charAt(0) == 'W' ||
                            chessGrid[curI - 1][curJ + 1].getEmpty())
                        chessGrid[curI - 1][curJ + 1].setColor(Color.decode("#CD3E47"));
                    chessGrid[curI - 1][curJ + 1].setDestinationOn();
                } catch (Exception ex) {
                }


                activeY = curI;
                activeX = curJ;
                System.out.println("Active X and Y: " + activeX + " " + activeY);
                whiteSide = true;
                moved = false;
            }

        }



    }

    public void resetColor(){
        for(int i=0; i < 6; i++){
            for(int j=0; j < 6; j++){
                chessGrid[i][j].setOriginalTileColor();
                chessGrid[i][j].setDestinationOff();
            }
        }
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            Board.class.getResourceAsStream(url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public boolean checkValidGridW(ChessButton[][] cg, int x, int y){
        try{
            if(cg[x][y].getName().equals("B_King"))
                return true;
            else return false;
        }catch(Exception ex){return false;}
    }
    public boolean checkValidGridB(ChessButton[][] cg, int x, int y){
        try{
            if(cg[x][y].getName().equals("W_King"))
                return true;
            else return false;
        }catch(Exception ex){return false;}
    }

}