package com.oranbyte.tryanimation.ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimatedNameReveal extends JPanel implements ActionListener {
    private final String name = "ORANBYTE";
    private final int[] yPositions = new int[name.length()];
    private final int targetY = 200;
    private final int fallSpeed = 10;
    private final int exitSpeed = 8;
    private final boolean[] arrived = new boolean[name.length()];
    private boolean allArrived = false;
    private boolean removing = false;
    private Timer animationTimer;

    public AnimatedNameReveal() {
        animationTimer = new Timer(30, this);
        animationTimer.start();

        for (int i = 0; i < yPositions.length; i++) {
            yPositions[i] = -((i + 1) * 60);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Monospaced", Font.BOLD, 60));

        int panelWidth = getWidth();
        int letterSpacing = 60;
        int totalWidth = name.length() * letterSpacing;
        int startX = panelWidth / 2 - totalWidth / 2;

        for (int i = 0; i < name.length(); i++) {
            if (yPositions[i] > -100 && yPositions[i] < getHeight() + 100) {
                g.drawString(String.valueOf(name.charAt(i)), startX + i * letterSpacing, yPositions[i]);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!removing) {
            boolean currentAllArrived = true;

            for (int i = 0; i < name.length(); i++) {
                if (!arrived[i]) {
                    yPositions[i] += fallSpeed;
                    if (yPositions[i] >= targetY) {
                        yPositions[i] = targetY;
                        arrived[i] = true;
                    } else {
                        currentAllArrived = false;
                    }
                }
            }

            repaint();

            if (currentAllArrived && !allArrived) {
                allArrived = true;

                // Start exit animation after 5 seconds
                Timer delay = new Timer(5000, evt -> removing = true);
                delay.setRepeats(false);
                delay.start();
            }
        } else {
            // Exit animation
            boolean allGone = true;
            for (int i = 0; i < name.length(); i++) {
                if (yPositions[i] < getHeight() + 100) {
                    yPositions[i] += exitSpeed;
                    allGone = false;
                }
            }

            repaint();

            if (allGone) {
                animationTimer.stop();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Animated ORANBYTE Entrance and Exit");
        AnimatedNameReveal panel = new AnimatedNameReveal();
        frame.add(panel);
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
