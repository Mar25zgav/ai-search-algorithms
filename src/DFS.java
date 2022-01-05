import java.awt.*;
import java.util.*;

public class DFS {

    private static int maxDepth;

    public static void search(int[][] graph, int startNode, ArrayList<Integer> endNodes, int fn) {
        boolean[] marked = new boolean[graph.length];
        int[] from = new int[graph.length];

        Stack<Integer> stack = new Stack<>();
        Stack<Integer> pathToExit = new Stack<>();
        Map<Integer, Integer> moves = new HashMap<>();
        Set<Integer> path = new HashSet<>();

        from[startNode] = -1;
        marked[startNode] = true;
        stack.push(startNode);

        boolean naselExit = false;

        //System.out.println("Polagam na sklad vozlisce " + startNode);

        while (!stack.isEmpty()) {
            int curNode = stack.peek();

            draw(marked, curNode);

            if (endNodes.contains(curNode)) {
                int nodeFound = curNode;

                if (curNode == fn) {
                    naselExit = true;
                }

                endNodes.remove((Integer) curNode);

                //System.out.println("\nResitev DFS v vozliscu " + curNode);
                System.out.print("\nPot: " + curNode);
                path.add(curNode);
                int depth = 0;

                while (true) {
                    curNode = from[curNode];
                    if (curNode != -1) {
                        System.out.print(" <-- " + curNode);
                        path.add(curNode);
                        depth++;
                    } else {
                        break;
                    }

                    if (depth > maxDepth) maxDepth = depth;
                }

                // If found all endNodes end else continue
                if (endNodes.size() != 0) {
                    //from[nodeFound] = -1;
                    //marked[nodeFound] = true; // pobarvaj ga
                    stack.push(nodeFound);
                    //continue;
                    curNode = stack.peek();
                } else {
                    // go to Exit,
                    // DRAW path to exit
                    //System.out.println("\nPot do izhoda: \n" + pathToExit.toString() + "\n");
                    marked[fn] = false;
                    while (!pathToExit.isEmpty()) {
                        curNode = pathToExit.pop();
                        draw(marked, curNode);
                    }

                    // Calculate number of moves
//                    int move = 0;
//                    for (Integer node : path)
//                        move += moves.get(node);

                    //System.out.println("\nŠtevilo premikov na najdeni poti: " + move);

                    System.out.println(moves);

                    // Draw final path
                    marked = new boolean[graph.length];
                    for (Integer node : path) {
                        marked[node] = true;
                    }
                    draw(marked, fn);

                    return;
                }
            }

            // najdi neobiskanega naslednjika
            boolean found = false;
            for (int nextNode = 0; nextNode < graph[curNode].length; nextNode++) {
                if ((graph[curNode][nextNode] > 0 ||
                        graph[curNode][nextNode] == -2 ||
                        graph[curNode][nextNode] == -3 ||
                        graph[curNode][nextNode] == -4) && !marked[nextNode]) {
                    marked[nextNode] = true; // pobarvaj ga
                    from[nextNode] = curNode;
                    stack.push(nextNode);
                    //System.out.println("Polagam na sklad vozlisce " + nextNode);
                    found = true;
                    break;
                }
            }

            if (naselExit) {
                pathToExit.push(curNode);
            }

            if (!found) {
                stack.pop();
                //System.out.println("Odstranjum s sklada vozlisce " + curNode);
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

    public static void printStats() {
        System.out.println("Največja preiskana globina: " + maxDepth);
    }

}
