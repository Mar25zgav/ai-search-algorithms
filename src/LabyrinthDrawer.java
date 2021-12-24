import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LabyrinthDrawer {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private int[][] labyrinth;
    private boolean drawCost = false;

    public LabyrinthDrawer(String filename) {
        labyrinth = read(filename);

        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, labyrinth.length);
        StdDraw.setYscale(labyrinth.length, 0);
        StdDraw.enableDoubleBuffering();
    }

    public void DFS() {
        drawCost = false;
        draw();
    }

    public void AStar() {
        drawCost = true;
        draw();
    }

    private void draw() {
        StdDraw.clear();

        // Draw labyrinth
        for (int y = 0; y < labyrinth.length; y++) {
            for (int x = 0; x < labyrinth.length; x++) {
                StdDraw.setPenColor(getColor(labyrinth[x][y]));
                StdDraw.filledSquare(y + 0.5, x + 0.5, 0.5);

                if (drawCost) {
                    StdDraw.setPenColor(Color.BLACK);
                    if (labyrinth[x][y] > 0)
                        StdDraw.text(y + 0.5, x + 0.5, String.valueOf(labyrinth[x][y]));
                }
            }
        }

        // Draw grid
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        for (int x = 1; x < labyrinth.length; x++) {
            StdDraw.line(x, 0, x, labyrinth.length);
            StdDraw.line(0, x, labyrinth.length, x);
        }

        StdDraw.show();
    }

    private Color getColor(int x) {
        switch (x) {
            case -1:
                // wall
                return StdDraw.GRAY;
            case -2:
                // starting point
                return StdDraw.GREEN;
            case -3:
                // treasue
                return StdDraw.YELLOW;
            case -4:
                // target point
                return StdDraw.RED;
        }
        // hallway
        return StdDraw.WHITE;
    }

    private int[][] read(String filename) {
        int labyrinthWidth = getRowLength(filename);
        int labyrinthHeight = getNumberOfRows(filename);

        int[][] array = new int[labyrinthHeight][labyrinthWidth];

        int rowIndex = 0;
        String[] row;
        try (Scanner sc = new Scanner(new File("resources/" + filename))) {

            while (sc.hasNextLine()) {
                row = sc.nextLine().split(",");
                for (int column = 0; column < labyrinthWidth; column++)
                    array[rowIndex][column] = Integer.parseInt(row[column]);
                rowIndex++;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return array;
    }

    private int getNumberOfRows(String filename) {
        int rows = 0;

        try (Scanner sc = new Scanner(new File("resources/" + filename))) {
            while (sc.hasNextLine()) {
                sc.nextLine();
                rows++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return rows;
    }

    private int getRowLength(String filename) {
        int length = 0;

        try (Scanner sc = new Scanner(new File("resources/" + filename))) {
            length = sc.nextLine().split(",").length;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return length;
    }

}
