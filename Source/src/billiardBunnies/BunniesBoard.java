package billiardBunnies;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tankMaze.TankMazeBoard;

import java.awt.*;

public class BunniesBoard {

    public  enum Tile {
        Vertical, Horizontal, DiagonalUpLeft, DiagonalUpRight, Empty;

        public static Tile random() {
            int i = (int)(Math.random() * 8);
            return createFromInt(i);
        }

        public static Tile createFromInt(int x) {
            switch (x) {
                case 0:
                    return Tile.Vertical;
                case 1:
                    return Tile.Horizontal;
                case 2:
                    return Tile.DiagonalUpLeft;
                case 3:
                    return Tile.DiagonalUpRight;
                default:
                    return Tile.Empty;
            }
        }

        public ImageView imageView() {
            ImageView v = new ImageView();
            v.setFitHeight(84.0);
            v.setFitWidth(84.0);
            switch (this) {
                case Vertical:
                    v.setImage(new javafx.scene.image.Image("billiardBunnies/Vertical.png"));
                    break;
                case Horizontal:
                    v.setImage(new javafx.scene.image.Image("billiardBunnies/Horizontal.png"));
                    break;
                case DiagonalUpLeft:
                    v.setImage(new javafx.scene.image.Image("billiardBunnies/DiagLeft.png"));
                    break;
                case DiagonalUpRight:
                    v.setImage(new javafx.scene.image.Image("billiardBunnies/DiagRight.png"));
                    break;
                default:
                    break;
            }
            return v;
        }

    }

    public BunniesBoard.Tile[][] board;
    final int  rows = 10;
    final int columns = 10;


    public BunniesBoard() {
        board = new Tile[rows][columns];
        randomizeBoard();
    }

    private void randomizeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (    (i == 0 || i == columns - 1) ||
                        (j == 0 || j == rows - 1) ) {
                    board[i][j] = Tile.Empty;
                    // add boarder region and plus
                    continue;
                } else {

                    board[i][j] = Tile.random();
                }
            }
        }
    }

}
