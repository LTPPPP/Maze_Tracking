package maze_tracking;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Map_Maze extends JPanel {

    private static final int WALL = 1;
    public static final int PATH = 0;

    private final int mapSize;
    private final int[][] maze;
    private final Random random;

    private JButton startButton;
    private JButton quitButton;
    private JButton homeButton;
    private JButton resetButton;

    private boolean gameStarted = false;
    private Player player;

    private Timer timer;
    private JLabel timerLabel;
    private int elapsedTime;

    public Map_Maze(int mapSize) {
        setLayout(null);

        this.mapSize = mapSize + 1;
        this.maze = new int[this.mapSize][this.mapSize];
        this.random = new Random();
        for (int i = 0; i < this.mapSize; i++) {
            Arrays.fill(maze[i], WALL);
        }
        generateMaze(1, 1);
        ensureExitPath();

        initUI();
    }

    private void initUI() {
        int cellSize = Math.min(getWidth(), getHeight()) / mapSize;
        int mazeWidth = cellSize * mapSize;
        int buttonWidth = 100;
        int buttonHeight = 40;
        int margin_left = 800;
        int margin_top = 400;

        // Nút Start
        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBounds(mazeWidth + margin_left, margin_top, buttonWidth, buttonHeight);
        startButton.addActionListener(e -> {
            if (!gameStarted) {
                gameStarted = true;
                startButton.setEnabled(false);
                if (player != null) {
                    startTimer();
                    player.startAutoMove();
                }
            }
        });

        // Nút Quit
        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 16));
        quitButton.setBounds(mazeWidth + margin_left, margin_top + 60, buttonWidth, buttonHeight);
        quitButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.dispose();
            }
        });

        // Nút Home
        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 16));
        homeButton.setBounds(mazeWidth + margin_left, margin_top + 120, buttonWidth, buttonHeight);
        homeButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.dispose();
            }
            Maze_Tracking main = new Maze_Tracking();
            main.showConfigurationWindow();
        });

        // Nút Reset
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setBounds(mazeWidth + margin_left, margin_top + 180, buttonWidth, buttonHeight);
        resetButton.addActionListener(e -> {
            gameStarted = false;
            startButton.setEnabled(true);

            for (int i = 0; i < mapSize; i++) {
                Arrays.fill(maze[i], WALL);
            }
            generateMaze(1, 1);
            ensureExitPath();

            player = new Player(this);

            repaint();
            elapsedTime = 0;
            stopTimer();
        });
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setBounds(mazeWidth + margin_left, margin_top - 40, buttonWidth * 2, buttonHeight);
        timerLabel.setForeground(Color.BLACK);

        add(timerLabel);
        add(startButton);
        add(quitButton);
        add(homeButton);
        add(resetButton);
    }

    public void generateMaze(int startX, int startY) {
        maze[startX][startY] = PATH;
        dfs(startX, startY);
    }

    private void dfs(int x, int y) {
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        List<Integer> directions = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(directions, random);

        for (int dir : directions) {
            int nx = x + dx[dir] * 2;
            int ny = y + dy[dir] * 2;

            if (isInBounds(nx, ny) && maze[nx][ny] == WALL) {
                maze[x + dx[dir]][y + dy[dir]] = PATH;
                maze[nx][ny] = PATH;
                dfs(nx, ny);
            }
        }
    }

    private boolean isInBounds(int x, int y) {
        return x > 0 && y > 0 && x < mapSize - 1 && y < mapSize - 1;
    }

    private void ensureExitPath() {
        maze[mapSize - 2][mapSize - 2] = PATH;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = Math.min(getWidth(), getHeight()) / mapSize;

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                g.setColor(maze[i][j] == WALL ? Color.BLACK : Color.WHITE);
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        g.setColor(Color.GREEN);
        g.fillRect(cellSize, cellSize, cellSize, cellSize);

        g.setColor(Color.RED);
        g.fillRect((mapSize - 2) * cellSize, (mapSize - 2) * cellSize, cellSize, cellSize);

        if (player != null) {
            player.paint(g, cellSize);
        }
    }

    public int getMapSize() {
        return mapSize;
    }

    public int[][] getMaze() {
        return maze;
    }

    private void startTimer() {
        elapsedTime = 0;
        timer = new Timer(100, e -> {
            elapsedTime++;
            int seconds = elapsedTime / 10;
            int tenths = elapsedTime % 10;
            timerLabel.setText(String.format("Time: %02d:%d", seconds, tenths));
        });
        timer.start();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

}
