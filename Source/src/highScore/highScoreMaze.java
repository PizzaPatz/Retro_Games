package highScore;

import javafx.scene.control.Label;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.awt.EventQueue;

public class highScoreMaze {
    File scoreFile;
    static String[] score = new String[6];
    public String userName;
    double userScore;

    public highScoreMaze(){
        scoreFile = new File("./maze.txt");
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
            list.setText("HIGH SCORE LIST\n" + score[0] + " " + score[1] +"s\n" + score[2] + " " + score[3] + "s\n"
                    + score[4] + " " + score[5] + "s");
            return list;
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        return list;
    }

    public void changeScore(double change){
        change = Math.floor(change * 10) / 10;
        userScore = change;
        this.userName();
        try {
            for(int i=1; i < score.length; i=i+2){
                double ch = Double.parseDouble(score[i]);
                if(ch > change){
                    if(i == 1){
                        score[i+4] = score[i+2];
                        score[i+3] = score[i+1];
                        score[i+2] = score[i];
                        score[i+1] = score[i-1];
                        score[i] = Double.toString(change);
                        score[i-1] = userName;
                        i = score.length;
                    }
                    if(i == 3){
                        score[i+2] = score[i];
                        score[i+1] = score[i-1];
                        score[i] = Double.toString(change);
                        score[i-1] = userName;
                        i = score.length;
                    }
                    if(i == 5){
                        score[i] = Double.toString(change);
                        score[i-1] = userName;
                        i = score.length;
                    }
                }
            }
            PrintWriter writer = new PrintWriter("./maze.txt");
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
        String win = "You win! Your score was: " + userScore + "s\n" + "Enter your 3 initial user name: ";
        JDialog winMessage = new JDialog();
        winMessage.setAlwaysOnTop(true);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                userName = JOptionPane.showInputDialog(winMessage, win);
                if(userName.length()>3){
                    userName = userName.substring(0,3);
                }
            }
        });
    }
}
