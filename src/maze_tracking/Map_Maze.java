package maze_tracking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Map_Maze extends JPanel {
    public static final int CELL_SIZE = 40;
    private BufferedImage img;
    private Player player;
    public final int BLACK = -16777216;
    public final int WHITE = -1;
    public final int GREEN = -5708259;
    public final int RED = -1237980;

    public Map_Maze(String imgPath) {
        try {
            File imgFile = new File(imgPath);
            img = ImageIO.read(imgFile);
            if (img != null) {
                int panelWidth = img.getWidth() * CELL_SIZE;
                int panelHeight = img.getHeight() * CELL_SIZE;
                setPreferredSize(new Dimension(panelWidth, panelHeight));
            } else {
                System.err.println("Image not loaded: Image is null.");
            }
        } catch (IOException e) {
            System.err.println("Image could not be loaded: " + e.getMessage());
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            // Vẽ bản đồ
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    int pixel = img.getRGB(x, y);
                    Color pixelColor = new Color(pixel, true);
                    if (pixelColor.getRGB() == BLACK) {
                        g.setColor(Color.BLACK);
                    } else if (pixelColor.getRGB() == WHITE) {
                        g.setColor(Color.WHITE);
                    } else if (pixelColor.getRGB() == GREEN) {
                        g.setColor(Color.GREEN);
                    } else if (pixelColor.getRGB() == RED) {
                        g.setColor(Color.RED);
                    }
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }

            // Vẽ đường đi của Player
            if (player != null) {
                g.setColor(Color.BLUE); // Màu xanh dương cho đường đã đi
                for (Point p : player.getVisitedPath()) {
                    g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }

                // Vẽ Player
                g.setColor(player.getPlayerColor());
                g.fillRect(
                    player.getCurrentPosition().x * CELL_SIZE,
                    player.getCurrentPosition().y * CELL_SIZE,
                    CELL_SIZE,
                    CELL_SIZE
                );
            }
        }
    }

    public BufferedImage getImage() {
        return img;
    }
}
