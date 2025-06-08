package com.oranbyte.tryanimation.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class WalkingCharacterAnimation extends JPanel {

    private BufferedImage spriteSheet;
    private BufferedImage[] frames;
    private int frameIndex = 0;
    private int x = 0;
    private int scaledWidth, scaledHeight;
    private Timer timer;

    public WalkingCharacterAnimation() {
        try {
            spriteSheet = ImageIO.read(new File("C:\\Users\\Shubham\\Downloads\\char.jpg"));
            frames = new BufferedImage[12];

            int frameWidth = spriteSheet.getWidth() / 3;
            int frameHeight = spriteSheet.getHeight() / 4;

            scaledWidth = frameWidth / 2;
            scaledHeight = frameHeight / 2;

            int index = 0;
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 3; col++) {
                    if (index < 12) {
                        BufferedImage original = spriteSheet.getSubimage(
                            col * frameWidth, row * frameHeight,
                            frameWidth, frameHeight
                        );
                        BufferedImage scaled = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2d = scaled.createGraphics();
                        g2d.drawImage(original, 0, 0, scaledWidth, scaledHeight, null);
                        g2d.dispose();
                        frames[index] = scaled;
                        index++;
                    }
                }
            }

            // Animation timer
            timer = new Timer(100, e -> {
                frameIndex = (frameIndex + 1) % frames.length;
                x += 5;
                if (x > getWidth()) {
                    x = -scaledWidth; // wrap around
                }
                repaint();
            });
            timer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (frames[frameIndex] != null) {
            int y = (getHeight() - scaledHeight) / 2;
            g.drawImage(frames[frameIndex], x, y, null);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Walking Character Animation");
        WalkingCharacterAnimation panel = new WalkingCharacterAnimation();
        frame.add(panel);
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setVisible(true);
    }
}
