/* Game.java
 * Space Invaders Main Program
 * with a scrolling background.
 */
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Game extends Canvas {
    private BufferStrategy strategy;
    private boolean waitingForKeyPress = true;
    private final int GAME_WIDTH = 1920;
    final int GAME_HEIGHT = 1080;

    private boolean gameRunning = true;
    private ArrayList<PipeEntity> pipes = new ArrayList<>();
    private long lastPipeTime = 0;

    private PigEntity pig; // Declare pig here

    public Game() {
        JFrame container = new JFrame("Flappy Pig");

        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        panel.setLayout(null);

        setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
        panel.add(this);

        setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(new KeyInputHandler());

        requestFocus();

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // Initialize PigEntity
        pig = new PigEntity(this, "images/FlyingPig.png", 100, GAME_HEIGHT / 2);

        gameLoop();
    }

    public void createPipes() {
        int topPipeHeight = 100 + new Random().nextInt(200);
        int gap = 200;
        int bottomPipeY = topPipeHeight + gap;

        PipeEntity topPipe = new PipeEntity(this, "images/Pipe_Top.png", 800, 0, true);
        topPipe.setY(0);
        pipes.add(topPipe);

        PipeEntity bottomPipe = new PipeEntity(this, "images/pipe_bottom.png", 800, bottomPipeY, false);
        pipes.add(bottomPipe);
    }

    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();
        BufferedImage back = null;
        Background backOne = new Background();
        Background backTwo = new Background(backOne.getImageWidth(), 0);

        while (gameRunning) {
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

            if (back == null) back = (BufferedImage) createImage(getWidth(), getHeight());
            Graphics buffer = back.createGraphics();

            // Clear buffer
            buffer.clearRect(0, 0, getWidth(), getHeight());

            backOne.draw(buffer);
            backTwo.draw(buffer);

            g.drawImage(back, 0, 0, null);

            // Draw pig
            pig.move(delta);
            pig.draw(g);

            // Create pipes periodically
            if (System.currentTimeMillis() - lastPipeTime > 2000) {
                createPipes();
                lastPipeTime = System.currentTimeMillis();
            }

            // Move and draw pipes
            for (PipeEntity pipe : pipes) {
                pipe.move(delta);
                if (pig.collidesWith(pipe)) {
                    notifyDeath();
                }
            }

            pipes.removeIf(pipe -> pipe.getX() < -50);
            for (PipeEntity pipe : pipes) {
                pipe.draw(g);
            }

            g.dispose();
            strategy.show();
        }
    }

    public void notifyDeath() {
        waitingForKeyPress = true;
    }

    private class KeyInputHandler extends KeyAdapter {
        private int pressCount = 1;

        public void keyPressed(KeyEvent e) {
            if (waitingForKeyPress) {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                pig.jump();
            }
        }
    }

    public static void main(String[] args) {
        new Game();
    }
}