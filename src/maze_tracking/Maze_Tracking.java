package maze_tracking;

import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Maze_Tracking {

    public static final int WIDTH_BOARD = 800;
    public static final int HEIGHT_BOARD = 800;

    public void startGame() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MAZE_TRACKIING");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            int size;
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Nhập kích thước mê cung: ");
                size = scanner.nextInt();
            }

            Map_Maze mazePanel = new Map_Maze(size);
            Player player = new Player(mazePanel);
            mazePanel.setPlayer(player);
            player.startAutoMove();

            frame.add(mazePanel);
            frame.setSize(WIDTH_BOARD, HEIGHT_BOARD);

            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        Maze_Tracking main = new Maze_Tracking();
        main.startGame();
    }
}
