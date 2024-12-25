package maze_tracking;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.swing.Timer;

public class Player {

    private Point currentPosition;
    private final Point goalPosition;
    private final Map_Maze mazePanel;
    private Queue<Point> path;
    private List<Point> visitedPath;
    private Timer moveTimer;
    private static final int MOVE_DELAY = 10;
    private long startTime;
    private long endTime;

    public Player(Map_Maze mazePanel) {
        this.mazePanel = mazePanel;
        this.currentPosition = new Point(1, 1);
        this.goalPosition = new Point(mazePanel.getMapSize() - 2,
                mazePanel.getMapSize() - 2);
        this.path = new LinkedList<>();
        this.visitedPath = new ArrayList<>();
    }

    public void startAutoMove() {
        findShortestPath();

        moveTimer = new Timer(MOVE_DELAY, e -> {
            if (!path.isEmpty()) {
                moveStep();
            } else {
                finishMove((Timer) e.getSource());
            }
        });

        startTime = System.currentTimeMillis();
        moveTimer.start();
    }

    private void moveStep() {
        visitedPath.add(currentPosition);
        currentPosition = path.poll();
        mazePanel.repaint();
    }

    private void finishMove(Timer timer) {
        timer.stop();
        mazePanel.stopTimer();
        endTime = System.currentTimeMillis();
        System.out.println("Congratulations! You've reached the end!");
        System.out.println("Time: " + (endTime - startTime) / 1000 + " seconds");
    }

    private void findShortestPath() {
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parentMap = new HashMap<>();
        BitSet visited = new BitSet(mazePanel.getMapSize() * mazePanel.getMapSize());

        queue.add(currentPosition);
        setVisited(visited, currentPosition);

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(goalPosition)) {
                reconstructPath(parentMap);
                return;
            }

            exploreNeighbors(current, queue, parentMap, visited);
        }
    }

    private void exploreNeighbors(Point current, Queue<Point> queue,
            Map<Point, Point> parentMap, BitSet visited) {
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        for (int[] dir : directions) {
            int newX = current.x + dir[0];
            int newY = current.y + dir[1];
            Point next = new Point(newX, newY);

            if (isValidMove(newX, newY) && !isVisited(visited, next)) {
                queue.add(next);
                setVisited(visited, next);
                parentMap.put(next, current);
            }
        }
    }

    private void reconstructPath(Map<Point, Point> parentMap) {
        path.clear();
        Point step = goalPosition;
        while (step != null) {
            path.add(step);
            step = parentMap.get(step);
        }
        Collections.reverse((List<Point>) path);
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && y >= 0
                && x < mazePanel.getMapSize()
                && y < mazePanel.getMapSize()
                && mazePanel.getMaze()[x][y] == Map_Maze.PATH;
    }

    private void setVisited(BitSet visited, Point p) {
        visited.set(p.x * mazePanel.getMapSize() + p.y);
    }

    private boolean isVisited(BitSet visited, Point p) {
        return visited.get(p.x * mazePanel.getMapSize() + p.y);
    }

    public void paint(Graphics g, int cellSize) {
        // Draw visited path
        g.setColor(new Color(128, 0, 128, 128));
        for (Point p : visitedPath) {
            g.fillRect(p.y * cellSize, p.x * cellSize, cellSize, cellSize);
        }

        // Draw current position
        g.setColor(new Color(128, 0, 128));
        g.fillRect(currentPosition.y * cellSize,
                currentPosition.x * cellSize,
                cellSize, cellSize);
    }

    public void cleanup() {
        if (moveTimer != null) {
            moveTimer.stop();
            moveTimer = null;
        }
        path.clear();
        visitedPath.clear();
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public long getElapsedTime() {
        return (endTime - startTime) / 1000;
    }
}
