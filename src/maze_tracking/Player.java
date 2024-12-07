package maze_tracking;

import java.awt.Point;
import java.util.*;

import javax.swing.Timer;

public class Player {

    private Point currentPosition;
    private Point goalPosition;
    private Map_Maze mazePanel;
    private Queue<Point> path = new LinkedList<>();
    private List<Point> visitedPath = new ArrayList<>();
    private static final int MOVE_DELAY = 100;
    private Timer moveTimer;

    public static float startTime ;
    public static float endTime ;
    public Player(Map_Maze mazePanel) {
        this.mazePanel = mazePanel;
        this.currentPosition = new Point(1, 1);
        this.goalPosition = new Point(mazePanel.getMapSize() - 2, mazePanel.getMapSize() - 2);
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || y < 0 || x >= mazePanel.getMapSize() || y >= mazePanel.getMapSize()) {
            return false;
        }
        return mazePanel.getMaze()[x][y] == Map_Maze.PATH;
    }

    public void findShortestPath() {
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parentMap = new HashMap<>();
        boolean[][] visited = new boolean[mazePanel.getMapSize()][mazePanel.getMapSize()];

        queue.add(currentPosition);
        visited[currentPosition.x][currentPosition.y] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(goalPosition)) {
                Point step = goalPosition;
                while (step != null) {
                    path.add(step);
                    step = parentMap.get(step);
                }
                Collections.reverse((List<Point>) path);
                return;
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
    }

    public void startAutoMove() {
        findShortestPath();
        
        moveTimer = new Timer(MOVE_DELAY, e -> {
            if (!path.isEmpty()) {
                visitedPath.add(currentPosition);
                currentPosition = path.poll();
                mazePanel.repaint();
            } else {
                ((Timer) e.getSource()).stop();
                endTime = System.currentTimeMillis()%1000000;
                System.out.println("Congratulations! You've reached the end!");
                System.out.println("Time : "+(endTime-startTime)/1000);
            }
        });
        startTime = System.currentTimeMillis()%1000000;
        moveTimer.start();
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public List<Point> getVisitedPath() {
        return visitedPath;
    }
}
