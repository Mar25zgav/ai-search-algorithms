public class Main {

    public static void main(String[] args) {

        String labyrinth = "labyrinth_5.txt";

        LabyrinthDrawer lb = new LabyrinthDrawer(labyrinth);

        // Less = faster, default = 100ms
        lb.speed = 50;

        //lb.DFS();
        //lb.BFS();
        //lb.AStar();
        //lb.IDDFS();
        lb.IDAStar();
    }

}
