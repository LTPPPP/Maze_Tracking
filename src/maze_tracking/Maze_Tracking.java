package maze_tracking;

import javax.swing.JFrame;

public class Maze_Tracking {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Tracking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Đường dẫn tới file ảnh
        Map mazeMap = new Map("src/img/map.png");
        frame.add(mazeMap);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
