package com.oranbyte.tryanimation.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimatedNameReveal extends JPanel implements ActionListener {
    private final String name = "ORANBYTE";
    private final String url = "www.oranbyte.com";

    private final int[] yPositions = new int[name.length()];
    private final int targetY = 200;
    private final int fallSpeed = 10;
    private final int exitSpeed = 2;
    private final boolean[] arrived = new boolean[name.length()];
    private boolean allArrived = false;
    private boolean urlMovingIn = false;
    private boolean urlHolding = false;
    private boolean urlMovingOut = false;
    private boolean removing = false;

    private Timer animationTimer;

    private int urlX;
    private int urlY;
    private final int urlStartX;
    private final int panelWidthTarget = 800;
    private int urlWidth;

    private int holdTime = 60;
    private int holdCounter = 0;

    public AnimatedNameReveal() {
        animationTimer = new Timer(30, this);
        animationTimer.start();

        for (int i = 0; i < yPositions.length; i++) {
            yPositions[i] = -((i + 1) * 60);
        }

        urlWidth = getFontMetrics(new Font("Verdana", Font.PLAIN, 24)).stringWidth(url);
        urlStartX = -urlWidth - 20;
        urlX = urlStartX;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        g.setColor(Color.ORANGE);
        g.setFont(new Font("Verdana", Font.BOLD, 60));

        int panelWidth = getWidth();
        int letterSpacing = 60;
        int totalWidth = name.length() * letterSpacing;
        int startX = panelWidth / 2 - totalWidth / 2;

        for (int i = 0; i < name.length(); i++) {
            if (yPositions[i] > -100 && yPositions[i] < getHeight() + 100) {
                g.drawString(String.valueOf(name.charAt(i)), startX + i * letterSpacing, yPositions[i]);
            }
        }

        if (allArrived) {
            g.setColor(Color.yellow);
            g.setFont(new Font("Monospaced", Font.ITALIC, 24));
            urlY = targetY + 50;

            urlWidth = g.getFontMetrics().stringWidth(url);
            int centerX = panelWidth / 2 - urlWidth / 2;

            g.drawString(url, urlX, urlY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!removing) {
            if (!allArrived) {
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
                    urlX = urlStartX;

                    Timer delayForURL = new Timer(1000, evt -> urlMovingIn = true);
                    delayForURL.setRepeats(false);
                    delayForURL.start();
                }
            } else if (urlMovingIn) {
                int panelWidth = getWidth();
                int centerX = panelWidth / 2 - urlWidth / 2;

                int dist = centerX - urlX;

                int speed = Math.max(1, dist / 8);

                urlX += speed;

                if (dist <= 3) {
                    urlX = centerX;
                    urlMovingIn = false;
                    urlHolding = true;
                    holdCounter = 0;
                }
                repaint();

            } else if (urlHolding) {
                holdCounter++;
                if (holdCounter >= holdTime) {
                    urlHolding = false;
                    urlMovingOut = true;
                }
                repaint();

            } else if (urlMovingOut) {
                int panelWidth = getWidth();

                int distToEdge = panelWidth - urlX;

                int speed = Math.min(40, Math.max(5, 40 - distToEdge / 5)) + 25;
                urlX += speed;

                if (urlX > panelWidth + urlWidth) {
                    urlMovingOut = false;
                    removing = true;
                }
                repaint();

            } else {
                repaint();
            }
        } else {
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
        JFrame frame = new JFrame("Oranbyte");
        AnimatedNameReveal panel = new AnimatedNameReveal();
        frame.add(panel);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
       

        frame.setIconImage(new ImageIcon(AnimatedNameReveal.class.getResource("/com/oranbyte/resource/selected.png")).getImage());;
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
