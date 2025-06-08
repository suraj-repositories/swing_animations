package com.oranbyte.tryanimation.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

    private BufferedImage backgroundImage;

    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Animation");
        this.setSize(900, 700);
        this.setLocationRelativeTo(null);

        // Load the background image once
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/com/oranbyte/resource/starts.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the content pane to custom panel that draws the background
        this.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Draw image scaled to fill entire panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        });
        
        JPanel panel = new CrackersPanel();
        panel.setOpaque(false); // Make transparent

        // Use absolute positioning:
        this.setLayout(null);

        // Set panel to cover full frame size
        panel.setBounds(0, 0, this.getWidth(), this.getHeight());

        // Add panel to the frame
        this.add(panel);

        // If frame size changes, update panel size accordingly (optional)
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                panel.setSize(getWidth(), getHeight());
            }
        });
        this.setVisible(true);
    }

    public static void main(String[] args) {
        // Run on the Event Dispatch Thread
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
