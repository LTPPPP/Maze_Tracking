package maze_tracking;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
    private BufferedImage buffer;

    private JButton startButton, quitButton, homeButton, resetButton;
    private Timer timer;
    private JLabel timerLabel;
    private int elapsedTime;
    private boolean gameStarted = false;
    private Player player;

    public Map_Maze(int mapSize) {
        setLayout(null);
        this.mapSize = mapSize + 1;
        this.maze = new int[this.mapSize][this.mapSize];
        this.random = new Random();

        initializeMaze();
        initUI();
    }

    private void initializeMaze() {
        for (int[] row : maze) {
            Arrays.fill(row, WALL);
        }
        generateMaze(1, 1);
        ensureExitPath();
    }

    private void initUI() {
        setupButtons();
        setupTimer();
    }

    private void setupButtons() {
        int buttonWidth = 100;
        int buttonHeight = 40;
        int marginLeft = 800;
        int marginTop = 400;

        startButton = createGameButton("Start Auto", marginLeft, marginTop, e -> startGame());
        quitButton = createGameButton("Quit", marginLeft, marginTop + 60, e -> quitGame());
        homeButton = createGameButton("Home", marginLeft, marginTop + 120, e -> goHome());
        resetButton = createGameButton("Reset", marginLeft, marginTop + 180, e -> resetGame());

        Arrays.asList(startButton, quitButton, homeButton, resetButton)
                .forEach(this::add);
    }

    private JButton createGameButton(String text, int x, int y, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBounds(x, y, 100, 40);
        button.addActionListener(action);
        return button;
    }

    private void setupTimer() {
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setBounds(800, 360, 200, 40);
        add(timerLabel);
    }

    // Game control methods
    private void startGame() {
        if (!gameStarted) {
            gameStarted = true;
            startButton.setEnabled(false);
            if (player != null) {
                startTimer();
                player.startAutoMove();
            }
        }
    }

    private void quitGame() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            window.dispose();
            System.exit(0);
        }
    }

    private void goHome() {
        Window window = SwingUtilities.getWindowAncestor(this);
        window.dispose();
        new Maze_Tracking().showConfigurationWindow();
    }

    private void resetGame() {
        gameStarted = false;
        startButton.setEnabled(true);
        initializeMaze();
        player = new Player(this);
        elapsedTime = 0;
        stopTimer();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (buffer == null || buffer.getWidth() != getWidth()
                || buffer.getHeight() != getHeight()) {
            buffer = new BufferedImage(getWidth(), getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = buffer.createGraphics();
        super.paintComponent(g2d);
        drawMaze(g2d);
        if (player != null) {
            player.paint(g2d, getCellSize());
        }
        g2d.dispose();

        g.drawImage(buffer, 0, 0, null);
    }

    private void drawMaze(Graphics2D g) {
        int cellSize = getCellSize();

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                g.setColor(maze[i][j] == WALL ? Color.BLACK : Color.WHITE);
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        // Draw start and end points
        g.setColor(Color.GREEN);
        g.fillRect(cellSize, cellSize, cellSize, cellSize);

        g.setColor(Color.RED);
        g.fillRect((mapSize - 2) * cellSize, (mapSize - 2) * cellSize,
                cellSize, cellSize);
    }

    private int getCellSize() {
        return Math.min(getWidth(), getHeight()) / mapSize;
    }

    // Getter methods
    public int getMapSize() {
        return mapSize;
    }

    public int[][] getMaze() {
        return maze;
    }

    // Timer methods
    private void startTimer() {
        elapsedTime = 0;
        timer = new Timer(100, e -> updateTimer());
        timer.start();
    }

    private void updateTimer() {
        elapsedTime++;
        int seconds = elapsedTime / 10;
        int tenths = elapsedTime % 10;
        timerLabel.setText(String.format("Time: %02d:%d", seconds, tenths));
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    public void cleanup() {
        stopTimer();
        if (player != null) {
            player.cleanup();
        }
    }

    // Maze generation methods
    private void generateMaze(int startX, int startY) {
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
}
