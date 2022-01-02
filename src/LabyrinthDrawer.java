import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class LabyrinthDrawer {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private int[][] labyrinth;
    private boolean drawCost = false;
    private int startNode;
    private ArrayList<Integer> endNodes;
    private int finalNode;
    public int speed;

    public LabyrinthDrawer(String filename) {
        labyrinth = read(filename);

        endNodes = new ArrayList<>();

        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, labyrinth.length);
        StdDraw.setYscale(labyrinth.length, 0);

        speed = 100;
    }

    public void DFS() {
        drawCost = false;
        initializeNodes();
        searchDFS(convertToGraph(labyrinth), startNode, endNodes);
    }

    public void searchDFS(int[][] graph, int startNode, ArrayList<Integer> endNodes) {
        boolean[] marked = new boolean[graph.length];
        int[] from = new int[graph.length];
        int numberOfTreasures = endNodes.size();
        int treasuredFound = 0;

        Stack<Integer> stack = new Stack<>();

        from[startNode] = -1;
        marked[startNode] = true;
        stack.push(startNode);

        //System.out.println("Polagam na sklad vozlisce " + startNode);

        while (!stack.isEmpty()) {
            int curNode = stack.peek();

            draw(marked, curNode);

            if (endNodes.contains(curNode)) {
                int nodeFound = curNode;
                endNodes.remove((Integer) curNode);

                System.out.println("\nResitev DFS v vozliscu " + curNode);
                System.out.print("Pot: " + curNode);

                while (true) {
                    curNode = from[curNode];
                    if (curNode != -1)
                        System.out.print(" <-- " + curNode);
                    else
                        break;
                }

                // If found all endNodes end else continue
                if (endNodes.size() != 0) {
                    from[nodeFound] = -1;
                    marked[nodeFound] = true;
                    stack.push(nodeFound);
                    curNode = stack.peek();
                } else
                    return;
            }

            // najdi neobiskanega naslednjika
            boolean found = false;
            for (int nextNode = 0; nextNode < graph[curNode].length; nextNode++) {
                if (graph[curNode][nextNode] == 1 && !marked[nextNode]) {
                    marked[nextNode] = true;
                    from[nextNode] = curNode;
                    stack.push(nextNode);

                    //System.out.println("Polagam na sklad vozlisce " + nextNode);

                    found = true;
                    break;
                }
            }

            if (!found) {
                stack.pop();
                //System.out.println("Odstranjum s sklada vozlisce " + curNode);
            }
        }
    }

    private int[][] convertToGraph(int[][] labyrinth) {
        int n = labyrinth.length;
        int[][] graph = new int[n * n][n * n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (labyrinth[i][j] == -1)
                    continue;
                if (i + 1 < n && labyrinth[i + 1][j] != -1)
                    set(graph, n, i, j, i + 1, j);
                if (j + 1 < n && labyrinth[i][j + 1] != -1)
                    set(graph, n, i, j, i, j + 1);
            }
        }
        return graph;
    }

    private void set(int[][] paths, int cols, int row0, int col0, int row1, int col1) {
        int index0 = row0 * cols + col0;
        int index1 = row1 * cols + col1;
        paths[index0][index1] = paths[index1][index0] = 1;
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

    private void draw(boolean[] path, int curNode) {
        StdDraw.enableDoubleBuffering();
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

        // Draw path
        StdDraw.setPenColor(Color.ORANGE);
        for (int i = 0; i < path.length; i++) {
            if (path[i]) {
                int curY = i % labyrinth.length;
                int curX = i / labyrinth.length;
                StdDraw.filledSquare(curY + 0.5, curX + 0.5, 0.5);
            }
        }

        // Draw current position
        StdDraw.setPenColor(Color.MAGENTA);
        int curY = curNode % labyrinth.length;
        int curX = curNode / labyrinth.length;
        StdDraw.filledSquare(curY + 0.5, curX + 0.5, 0.5);

        // Draw grid
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        for (int x = 1; x < labyrinth.length; x++) {
            StdDraw.line(x, 0, x, labyrinth.length);
            StdDraw.line(0, x, labyrinth.length, x);
        }

        StdDraw.show();
        StdDraw.pause(speed);
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
