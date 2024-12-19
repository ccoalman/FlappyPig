/* Game.java
 * Space Invaders Main Program
 * with a scrolling background.
 */
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Game extends Canvas {
    private BufferStrategy strategy;
    private boolean waitingForKeyPress = false;
    private final int GAME_WIDTH = 1680; // game window width
    final int GAME_HEIGHT = 1080; // game window height
    
    public boolean startScreen = true; // if true, display start screen
    private boolean skinScreen = false; // if true, display skin screen
    public boolean endScreen = false; // if true, display end screen
    private boolean gameStarted = false;
    private boolean gameRunning = false; 
    private PigEntity pig; // declare pig
    private String selectedSkinPath = "images/FlyingPig1.png"; // defualt skin for pig
    private ArrayList<PipePair> pipes = new ArrayList<>(); // declare pipes
    private long lastPipeTime = 0; // declare last pipe spawn time
    private int score = 0; // score
    private int highscore = 0; // final score
    private boolean isNewHighScore = false;
    
    // game variables
    Graphics2D g;
    String message; // message that displays when you die
    
    // buttons
    private Rectangle startButton;
    private Rectangle skinButton;
    private Rectangle homeButton;
    private Rectangle retryButton;
    private Rectangle skin1Button;
    private Rectangle skin2Button;
    private Rectangle skin3Button;
    private Rectangle backButton;
    
    // game method
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

        addKeyListener(new KeyInputHandler()); // key input listener
        addMouseListener(new MouseInputHandler()); // mouse input listener
     
        requestFocus();

        createBufferStrategy(2);
        strategy = getBufferStrategy();
        
        // initialize PigEntity
        pig = new PigEntity(this, "images/FlyingPig1.png", 100, GAME_HEIGHT / 2);
        
        // initialize buttons
        startButton = new Rectangle(GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 + 10, 200, 50);
        skinButton = new Rectangle(GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 + 100, 200, 50);
        homeButton = new Rectangle(GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 + 10, 200, 50);
        retryButton = new Rectangle(GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 + 100, 200, 50);
        backButton = new Rectangle(GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 + 300, 200, 50);
        skin1Button = new Rectangle(GAME_WIDTH / 2 - 100, 300, 200, 50);
        skin2Button = new Rectangle(GAME_WIDTH / 2 - 100, 380, 200, 50);
        skin3Button = new Rectangle(GAME_WIDTH / 2 - 100, 460, 200, 50);

        gameLoop();
    } // game
    
    // create pipes
    public void createPipes() {
        int topPipeHeight = 100 + new Random().nextInt(300); 
        int gap = 1000;  
        int initialY = topPipeHeight - gap / 2;

        PipePair pipePair = new PipePair(this, "images/Pipe_Top.png", "images/pipe_bottom.png", GAME_WIDTH, initialY, gap);
        pipes.add(pipePair);
    } // createPipes()
    
    // creates a button for the skins menu (for less repeated code)
    private void generateButton(String btnText, int posX, int posY, int fontSize, int btnWidth, int btnHeight, String subtext) {
    	Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
    	g.setColor(Color.WHITE);
        g.fillRect(posX, posY, btnWidth, btnHeight);
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        g.drawString(btnText, posX + (btnWidth - g.getFontMetrics().stringWidth(btnText)) / 2, posY + 32);
        
        if (subtext != "") {
        	g.setColor(Color.white);
        	g.drawString(subtext, posX + 150 + (btnWidth - g.getFontMetrics().stringWidth(btnText)) / 2, posY + 32);
        }// if
    }// generateButton
    
    // fixed image background
    private void drawStaticBackground(Graphics2D g) {
        Background staticBackground = new Background();
        staticBackground.setPosition(0, 0);
        staticBackground.draw(g);
    }// drawStaticBackground

    public void gameLoop() {
    	gameRunning = true;
        long lastLoopTime = System.currentTimeMillis();

        BufferedImage back = null;
        Background backOne = new Background();

        while (gameRunning) {
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

            if (back == null) back = (BufferedImage) (createImage(getWidth(), getHeight()));

            Graphics buffer = back.createGraphics();

            g.drawImage(back, null, 0, 0);
            
            // everything about startScreen drawn here
            if (startScreen && !skinScreen) {
                // Draw background
            	drawStaticBackground(g); 
                
                g.setFont(new Font("Arial", Font.BOLD, 60));
                g.setColor(Color.BLACK);
                String startMessage = "Welcome to Flappy Pig!";
                g.drawString(startMessage, (GAME_WIDTH - g.getFontMetrics().stringWidth(startMessage)) / 2, GAME_HEIGHT / 2 - 50);
                
                // start Button
                g.setColor(Color.WHITE);
                g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                String startButtonText = "Start Game";
                g.drawString(startButtonText, startButton.x + (startButton.width - g.getFontMetrics().stringWidth(startButtonText)) / 2, startButton.y + 35);
                
                // skins menu Button
                g.setColor(Color.WHITE);
                g.fillRect(skinButton.x, skinButton.y, skinButton.width, skinButton.height);
                g.setColor(Color.BLACK);
                g.drawString("Skins", skinButton.x + (skinButton.width - g.getFontMetrics().stringWidth("Skins")) / 2, skinButton.y + 35);

                g.dispose();
                strategy.show();
                continue;
            }// if (startScreen)
            
            // everything about startScreen drawn here
            if (skinScreen) {
            	g.setColor(Color.BLACK);
                g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.setColor(Color.white);
                String startMessage = "Please select a skin.";
                g.drawString(startMessage, (GAME_WIDTH - g.getFontMetrics().stringWidth(startMessage)) / 2, 100);
                
                // buttons VV          
                generateButton("default skin", skin1Button.x, skin1Button.y, 20, skin1Button.width, skin1Button.height, "");
                generateButton("skin 1", skin2Button.x, skin2Button.y, 20, skin2Button.width, skin2Button.height, "Required highscore: 20");
                generateButton("skin 2", skin3Button.x, skin3Button.y, 20, skin3Button.width, skin3Button.height, "Required highscore: 50");
                
                g.setColor(Color.WHITE);
                g.fillRect(backButton.x, backButton.y, backButton.width, backButton.height);
                g.setColor(Color.black);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                String backButtonText = "Back";
                g.drawString(backButtonText, startButton.x + (backButton.width - g.getFontMetrics().stringWidth(backButtonText)) / 2, backButton.y + 35);                               
                
                g.dispose();
                strategy.show();
                try { Thread.sleep(20); } catch (Exception e) {}
                continue;
            }// if (skinScreen)
            
            // everything about endScreen drawn here
            if (endScreen) {
            	g.setColor(Color.BLACK);
                g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                
                if (isNewHighScore) {
                	g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 25)); 
                    g.setColor(Color.yellow);
                    g.drawString("new highscore!!", (GAME_WIDTH - g.getFontMetrics().stringWidth("New highscore!!"))/2, 420);
                }// if (isNewHighScore)
                
                g.setFont(new Font("Arial", Font.BOLD, 60));
                g.setColor(Color.WHITE);
                String endMessage = "You Died!";
                g.drawString(endMessage, (GAME_WIDTH - g.getFontMetrics().stringWidth(endMessage)) / 2, GAME_HEIGHT / 2 - 50);
                
                g.setColor(Color.WHITE);
                g.fillRect(homeButton.x, homeButton.y, homeButton.width, homeButton.height);          
                	
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                String homeButtonText = "Home";
                g.drawString(homeButtonText, homeButton.x + (homeButton.width - g.getFontMetrics().stringWidth(homeButtonText)) / 2, homeButton.y + 35);
                
                g.setColor(Color.WHITE);
                g.fillRect(retryButton.x, retryButton.y, retryButton.width, retryButton.height);
                
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                String retryButtonText = "Retry";
                g.drawString(retryButtonText, retryButton.x + (retryButton.width - g.getFontMetrics().stringWidth(retryButtonText)) / 2, retryButton.y + 35);
      
                g.dispose();
                strategy.show();
                continue;
            }// if (endScreen)
            
            pig.draw(g); // draw pig entity
            
            // check last pipe time and pipe collision
            if (!waitingForKeyPress) {
	            pig.move(delta);
	            
	            backOne.draw(buffer); // updates position of background
	
	            if (System.currentTimeMillis() - lastPipeTime > 2500) {
	                createPipes();
	                lastPipeTime = System.currentTimeMillis();
	            } // spawns pipes if time is valid
	
	            for (PipePair pipePair : pipes) {
	                pipePair.move(delta);
	
	                if (pig.collidesWith(pipePair.getTopPipe()) || pig.collidesWith(pipePair.getBottomPipe())) {
	                    notifyDeath();
	                }// if
	            }// for
            }// if

            pipes.removeIf(pipePair -> pipePair.getTopPipe().getX() < -150); // remove pipe when pipe is off screen to reduce lag                    

            for (PipePair pipePair : pipes) {
                pipePair.getTopPipe().draw(g);
                pipePair.getBottomPipe().draw(g);
                
                if (pig.getX() >= pipePair.getTopPipe().getX() && pipePair.getTopPipe().isPassed == false) {
                	pipePair.getTopPipe().collected();
                	score++;
                } // if pig survived pipe, add score 
            } // for

            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 55));
            g.setColor(Color.black);
            g.drawString("" + score, (1820 - g.getFontMetrics().stringWidth("score:  "))/2, 70);
            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 35)); 
            g.drawString("highscore: " + highscore, (150 - g.getFontMetrics().stringWidth("score:  "))/2, 40);
            
            // shows death screen if dead (has to be down here so it shows over pipes)
            if (waitingForKeyPress) {
            	g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 50));  // last param is font size
            	g.setColor(Color.red);
                g.drawString(message, (GAME_WIDTH - g.getFontMetrics().stringWidth(message))/2, 475);
                
                g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20)); 
                g.setColor(Color.black);
                g.drawString("> press any key to restart <", (GAME_WIDTH - g.getFontMetrics().stringWidth("> press any key to restart <"))/2, 525);
                
                if (isNewHighScore) {
                	g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 25)); 
                    g.setColor(Color.yellow);
                    g.drawString("new highscore!!", (GAME_WIDTH - g.getFontMetrics().stringWidth("new highscore!!"))/2, 420);
                }
            } // if waitingForKeyPress
            
            if (!gameStarted) {
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.setColor(Color.WHITE);
                String prompt = "Press Space to Start";
                g.drawString(prompt, (GAME_WIDTH - g.getFontMetrics().stringWidth(prompt)) / 2, GAME_HEIGHT / 2);
                resetGame();
                g.dispose();
                strategy.show();
                continue; // Skip game logic until space is pressed
            }
            
            g.dispose();
            strategy.show();
            
            // this makes the cpu usage go from 30ish% to 10-20%
            // game loop could be optimized because the school computers aren't very good
            try { Thread.sleep(1); } catch (Exception e) {}
         }
        
     }// gameLoop
  
    // stops sprites and displays death screen
    public void notifyDeath() {   
        waitingForKeyPress = true;
        message = "you \"died\"";
        endScreen = true;
        
        if (score > highscore) { 
            highscore = score;
            isNewHighScore = true;
        } // if new high score   
    } // notifyDeath

    private void resetGame() {
        pig.setY(GAME_HEIGHT / 2);
        pig.resetVelocity();
        waitingForKeyPress = false;
        gameRunning = true;
        pipes.clear();
        lastPipeTime = 0;
        score = 0;
        isNewHighScore = false;
    } // resetGame()

    // key input handler for pig jump
    private class KeyInputHandler extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (waitingForKeyPress) {
                waitingForKeyPress = false;
                resetGame();
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (!gameStarted && startScreen == false && endScreen == false) {
                    gameStarted = true; // Start the game on the first space press
                } else {
                    pig.jump();
                }// else
            }// else if
        } // keyPressed()
    } // KeyInputHandler
    
    // mouse click event handler
    private class MouseInputHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (startScreen && startButton.contains(e.getPoint())) { // starts game on click
                startScreen = false;
                resetGame();
            }// if
            
            if (endScreen && homeButton.contains(e.getPoint())) { // back to start screen on click
                startScreen = true;
                endScreen = false;
                gameStarted = false;
                resetGame();
            }// if
            
            if (endScreen && retryButton.contains(e.getPoint())) { // restarts game on click
                startScreen = false;
                endScreen = false;
                gameStarted = false;
                resetGame();
            }// if
            
            if (startScreen && skinButton.contains(e.getPoint())) { // skin screen on click
                skinScreen = true;
                startScreen = false;
            }// if
            
            if (skinScreen && backButton.contains(e.getPoint())) { // back to home on click
                skinScreen = false;
                startScreen = true;
            }// if
            
            if (skinScreen && skin1Button.contains(e.getPoint())) { // default skin
                pig.setSprite("images/FlyingPig1.png");
            }// if

            if (skinScreen && skin2Button.contains(e.getPoint())) { // second skin
            	if (highscore >= 20) {
            		pig.setSprite("images/FlyingPig2.png"); 
            	}// if
            }// if
            
            if (skinScreen && skin3Button.contains(e.getPoint())) { // final skin
            	if (highscore >= 50) {
            		pig.setSprite("images/FlyingPig3.png"); 
            	}// if
            }// if
        }// mouseClicked
    }// MouseInputHandler

    public static void main(String[] args) {
        new Game();
    } // main
} // class