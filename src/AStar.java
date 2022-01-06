import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class AStar {

    public static StringBuffer pot = new StringBuffer();

    public static void search(int[][] graph, int startNode, ArrayList<Integer> endNodes, int[] hCost, int fn) {
        LinkedList<Integer> open = new LinkedList<>();
        boolean[] closed = new boolean[graph.length];
        int[] from = new int[graph.length];

        int[] gScore = new int[graph.length];
        int[] fScore = new int[graph.length];

        for (int i = 0; i < gScore.length; i++) {
            gScore[i] = Integer.MAX_VALUE;
            fScore[i] = Integer.MAX_VALUE;
        }

        gScore[startNode] = 0;
        fScore[startNode] = hCost[startNode];
        from[startNode] = -1;

        open.add(startNode);
        //System.out.println("Odpiram vozlisce " + startNode);

        while (!open.isEmpty()) {
            int minVal = Integer.MAX_VALUE;
            int minPos = 0;
            int curNode = 0;

            for (int i = 0; i < open.size(); i++) {
                int node = open.get(i);
                if (fScore[node] < minVal) {
                    minVal = fScore[node];
                    minPos = i;
                    curNode = node;
                }
            }

            draw(closed, curNode);

            open.remove(minPos);
            closed[curNode] = true;
            //System.out.println("Zapiram vozlisce " + curNode);

            if (endNodes.contains(curNode) && (curNode != fn || endNodes.size() == 1)) {
                //System.out.println("\nResitev A* v vozliscu " + curNode);
                //System.out.print("Pot: " + curNode);
                int nodeFound = curNode;

                endNodes.remove((Integer) curNode);

                StringBuffer sb = new StringBuffer();

                if (endNodes.isEmpty()) {
                    sb.append(curNode);
                }

                while (true) {
                    curNode = from[curNode];
                    if (curNode != -1) {
                        sb.append(" <-- " + curNode);
                    } else
                        break;
                }

                pot.insert(0, sb);

                if (endNodes.isEmpty()) return;

                search(graph, nodeFound, endNodes, hCost, fn);

                return;
            }

            for (int nextNode = 0; nextNode < graph[curNode].length; nextNode++) {
                if ((graph[curNode][nextNode] > 0) && !closed[nextNode]) {

                    if (!open.contains(nextNode))
                        //System.out.println("Odpiram vozlisce " + nextNode);

                        open.add(nextNode);
                    int dist = gScore[curNode] + graph[curNode][nextNode];

                    if (dist < gScore[nextNode]) {
                        from[nextNode] = curNode;
                        gScore[nextNode] = dist;
                        fScore[nextNode] = gScore[nextNode] + hCost[nextNode];
                        //System.out.println("Posodabljam vozlisce " + nextNode);
                    }
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
        Set<Integer> marked = new HashSet<>();
        for (int i = arr.length - 1; i >= 0; i--) {
            int node = Integer.parseInt(arr[i]);
            System.out.print(getPosition(node) + " ");

            if (!marked.contains(node)) {
                marked.add(node);
                cost += costs[node];
            }
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