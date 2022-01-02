public class Main {

    public static void main(String[] args) {

        String labyrinth = "labyrinth_1.txt";

        LabyrinthDrawer lb = new LabyrinthDrawer(labyrinth);

        // Less = faster, default = 100ms
        lb.speed = 50;

        lb.DFS();
        //lb.AStar();
    }

}
