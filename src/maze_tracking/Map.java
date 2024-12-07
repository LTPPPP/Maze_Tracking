package maze_tracking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Map extends JPanel {
    public static final int CELL_SIZE = 20;
    private BufferedImage img;

    public Map(String imgPath) {
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    Color pixelColor = new Color(img.getRGB(x, y), true);
                    if (pixelColor.equals(Color.BLACK)) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE); // Default to white for non-black pixels
                    }
                    g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
}
