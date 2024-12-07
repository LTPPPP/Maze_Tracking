package maze_tracking;

import javax.swing.JFrame;

public class Maze_Tracking {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Tracking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Map_Maze mazeMap = new Map_Maze("src/img/map.png");

        Player player = new Player(mazeMap);
        mazeMap.setPlayer(player);
        player.startAutoMove();

        frame.add(mazeMap);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
