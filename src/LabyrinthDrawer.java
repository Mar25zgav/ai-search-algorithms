import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LabyrinthDrawer {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    public static int[][] labyrinth;
    public static int speed, finalNode;
    private ArrayList<Integer> endNodes;
    private int startNode;

    public LabyrinthDrawer(String filename) {
        labyrinth = read(filename);

        endNodes = new ArrayList<>();

        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, labyrinth.length);
        StdDraw.setYscale(labyrinth.length, 0);

        speed = 100;

        initializeNodes();
    }

    public void AStar() {
        AStar.search(convertToGraph(labyrinth), startNode, endNodes, getCosts(labyrinth));
        AStar.drawPath(convertToGraph(labyrinth).length);
        AStar.printStats(getCosts(labyrinth));
    }

    public void DFS() {
        DFS.search(convertToGraph(labyrinth), startNode, endNodes, finalNode);
        DFS.printStats();
    }

    public void BFS() {
        BFS.search(convertToGraph(labyrinth), startNode, endNodes, finalNode);
        BFS.drawPath(convertToGraph(labyrinth).length);
        BFS.printStats();
    }

    public void IDDFS() {
        IDDFS.search(convertToGraph(labyrinth), startNode, endNodes, finalNode);
        IDDFS.printStats();
    }

    public void IDAStar() {
        IDAStar idas = new IDAStar();
        idas.find(convertToGraph(labyrinth), startNode, endNodes, getCosts(labyrinth));
        idas.printStats(getCosts(labyrinth));
    }

    private int[][] convertToGraph(int[][] labyrinth) {
        int n = labyrinth.length;
        int[][] graph = new int[n * n][n * n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (labyrinth[i][j] == -1)
                    continue;
                if (i + 1 < n && labyrinth[i + 1][j] != -1)
                    set(graph, n, i, j, i + 1, j, labyrinth[i][j]);
                if (j + 1 < n && labyrinth[i][j + 1] != -1)
                    set(graph, n, i, j, i, j + 1, labyrinth[i][j]);
            }
        }
        return graph;
    }

    private void set(int[][] paths, int cols, int row0, int col0, int row1, int col1, int cost) {
        int index0 = row0 * cols + col0;
        int index1 = row1 * cols + col1;
        if (cost < 0) cost = 1;
        paths[index0][index1] = paths[index1][index0] = cost;
    }

    private void initializeNodes() {
        endNodes.clear();
        int nodeIdx = -1;
        for (int i = 0; i < labyrinth.length; i++) {
            for (int j = 0; j < labyrinth.length; j++) {
                nodeIdx++;
                if (labyrinth[i][j] == -2) startNode = nodeIdx;
                if (labyrinth[i][j] == -3) endNodes.add(nodeIdx);
                if (labyrinth[i][j] == -4) finalNode = nodeIdx;
            }
        }
        endNodes.add(finalNode);
    }

    private int[] getCosts(int[][] maze) {
        int[] cost = new int[maze.length * maze.length];
        int x = 0;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] > 0) cost[x] = maze[i][j];
                x++;
            }
        }
        return cost;
    }

    public static Color getColor(int x) {
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
