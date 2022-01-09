import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    public static StringBuffer pot = new StringBuffer();

    public static void search(int[][] graph, int startNode, ArrayList<Integer> endNodes, int fn) {
        boolean[] marked = new boolean[graph.length];
        int[] from = new int[graph.length];

        Queue<Integer> queue = new LinkedList<>();

        marked[startNode] = true;
        from[startNode] = -1;

        queue.add(startNode);
        //System.out.println("Dajem v vrsto vozlisce " + startNode);

        while (!queue.isEmpty()) {
            int curNode = queue.remove();
            draw(marked, curNode);

            // check if found endNode, if endNode is exit, then check if it's the last one
            if (endNodes.contains(curNode) && (curNode != fn || endNodes.size() == 1)) {
                //int nodeFound = curNode;
                endNodes.remove((Integer) curNode);

                //System.out.println("Resitev BFS v vozliscu " + curNode);
                //System.out.print("Pot: " + curNode);

                int pathNode = curNode;
                StringBuffer sb = new StringBuffer(); // remove for duplication
                if (endNodes.isEmpty()) {
                    sb.append(curNode);
                }
                while (true) {

                    pathNode = from[pathNode];
                    if (pathNode != -1)
                        sb.append(" <-- " + pathNode);
                    else
                        break;
                }
                pot.insert(0, sb);
                //System.out.println(sb.toString());
                //System.out.println("\nendNodes Size: " + endNodes.size() + "\n");

                if (endNodes.isEmpty())
                    return;

                // clear arrays and start over
                search(graph, curNode, endNodes, fn);
                return;
            }
            for (int nextNode = 0; nextNode < graph[curNode].length; nextNode++) {
                if ((graph[curNode][nextNode] > 0 ||
                        graph[curNode][nextNode] == -2 ||
                        graph[curNode][nextNode] == -3 ||
                        graph[curNode][nextNode] == -4)
                        && !marked[nextNode]) {
                    marked[nextNode] = true;
                    from[nextNode] = curNode;
                    queue.add(nextNode);

                    //System.out.println("Dajem v vrsto vozlisce " + nextNode);
                }
            }
        }
    }

    private static void draw(boolean[] path, int curNode) {
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

        StdDraw.show();
        StdDraw.pause(LabyrinthDrawer.speed);
    }

    public static void drawPath(int graphLen) {
        String[] arr = pot.toString().split(" <-- ");
        boolean[] marked = new boolean[graphLen];
        for (int i = 0; i < arr.length; i++) {
            int num = Integer.parseInt(arr[i]);
            marked[num] = true;
            draw(marked, num);
        }
    }

    public static void printStats(int[] costs) {
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

    private static String getPosition(int curNode) {
        int curY = curNode % LabyrinthDrawer.labyrinth.length;
        int curX = curNode / LabyrinthDrawer.labyrinth.length;
        return "(" + curX + ", " + curY + ")";
    }
}
