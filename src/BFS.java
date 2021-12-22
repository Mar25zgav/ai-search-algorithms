import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    public static void search(int[][] graph, int startNode, ArrayList<Integer> endNodes) {
        boolean[] marked = new boolean[graph.length];
        int[] from = new int[graph.length];

        Queue<Integer> queue = new LinkedList<>();

        marked[startNode] = true;
        from[startNode] = -1;

        queue.add(startNode);
        System.out.println("Dajem v vrsto vozlisce " + startNode);

        while (!queue.isEmpty()) {
            int curNode = queue.remove();
            System.out.println("Odstranjujem iz vrste vozlisce " + curNode);

            if (endNodes.contains(curNode)) {
                System.out.println("Resitev BFS v vozliscu " + curNode);
                System.out.print("Pot: " + curNode);

                while (true) {
                    curNode = from[curNode];
                    if (curNode != -1)
                        System.out.print(" <-- " + curNode);
                    else
                        break;
                }

                return;
            }

            for (int nextNode = 0; nextNode < graph[curNode].length; nextNode++) {
                if (graph[curNode][nextNode] == 1 && !marked[nextNode]) {
                    marked[nextNode] = true;
                    from[nextNode] = curNode;
                    queue.add(nextNode);

                    System.out.println("Dajem v vrsto vozlisce " + nextNode);
                }
            }
        }
    }

    public static void vizualize(String filename) {

    }

    public static void main(String[] args) {
        int[][] graph = {
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 1, 1, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 1, 1},
                {0, 0, 0, 1, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};

        int startNode = 0;
        ArrayList<Integer> endNodes = new ArrayList<>();
        endNodes.add(6);
        endNodes.add(7);

        BFS.search(graph, startNode, endNodes);

    }

}
