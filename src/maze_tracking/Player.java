package maze_tracking;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.swing.Timer;

public class Player {
    private Point currentPosition;
    private BufferedImage maze;
    private Map_Maze mazePanel;
    private static final Color PLAYER_COLOR = new Color(128, 0, 128); 

    // Movement and colors
    private static final int MOVE_DELAY = 100;
    private Timer moveTimer;
    private Queue<Point> path = new LinkedList<>(); 
    private List<Point> visitedPath = new LinkedList<>(); 
    private static final int BLACK = -16777216;
    private static final int WHITE = -1;
    private static final int GREEN = -5708259;
    private static final int RED = -1237980;

    public Player(Map_Maze mazePanel) {
        this.mazePanel = mazePanel;
        this.maze = mazePanel.getImage();
        findStartingPosition();
    }

    private void findStartingPosition() {
        for (int x = 0; x < maze.getWidth(); x++) {
            for (int y = 0; y < maze.getHeight(); y++) {
                if (maze.getRGB(x, y) == GREEN) {
                    currentPosition = new Point(x, y);
                    return;
                }
            }
        }
        throw new IllegalStateException("No starting point (green cell) found in the maze");
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= maze.getWidth() || y < 0 || y >= maze.getHeight()) {
            return false;
        }
        int color = maze.getRGB(x, y);
        return color == WHITE || color == RED;
    }

    public void findShortestPath() {
        Point goal = null;
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parentMap = new HashMap<>();
        boolean[][] visited = new boolean[maze.getWidth()][maze.getHeight()];

        queue.add(currentPosition);
        visited[currentPosition.x][currentPosition.y] = true;

        // BFS to find the shortest path
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (maze.getRGB(current.x, current.y) == RED) {
                goal = current;
                break;
            }

            for (Point dir : new Point[]{new Point(0, -1), new Point(0, 1), new Point(-1, 0), new Point(1, 0)}) {
                int newX = current.x + dir.x;
                int newY = current.y + dir.y;

                if (isValidMove(newX, newY) && !visited[newX][newY]) {
                    queue.add(new Point(newX, newY));
                    visited[newX][newY] = true;
                    parentMap.put(new Point(newX, newY), current);
                }
            }
        }

        if (goal != null) {
            Point current = goal;
            while (current != null) {
                path.add(current);
                current = parentMap.get(current);
            }
            Collections.reverse((List<Point>) path);
        }
    }

    public void startAutoMove() {
        findShortestPath();

        moveTimer = new Timer(MOVE_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!path.isEmpty()) {
                    visitedPath.add(currentPosition); // Lưu điểm hiện tại
                    currentPosition = path.poll();
                    mazePanel.repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                    System.out.println("Congratulations! You've reached the end!");
                }
            }
        });
        moveTimer.start();
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public Color getPlayerColor() {
        return PLAYER_COLOR;
    }

    public List<Point> getVisitedPath() {
        return visitedPath;
    }
}
