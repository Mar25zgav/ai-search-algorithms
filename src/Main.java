public class Main {

    public static void main(String[] args) {

        String labyrinth = "labyrinth_5.txt";

        LabyrinthDrawer lb = new LabyrinthDrawer(labyrinth);

        lb.DFS();
    }

}
