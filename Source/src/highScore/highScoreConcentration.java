package highScore;

import javafx.scene.control.Label;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class highScoreConcentration{
    File scoreFile;
    static String[] score = new String[6];
    String userName;
    int userScore;

    public highScoreConcentration(){
        scoreFile = new File("C:/Users/Ivan/Desktop/Fall 2018/concentration.txt");
    }

    public Label readScores(){
        Label list = new Label();
        try {
            Scanner read = new Scanner(scoreFile);
            int j = 0;
            while (read.hasNext()) {
                score[j] = read.next();
                j++;
            }
            list.setText("HIGH SCORE LIST\n" + score[0] + " " + score[1] +"\n" + score[2] + " " + score[3] + "\n"
                    + score[4] + " " + score[5]);
            return list;
        }
        catch (FileNotFoundException e){
            System.out.println("Whoops");
        }
        return list;
    }

    public void changeScore(int change){
        userScore = change;
        this.userName();
        try {
            for(int i=1; i < score.length; i=i+2){
                int ch = Integer.parseInt(score[i]);
                if(ch > change){
                    if(i == 1){
                        score[i+4] = score[i+2];
                        score[i+3] = score[i+1];
                        score[i+2] = score[i];
                        score[i+1] = score[i-1];
                        score[i] = Integer.toString(change);
                        score[i-1] = userName;
                        i = score.length;
                    }
                    if(i == 3){
                        score[i+2] = score[i];
                        score[i+1] = score[i-1];
                        score[i] = Integer.toString(change);
                        score[i-1] = userName;
                        i = score.length;
                    }
                    if(i == 5){
                        score[i] = Integer.toString(change);
                        score[i-1] = userName;
                        i = score.length;
                    }
                }
            }
            PrintWriter writer = new PrintWriter("C:/Users/Ivan/Desktop/Fall 2018/concentration.txt");
            writer.println(score[0]);
            writer.println(score[1]);
            writer.println(score[2]);
            writer.println(score[3]);
            writer.println(score[4]);
            writer.println(score[5]);
            writer.close();
        }
        catch (FileNotFoundException e){
            System.out.println("whoopseeee");
        }
    }
    public void userName() {
        String win = "You win! Your score was: " + userScore + "\n" + "Enter your 3 initial user name: ";
        JDialog winMessage = new JDialog();
        winMessage.setAlwaysOnTop(true);
        userName = JOptionPane.showInputDialog(winMessage, win);
        if(userName.length()>3){
            userName = userName.substring(0,3);
        }
    }
}