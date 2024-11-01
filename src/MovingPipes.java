import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;

public class MovingPipes extends JPanel implements ActionListener, KeyListener {
	private Image image;
    private int birdY = 300, birdVelocity = 0;
    private final int BIRD_X = 100, BIRD_WIDTH = 20, BIRD_HEIGHT = 20;
    private ArrayList<Pipe> pipes;
    private final int PIPE_GAP = 200;
    private Timer timer;
    private boolean gameOver = false;

    private final Image pipeTop = new ImageIcon("images/pipe_top.webp").getImage();
    private final Image pipeBottom = new ImageIcon("images/pipe_bottom.jpg").getImage();

    public MovingPipes() {
        JFrame frame = new JFrame("Flappy Pig");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setVisible(true);

        pipes = new ArrayList<>();
        addPipe();

        timer = new Timer(20, this);
        timer.start();
    }

    private void addPipe() {
        Random rand = new Random();
        int topPipeHeight = 100 + rand.nextInt(200); 
        int bottomPipeY = topPipeHeight + PIPE_GAP;

        pipes.add(new Pipe(800, 0, 50, topPipeHeight, true));
        pipes.add(new Pipe(800, bottomPipeY, 50, 600 - bottomPipeY, false));
    }

    private void movePipes() {
        for (Pipe pipe : pipes) {
            pipe.x -= 5;
        }

        if (pipes.get(0).x + pipes.get(0).width < 0) {
            pipes.remove(0);
            pipes.remove(0);
            addPipe();
        }
    }

    private boolean checkCollision() {
        Rectangle bird = new Rectangle(BIRD_X, birdY, BIRD_WIDTH, BIRD_HEIGHT);
        for (Pipe pipe : pipes) {
            if (pipe.getBounds().intersects(bird)) return true;
        }
        return birdY >= 580 || birdY <= 0; 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            birdY += birdVelocity;
            birdVelocity += 1;
            movePipes();
            gameOver = checkCollision();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 600);  

        for (Pipe pipe : pipes) {
            if (pipe.isTop) {
                g.drawImage(pipeTop, pipe.x, pipe.y, pipe.width, pipe.height, null);
            } else {
                g.drawImage(pipeBottom, pipe.x, pipe.y, pipe.width, pipe.height, null);
            }
        }
    }

    public static void main(String[] args) {
        new MovingPipes();
    }

    // Pipe class to store pipe properties
    class Pipe {
        int x, y, width, height;
        boolean isTop;

        public Pipe(int x, int y, int width, int height, boolean isTop) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.isTop = isTop;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}