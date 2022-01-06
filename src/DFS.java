import java.awt.*;
import java.util.*;

public class DFS {

    private static int maxDepth;
    public static StringBuffer pot = new StringBuffer();
    public static StringBuffer prev = new StringBuffer();

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
                System.out.print("\nPot: ");
                path.add(curNode);
                int depth = 0;

                StringBuffer sb = new StringBuffer();
                sb.append(curNode);

                while (true) {
                    curNode = from[curNode];
                    if (curNode != -1) {
                        sb.append(" <-- " + curNode);
                        path.add(curNode);
                        depth++;
                    } else {
                        break;
                    }
                    if (depth > maxDepth) maxDepth = depth;
                }
                System.out.println(sb.toString());
                if (prev.length() == 0) {
                    pot.append(" <-- " + sb);
                    prev.append(sb);
                } else {
                    pot.insert(0, constructPot(sb));
                    prev = new StringBuffer(sb);
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
                    pot.insert(0, izhodPot(pathToExit));
                    while (!pathToExit.isEmpty()) {
                        curNode = pathToExit.pop();
                        sb.insert(0, " <-- " + curNode);
                        draw(marked, curNode);
                    }

                    System.out.println();

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

    private static String constructPot(StringBuffer sb) {
        StringBuffer rez = new StringBuffer();
        String[] arr1 = sb.toString().split(" <-- ");
        String[] arr2 = prev.toString().split(" <-- ");
        int k = arr1.length - 1;
        int j = arr2.length - 1;
        while (k >= 0 && j >= 0) {
            if (arr1[k].equals(arr2[j])) {
                k--;
                j--;
                continue;
            } else {
                break;
            }
        }
        for (int i = 1; i <= j; i++) {
            rez.insert(0, " <-- " + arr2[i]);
        }
        for (int i = k; i >= 0; i--) { // maybe index prob
            rez.insert(0, " <-- " + arr1[i]);
        }
        return rez.toString();
    }

    static String[] reverse(String a[])
    {
        Collections.reverse(Arrays.asList(a));
        return a;
    }

    private static String izhodPot(Stack s) {
        StringBuffer r = new StringBuffer();

        while (s.size() > 0) {
            r.insert(0, " <-- " + s.pop());
        }
        return r.toString();
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
