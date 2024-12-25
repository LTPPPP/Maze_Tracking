package maze_tracking;

import java.awt.*;
import javax.swing.*;

public class Maze_Tracking {
    public static final class GameConstants {
        public static final int BOARD_WIDTH = 950;
        public static final int BOARD_HEIGHT = 800;
        public static final int MIN_MAZE_SIZE = 10;
        public static final int MAX_MAZE_SIZE = 200;
    }

    private JSpinner sizeSpinner; // Added as a field

    void showConfigurationWindow() {
        JFrame configFrame = new JFrame("Maze Game Configuration");
        configFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configFrame.setSize(GameConstants.BOARD_WIDTH / 2, GameConstants.BOARD_HEIGHT / 2);
        configFrame.setLocationRelativeTo(null);

        JPanel mainPanel = createMainPanel();
        configFrame.add(mainPanel);
        configFrame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createConfigPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("MAZE TRACKING GAME");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 100));
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createConfigPanel() {
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(BorderFactory.createTitledBorder("Game Settings"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel sizeLabel = new JLabel("Maze Size (from 10 to 200):");
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        SpinnerNumberModel sizeModel = new SpinnerNumberModel(
                GameConstants.MIN_MAZE_SIZE,
                GameConstants.MIN_MAZE_SIZE,
                GameConstants.MAX_MAZE_SIZE,
                1
        );
        sizeSpinner = new JSpinner(sizeModel); // Initialize the sizeSpinner field
        sizeSpinner.setPreferredSize(new Dimension(200, 40));
        sizeSpinner.setFont(new Font("Arial", Font.PLAIN, 32));

        gbc.gridx = 0;
        gbc.gridy = 0;
        configPanel.add(sizeLabel, gbc);
        gbc.gridy = 1;
        configPanel.add(sizeSpinner, gbc);

        return configPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton startButton = createStyledButton("Start Game", new Color(100, 200, 100));
        JButton aboutButton = createStyledButton("About Us", new Color(100, 150, 250));
        JButton quitButton = createStyledButton("Quit", new Color(250, 100, 100));

        startButton.addActionListener(e -> {
            int size = (Integer) sizeSpinner.getValue(); // Use the sizeSpinner field
            ((JFrame) SwingUtilities.getWindowAncestor(buttonPanel)).dispose();
            startGame(size);
        });

        aboutButton.addActionListener(e -> showAboutDialog());
        quitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(aboutButton);
        buttonPanel.add(quitButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void startGame(int size) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Tracking Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Map_Maze mazePanel = new Map_Maze(size);
            Player player = new Player(mazePanel);
            mazePanel.setPlayer(player);

            frame.add(mazePanel);
            frame.setSize(GameConstants.BOARD_WIDTH, GameConstants.BOARD_HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(null,
                "Maze Tracking Game\nVersion 1.0\n\n" +
                        "Developed by LamTanPhat\n" +
                        "A challenging maze navigation game\n" +
                        "Features automatic maze generation\n" +
                        "and auto-solve game functionality.",
                "About Maze Tracking",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Maze_Tracking().showConfigurationWindow();
    }
}
