import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LabyrinthDrawer {

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private int[][] labyrinth;

    public LabyrinthDrawer(String filename) {
        labyrinth = read(filename);
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
