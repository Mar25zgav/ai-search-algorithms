public class Main {

    public static void main(String[] args) {

        String labyrinth = "labyrinth_4.txt";

        LabyrinthDrawer lb = new LabyrinthDrawer(labyrinth);

        // Less = faster, default = 100ms
        lb.speed = 0;

        lb.DFS();
        //lb.BFS();
        //lb.AStar();
        //lb.IDAStar();

        //lb.IDDFS();
    }

}
