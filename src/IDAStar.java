import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class IDAStar {

    public StringBuffer pot = new StringBuffer();

    int[][] searchGraph;
    ArrayList<Integer> searchEndNodes;
    int[] searchHeurCost;
    int fn;

    LinkedList<Integer> path;
    boolean found;

    private int search(int gScore, int bound) {
        int curNode = path.get(0);

        draw(path, curNode);

        int fScore = gScore + searchHeurCost[curNode];

        if (fScore > bound) {
            //System.out.println("Vozlisce " + curNode + " je zunaj trenutne meje (razdalja " + fScore + ")");
            return fScore;
        }

        //System.out.println("Vozlisce " + curNode + " je znotraj trenutne meje");

        if (searchEndNodes.contains(curNode) && (curNode != fn || searchEndNodes.size() == 1)) {
            found = true;
            return fScore;
        }

        int min = Integer.MAX_VALUE;

        for (int nextNode = 0; nextNode < searchGraph[curNode].length; nextNode++) {
            if (searchGraph[curNode][nextNode] > 0) {
                if (!(path.contains(nextNode))) {
                    path.add(0, nextNode);
                    int res = search(gScore + searchGraph[curNode][nextNode], bound);

                    if (found)
                        return res;

                    if (res < min)
                        min = res;

                    path.remove(0);
                }
            }
        }

        return min;
    }

    public void find(int[][] graph, int startNode, ArrayList<Integer> endNodes, int[] hCost) {
        searchGraph = graph;
        searchEndNodes = endNodes;
        searchHeurCost = hCost;

        path = new LinkedList<>();
        path.add(startNode);
        found = false;

        int bound = searchHeurCost[startNode];

        while (true) {
            //System.out.println("Meja iskanja je nastavljena na " + bound);

            int res = search(0, bound);

            if (found) {
                //System.out.println("Resitev IDA* je v vozliscu: " + path.get(0));
                //System.out.println("Najdena resitev je na razdalji: " + res);

                int nodeFound = path.getFirst();

                endNodes.remove((Integer) nodeFound);

                //System.out.print("Najdena pot: ");

                StringBuffer sb = new StringBuffer();

                if (endNodes.isEmpty()) {
                    sb.append(nodeFound);
                }

                for (int i = 0; i < path.size(); i++) {
                    //if (i > 0)
                    //sb.append(" <-- ");
                    sb.append(" <-- " + path.get(i));
                }

                pot.insert(0, sb);

                if (endNodes.isEmpty()) return;

                find(graph, nodeFound, endNodes, hCost);

                break;
            }

            if (res == Integer.MAX_VALUE) {
                //System.out.println("Iz zacetnega vozlisca ni mozno priti do nobenega ciljnega vozlisca!");
                break;
            }

            //postavi novo mejo iskanja
            bound = res;
        }
    }

    private void draw(LinkedList<Integer> path, int curNode) {
        int[][] labyrinth = LabyrinthDrawer.labyrinth;

        StdDraw.enableDoubleBuffering();
        StdDraw.clear();

        // Draw labyrinth
        for (int y = 0; y < labyrinth.length; y++) {
            for (int x = 0; x < labyrinth.length; x++) {
                StdDraw.setPenColor(LabyrinthDrawer.getColor(labyrinth[x][y]));
                StdDraw.filledSquare(y + 0.5, x + 0.5, 0.5);
            }
        }

        // Draw path
        StdDraw.setPenColor(Color.ORANGE);
        for (int i = 0; i < path.size(); i++) {
            int node = path.get(i);
            int curY = node % labyrinth.length;
            int curX = node / labyrinth.length;
            StdDraw.filledSquare(curY + 0.5, curX + 0.5, 0.5);
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

        // Draw cost
        // Draw labyrinth
        for (int y = 0; y < labyrinth.length; y++) {
            for (int x = 0; x < labyrinth.length; x++) {
                StdDraw.setPenColor(Color.BLACK);
                if (labyrinth[x][y] > 0)
                    StdDraw.text(y + 0.5, x + 0.5, String.valueOf(labyrinth[x][y]));
            }
        }

        StdDraw.show();
        StdDraw.pause(LabyrinthDrawer.speed);
    }

    private void draw(boolean[] path, int curNode) {
        int[][] labyrinth = LabyrinthDrawer.labyrinth;

        StdDraw.enableDoubleBuffering();
        StdDraw.clear();

        // Draw labyrinth
        for (int y = 0; y < labyrinth.length; y++) {
            for (int x = 0; x < labyrinth.length; x++) {
                StdDraw.setPenColor(LabyrinthDrawer.getColor(labyrinth[x][y]));
                StdDraw.filledSquare(y + 0.5, x + 0.5, 0.5);
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

        // Draw cost
        // Draw labyrinth
        for (int y = 0; y < labyrinth.length; y++) {
            for (int x = 0; x < labyrinth.length; x++) {
                StdDraw.setPenColor(Color.BLACK);
                if (labyrinth[x][y] > 0)
                    StdDraw.text(y + 0.5, x + 0.5, String.valueOf(labyrinth[x][y]));
            }
        }

        StdDraw.show();
        StdDraw.pause(LabyrinthDrawer.speed);
    }

    public void drawPath(int graphLen) {
        String[] arr = pot.toString().split(" <-- ");
        boolean[] marked = new boolean[graphLen];
        for (int i = 0; i < arr.length; i++) {
            int num = Integer.parseInt(arr[i]);
            marked[num] = true;
            draw(marked, num);
        }
    }

    public void printStats(int[] costs) {
        String[] arr = pot.toString().split(" <-- ");
        System.out.println("Pot do cilja:");
        int cost = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            int node = Integer.parseInt(arr[i]);
            System.out.print(getPosition(node) + " ");
            cost += costs[node];
        }

        System.out.println("\nŠtevilo premikov na najdeni poti: " + arr.length);
        System.out.println("Cena najdene poti: " + cost);
    }

    private String getPosition(int curNode) {
        int curY = curNode % LabyrinthDrawer.labyrinth.length;
        int curX = curNode / LabyrinthDrawer.labyrinth.length;
        return "(" + curX + ", " + curY + ")";
    }
}
