package maze_tracking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Maze_Tracking {
    
    public static final int WIDTH_BOARD = 750;
    public static final int HEIGHT_BOARD = 800;
    
    public void showConfigurationWindow() {
        // Create the configuration frame
        JFrame configFrame = new JFrame("Maze Game Configuration");
        configFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configFrame.setSize(WIDTH_BOARD / 2, HEIGHT_BOARD / 2);
        configFrame.setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("MAZE TRACKING GAME");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 100));
        titlePanel.add(titleLabel);
        
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(BorderFactory.createTitledBorder("Game Settings"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel sizeLabel = new JLabel("Maze Size (from 10 to 200):");
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        configPanel.add(sizeLabel, gbc);
        
        SpinnerNumberModel sizeModel = new SpinnerNumberModel(10, 10, 200, 1) {
            @Override
            public void setValue(Object value) {
                if ((Integer) value > 200) {
                    super.setValue(200);
                } else if ((Integer) value < 10) {
                    super.setValue(10);
                } else {
                    super.setValue(value);
                }
            }
        };
        JSpinner sizeSpinner = new JSpinner(sizeModel);
        sizeSpinner.setPreferredSize(new Dimension(200, 40));
        sizeSpinner.setFont(new Font("Arial", Font.PLAIN, 32));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        configPanel.add(sizeSpinner, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Start Game Button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(new Color(100, 200, 100));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = (Integer) sizeSpinner.getValue();
                configFrame.dispose();
                startGame(size);
            }
        });

        // About Us Button
        JButton aboutButton = new JButton("About Us");
        aboutButton.setFont(new Font("Arial", Font.BOLD, 14));
        aboutButton.setBackground(new Color(100, 150, 250));
        aboutButton.setForeground(Color.WHITE);
        aboutButton.setFocusPainted(false);
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
        });

        // Quit Button
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 14));
        quitButton.setBackground(new Color(250, 100, 100));
        quitButton.setForeground(Color.WHITE);
        quitButton.setFocusPainted(false);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add buttons to button panel
        buttonPanel.add(startButton);
        buttonPanel.add(aboutButton);
        buttonPanel.add(quitButton);

        // Assemble main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(configPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        configFrame.add(mainPanel);

        // Make the configuration window visible
        configFrame.setVisible(true);
    }

    // About Us Dialog Method
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(null,
                "Maze Tracking Game\n"
                + "Version 1.0\n\n"
                + "Developed by LamTanPhat\n"
                + "A challenging maze navigation game\n"
                + "Features automatic generate maze\n"
                + "and auto solve maze game.",
                "About Maze Tracking",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void startGame(int size) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MAZE_TRACKING");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
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
        main.showConfigurationWindow();
    }
}
