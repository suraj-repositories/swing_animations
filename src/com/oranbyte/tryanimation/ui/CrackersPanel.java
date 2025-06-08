package com.oranbyte.tryanimation.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class CrackersPanel extends JPanel {

    private final List<Firework> fireworks = new ArrayList<>();
    private final Timer timer;
    private final Random rand = new Random();

    public CrackersPanel() {
        setBackground(Color.BLACK);
        


        timer = new Timer(16, e -> {
            if (rand.nextInt(20) == 0) {
                for (int i = 0; i < rand.nextInt(1, 3); i++) {
                    fireworks.add(new Firework(rand.nextInt(getWidth()), getHeight()));
                }
            }

            for (Firework firework : fireworks) {
                firework.update();
            }

            fireworks.removeIf(Firework::isFinished);
            repaint();
        });
        timer.start();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Firework firework : fireworks) {
            firework.draw(g);
        }
    }

    static class Firework {
        private double x, y;
        private final double targetY;
        private boolean exploded = false;
        private final List<Particle> particles = new ArrayList<>();
        private final Color trailColor = Color.WHITE;

        public Firework(int startX, int startY) {
            this.x = startX;
            this.y = startY;
            this.targetY = 150 + new Random().nextInt(150);
        }

        public void update() {
            if (!exploded) {
                y -= 6;
                if (y <= targetY) {
                    explode();
                }
            } else {
                for (Particle p : particles) {
                    p.update();
                }
                particles.removeIf(p -> p.life <= 0);
            }
        }

        private void explode() {
            exploded = true;
            Random rand = new Random();

            int blastSize = rand.nextInt(60, 100);
            for (int i = 0; i < blastSize; i++) {
                double angle = 2 * Math.PI * i / blastSize;
                double speed = 2 + rand.nextDouble() * 2;
                Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                int size = 2 + rand.nextInt(5);
                particles.add(new Particle(x, y, angle, speed, 60, color, size));
            }
        }


        public void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            if (!exploded) {
                int centerX = (int) x;
                int centerY = (int) y;

                RadialGradientPaint starGlow = new RadialGradientPaint(
                    new Point(centerX, centerY),
                    20f,
                    new float[]{0f, 0.3f, 1f},
                    new Color[]{
                        new Color(255, 255, 255, 255),
                        new Color(255, 255, 100, 180),
                        new Color(255, 200, 0, 0)
                    }
                );

                g2d.setPaint(starGlow);
                g2d.fillOval(centerX - 20, centerY - 20, 40, 40);

                g2d.setColor(new Color(255, 255, 150));
                g2d.fillOval(centerX - 3, centerY - 3, 6, 6);

                GradientPaint trail = new GradientPaint(
                    centerX, centerY + 40, new Color(255, 180, 0, 0),
                    centerX, centerY, new Color(255, 255, 200, 180)
                );
                g2d.setPaint(trail);
                g2d.fillRect(centerX - 1, centerY, 2, 40);
            } else {
                for (Particle p : particles) {
                    p.draw(g);
                }
            }

            g2d.dispose();
        }


        public boolean isFinished() {
            return exploded && particles.isEmpty();
        }
    }

    static class Particle {
        double x, y, angle, speed;
        int life, size;
        final Color color;

        public Particle(double x, double y, double angle, double speed, int life, Color color, int size) {
            this.x = x;
            this.y = y;
            this.angle = angle;
            this.speed = speed;
            this.life = life;
            this.color = color;
            this.size = size;
        }

        public void update() {
            x += Math.cos(angle) * speed;
            y += Math.sin(angle) * speed;
            speed *= 0.95;
            life--;
        }

        public void draw(Graphics g) {
            g.setColor(color);
            g.fillOval((int) x, (int) y, size, size);
        }
    }
}
