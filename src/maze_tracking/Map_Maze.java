package maze_tracking;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

public class Map_Maze extends JPanel {

    public static final int CELL_SIZE = 40;
    private BufferedImage img;
    private Player player;
    public final int BLACK = -16777216;
    public final int WHITE = -1;
    public final int GREEN = -5708259;
    public final int RED = -1237980;

    private static final int[] DX = {0, 0, -1, 1}; // maze move to x
    private static final int[] DY = {-1, 1, 0, 0}; // maze move to y
    private static final int WALL = 1;
    public static final int PATH = 0;

    private final int mapSize;
    private final int[][] maze;
    private final Random random;

    public Map_Maze(int mapSize) {
        this.mapSize = mapSize + 1;
        this.maze = new int[this.mapSize][this.mapSize];
        this.random = new Random();
        for (int i = 0; i < this.mapSize; i++) {
            Arrays.fill(maze[i], WALL);
        }
        generateMaze(1, 1);
        ensureExitPath();
    }

    public void generateMaze(int startX, int startY) {
        maze[startX][startY] = PATH;
        dfs(startX, startY);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private void dfs(int x, int y) {
        List<Integer> directions = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(directions, random);

        for (int dir : directions) {
            int nx = x + DX[dir] * 2;
            int ny = y + DY[dir] * 2;

            if (isInBounds(nx, ny) && maze[nx][ny] == WALL) {
                maze[x + DX[dir]][y + DY[dir]] = PATH;
                maze[nx][ny] = PATH;
                dfs(nx, ny);
            }
        }
    }

    private boolean isInBounds(int x, int y) {
        return x > 0 && y > 0 && x < mapSize - 1 && y < mapSize - 1;
    }

    private void ensureExitPath() {
        if (maze[mapSize - 2][mapSize - 2] == WALL) {
            maze[mapSize - 3][mapSize - 2] = PATH;
            maze[mapSize - 2][mapSize - 2] = PATH;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = Math.min(getWidth(), getHeight()) / mapSize;

        // Vẽ mê cung
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if (maze[i][j] == WALL) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        g.setColor(Color.GREEN);
        g.fillRect(cellSize, cellSize, cellSize, cellSize);

        g.setColor(Color.RED);
        g.fillRect((mapSize - 2) * cellSize, (mapSize - 2) * cellSize, cellSize, cellSize);

        if (player != null) {
            List<Point> visitedPath = player.getVisitedPath();
            g.setColor(new Color(128, 0, 128, 128)); 
            for (Point p : visitedPath) {
                g.fillRect(p.y * cellSize, p.x * cellSize, cellSize, cellSize);
            }

            Point currentPosition = player.getCurrentPosition();
            g.setColor(new Color(128, 0, 128)); 
            g.fillRect(currentPosition.y * cellSize, currentPosition.x * cellSize, cellSize, cellSize);
        }
    }

    public int getMapSize() {
        return mapSize;
    }

    public int[][] getMaze() {
        return maze;
    }

}
