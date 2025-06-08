package com.oranbyte.tryanimation.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BigBangAnimation extends JPanel implements ActionListener {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 700;
    private static final int TIMER_DELAY = 20;
    private static final int PARTICLE_COUNT = 400;
    private static final int PARTICLE_LIFE = 100;

    private final ArrayList<Particle> particles = new ArrayList<>();
    private final Timer timer;
    private final Random random = new Random();

    public BigBangAnimation() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        timer = new Timer(TIMER_DELAY, this);
        createBigBang();
        timer.start();
    }

    private void createBigBang() {
        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double angle = 2 * Math.PI * random.nextDouble();
            double speed = 2 + random.nextDouble() * 3;
            double dx = Math.cos(angle) * speed;
            double dy = Math.sin(angle) * speed;

            Color color = Color.getHSBColor(random.nextFloat(), 1.0f, 1.0f);

            particles.add(new Particle(centerX, centerY, dx, dy, color, PARTICLE_LIFE));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        Iterator<Particle> iter = particles.iterator();
        while (iter.hasNext()) {
            Particle p = iter.next();
            if (p.life <= 0) {
                iter.remove();
                continue;
            }
            p.update();
            g2.setColor(new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), p.life * 255 / PARTICLE_LIFE));
            g2.fillOval((int) p.x, (int) p.y, 6, 6);
        }

        if (particles.isEmpty()) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    static class Particle {
        double x, y, dx, dy;
        Color color;
        int life;

        public Particle(double x, double y, double dx, double dy, Color color, int life) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
            this.life = life;
        }

        void update() {
            x += dx;
            y += dy;
            life--;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Big Bang Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new BigBangAnimation());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
