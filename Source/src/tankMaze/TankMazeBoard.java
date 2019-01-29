package tankMaze;

import javafx.geometry.Point2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class TankMazeBoard {

    final int rows;
    final int columns;

    public enum Tile {
        path, wall;
    }


    // Contains information of the maze
    // Use the Tile enum to represent tiles
    public Tile[][] board;

    public TankMazeBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        board = new Tile[rows][columns];
        setupBoard();
    }

    void setupBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (    (i == 0 || i == columns - 1) ||
                        (j == 0 || j == rows - 1) ||
                        (i == columns/2 || j == rows/2)
                        ) {
                    board[i][j] = Tile.path;
                    // add boarder region and plus
                    continue;
                } else {
                    board[i][j] = Tile.wall;
                }
            }
        }
        int minBounds = 1;
        int maxBounds = (columns/2)-1;
        // upperLeft
        partitionSection(minBounds, maxBounds, minBounds, maxBounds);
        // upperRight
        partitionSection(minBounds, (columns/2)-1, (columns/2)+1, columns-1);
        // lowerLeft
        partitionSection((columns/2)+1, columns-1, minBounds, maxBounds);
        // lowerRight
        partitionSection((columns/2)+1, columns-1, (columns/2)+1, columns-1);

        blowOutCenter();

    }

    private enum CrawlDirection {
       left, right, up, down, ended;

        static CrawlDirection getDirection(int index) {
           switch (index) {
               case 0:
                   return CrawlDirection.left;
               case 1:
                   return CrawlDirection.right;
               case 2:
                   return CrawlDirection.up;
               case 3:
                   return CrawlDirection.down;

                   default:
                       return  CrawlDirection.right;
           }
       }
    }

    void blowOutCenter() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (   i > (columns/2) - 3 &&
                        i < (columns/2) + 3 &&
                        j > (columns/2) - 3 &&
                        j < (columns/2) + 3
                        ) {
                    board[i][j] = Tile.path;
                }
            }
        }
    }

    void partitionSection(int minVertical, int maxVertical, int minHorizontal, int maxHorizontal) {

        Random generator = new Random();

        int entryLeft = Math.abs(generator.nextInt()%((columns/2)-3));
        int entryTop = Math.abs(generator.nextInt()%((columns/2)-3));
        int entryBottom = Math.abs(generator.nextInt()%((columns/2)-3));
        int entryRight= Math.abs(generator.nextInt()%((columns/2)-3));

        entryLeft += minVertical+1;
        entryTop += minHorizontal+1;
        entryBottom += minHorizontal+1;
        entryRight += minVertical+1;

        Point leftPos = new Point(entryLeft, minHorizontal);
        Point topPos = new Point(minVertical, entryTop);
        Point bottomPos = new Point(maxVertical, entryBottom);
        Point rightPos = new Point(entryRight, maxHorizontal);


        board[entryLeft][minHorizontal] = Tile.path;
        board[minVertical][entryTop] = Tile.path;
        board[maxVertical][entryBottom] = Tile.path;
        board[entryRight][maxHorizontal] = Tile.path;

        CrawlDirection leftDirection = CrawlDirection.right;
        CrawlDirection topDirection = CrawlDirection.down;
        CrawlDirection bottomDirection = CrawlDirection.up;
        CrawlDirection rightDirection= CrawlDirection.left;

        crawlMaze(leftDirection, leftPos, minVertical, maxVertical, minHorizontal, maxHorizontal);
        crawlMaze(rightDirection, rightPos, minVertical, maxVertical, minHorizontal, maxHorizontal);
        crawlMaze(topDirection, topPos, minVertical, maxVertical, minHorizontal, maxHorizontal);
        crawlMaze(bottomDirection, bottomPos, minVertical, maxVertical, minHorizontal, maxHorizontal);

    }

    private void crawlMaze(CrawlDirection direction, Point postition, int minVertical, int maxVertical, int minHorizontal, int maxHorizontal) {
        // crawl left
        Tile nextPiece;
        Point pt;
        Random directionGenerator = new Random();
        switch (direction) {
            case up:
                pt = new Point(postition.x-1, postition.y);
                nextPiece = board[pt.x][pt.y];
                if (nextPiece != Tile.path) {
                    board[pt.x][pt.y] = Tile.path;
                    postition = pt;
                }
                break;
            case left:
                pt = new Point(postition.x, postition.y-1);
                nextPiece = board[pt.x][pt.y];
                if (nextPiece != Tile.path) {
                    board[pt.x][pt.y] = Tile.path;
                    postition = pt;
                }
                break;
            case right:
                pt = new Point(postition.x, postition.y+1);
                nextPiece = board[pt.x][pt.y];
                if (nextPiece != Tile.path) {
                    board[pt.x][pt.y] = Tile.path;
                    postition = pt;
                }
                break;
            case down:
                pt = new Point(postition.x+1, postition.y);
                nextPiece = board[pt.x][pt.y];
                if (nextPiece != Tile.path) {
                    board[pt.x][pt.y] = Tile.path;
                    postition = pt;
                }
                break;
        }

        ArrayList<CrawlDirection> validDirections = new ArrayList<CrawlDirection>();
        // check to see if we can move left
        if (    postition.y-1 >= minHorizontal+1 &&
                board[postition.x][postition.y-1] != Tile.path &&
                board[postition.x][postition.y-2] != Tile.path
                ) {
            validDirections.add(CrawlDirection.left);
        }
        // check to see if we can move right
        if (    postition.y+1 <= maxHorizontal-1 &&
                board[postition.x][postition.y+1] != Tile.path &&
                board[postition.x][postition.y+2] != Tile.path
                ) {
            validDirections.add(CrawlDirection.right);
        }
        // check to see if we can move up
        if (    postition.x-1 >= minVertical+1 &&
                board[postition.x-1][postition.y] != Tile.path &&
                board[postition.x-2][postition.y] != Tile.path
                ) {
            validDirections.add(CrawlDirection.up);
        }
        // check to see if we can move down
        if (    postition.x+1 <= maxVertical-1 &&
                board[postition.x+1][postition.y] != Tile.path &&
                board[postition.x+2][postition.y] != Tile.path
                ) {
            validDirections.add(CrawlDirection.down);
        }
        int options = validDirections.size();
        if (options == 0) {
            return;
        }
        CrawlDirection newDirection = validDirections.get(directionGenerator.nextInt(validDirections.size()));
        crawlMaze(newDirection, postition, minVertical, maxVertical, minHorizontal, maxHorizontal);
    }

    public boolean isPointInsideWall(Point2D points[], double tileSize) {
        for (Point2D point: points) {
            int row = (int) (point.getX()/tileSize);
            int column = (int) (point.getY()/tileSize);
            if (row >= rows || column >= columns) {
                return true;
            }
            if (board[column][row] == Tile.wall) {
                return true;
            }
        }
        return  false;
    }

}