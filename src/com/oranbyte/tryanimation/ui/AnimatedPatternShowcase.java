package com.oranbyte.tryanimation.ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimatedPatternShowcase extends JPanel implements ActionListener {
    private int currentPatternIndex = 0;
    private int currentLine = 0;
    private final Timer timer;
    private final String[][] patterns = {
        generateTrianglePattern(10),
        generatePyramidPattern(10),
        generateDiamondPattern(10),
        generateNumberPyramid(10),
        generateAlphabetPyramid(10)
    };

    public AnimatedPatternShowcase() {
        timer = new Timer(500, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        g.setColor(Color.GREEN);
        g.setFont(new Font("Monospaced", Font.BOLD, 20));

        int startX = 50;
        int startY = 50;
        int lineHeight = 30;

        // Draw the current pattern up to the current line
        for (int i = 0; i <= currentLine; i++) {
            String line = patterns[currentPatternIndex][i];
            g.drawString(line, startX, startY + i * lineHeight);
        }
    }

    private static String[] generateTrianglePattern(int size) {
        String[] pattern = new String[size];
        for (int i = 0; i < size; i++) {
            pattern[i] = "* ".repeat(i + 1);
        }
        return pattern;
    }

    private static String[] generatePyramidPattern(int size) {
        String[] pattern = new String[size];
        for (int i = 0; i < size; i++) {
            String spaces = " ".repeat(size - i - 1);
            String stars = "* ".repeat(i + 1);
            pattern[i] = spaces + stars;
        }
        return pattern;
    }

    private static String[] generateDiamondPattern(int size) {
        String[] pattern = new String[size * 2 - 1];
        for (int i = 0; i < size; i++) {
            String spaces = " ".repeat(size - i - 1);
            String stars = "* ".repeat(i * 2 + 1);
            pattern[i] = spaces + stars;
        }
        for (int i = size; i < size * 2 - 1; i++) {
            String spaces = " ".repeat(i - size + 1);
            String stars = "* ".repeat((size * 2 - 1 - i) * 2 - 1);
            pattern[i] = spaces + stars;
        }
        return pattern;
    }

    private static String[] generateNumberPyramid(int size) {
        String[] pattern = new String[size];
        for (int i = 0; i < size; i++) {
            String spaces = " ".repeat(size - i - 1);
            String numbers = "";
            for (int j = 1; j <= i + 1; j++) {
                numbers += j + " ";
            }
            pattern[i] = spaces + numbers;
        }
        return pattern;
    }

    private static String[] generateAlphabetPyramid(int size) {
        String[] pattern = new String[size];
        for (int i = 0; i < size; i++) {
            String spaces = " ".repeat(size - i - 1);
            String letters = "";
            for (char ch = 'A'; ch < 'A' + i + 1; ch++) {
                letters += ch + " ";
            }
            pattern[i] = spaces + letters;
        }
        return pattern;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentLine < patterns[currentPatternIndex].length - 1) {
            currentLine++;
            repaint();
        } else {
            // Pause after completing a pattern
            timer.stop();
            Timer pauseTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currentPatternIndex = (currentPatternIndex + 1) % patterns.length;
                    currentLine = 0;
                    repaint();
                    timer.start();
                }
            });
            pauseTimer.setRepeats(false);
            pauseTimer.start();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Animated Pattern Showcase");
        AnimatedPatternShowcase patternPanel = new AnimatedPatternShowcase();
        frame.add(patternPanel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
