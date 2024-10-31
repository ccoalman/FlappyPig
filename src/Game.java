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
      	private BufferStrategy strategy;   // take advantage of accelerated graphics
        private boolean waitingForKeyPress = true;  // true if game held up until
        private final int GAME_WIDTH = 1920;  // width of game
	    private final int GAME_HEIGHT = 1080;  // height of game 

        private boolean gameRunning = true;
      
    	/*
    	 * Construct our game and set it running.
    	 */
    	public Game() {
    		// create a frame to contain game
    		JFrame container = new JFrame("Flappy Pig");
    
    		// get hold the content of the frame
    		JPanel panel = (JPanel) container.getContentPane();
    
    		// set up the resolution of the game
    		panel.setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
    		panel.setLayout(null);
    
    		// set up canvas size (this) and add to frame
    		setBounds(0,0,GAME_WIDTH,GAME_HEIGHT);
    		panel.add(this);
    
    		// Tell AWT not to bother repainting canvas since that will
            // be done using graphics acceleration
    		setIgnoreRepaint(true);
    
    		// make the window visible
    		container.pack();
    		container.setResizable(false);
    		container.setVisible(true);
    
    
            // if user closes window, shutdown game and jre
    		container.addWindowListener(new WindowAdapter() {
    			public void windowClosing(WindowEvent e) {
    				System.exit(0);
    			} // windowClosing
    		});
    
    		// add key listener to this canvas
    		addKeyListener(new KeyInputHandler());
    
    		// request focus so key events are handled by this canvas
    		requestFocus();

    		// create buffer strategy to take advantage of accelerated graphics
    		createBufferStrategy(2);
    		strategy = getBufferStrategy();
    

    
    		// start the game
    		gameLoop();
      } // constructor
    



	/*
	 * gameLoop
         * input: none
         * output: none
         * purpose: Main game loop. Runs throughout game play.
         *          Responsible for the following activities:
	 *           - calculates speed of the game loop to update moves
	 *           - moves the game entities
	 *           - draws the screen contents (entities, text)
	 *           - updates game events
	 *           - checks input
	 */
	public void gameLoop() {
          long lastLoopTime = System.currentTimeMillis();
          
           // Scrolling Background
            BufferedImage back = null; //background image
            Background backOne = new Background(); //first copy of background image (used for moving background)
            Background backTwo = new Background(backOne.getImageWidth(), 0); //second copy of background image (used for moving background)


          // keep loop running until game ends
          while (gameRunning) {
            
            // calc. time since last update, will be used to calculate
            // entities movement
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            
           
            // get graphics context for the accelerated surface and make it black
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
           
            //scrolling Background
            if (back == null)
                back = (BufferedImage)(createImage(getWidth(), getHeight()));

            //creates a buffer to draw to
            Graphics buffer = back.createGraphics();

            //puts the two copies of the background image onto the buffer
            backOne.draw(buffer);
            backTwo.draw(buffer);

            //draws the image onto the window
            g.drawImage(back, null, 0, 0);
  
            // clear graphics and flip buffer
            g.dispose();
            strategy.show();

            // pause
            //try { Thread.sleep(60); } catch (Exception e) {}

          } // while

	} // gameLoop




        /* inner class KeyInputHandler
         * handles keyboard input from the user
         */
	private class KeyInputHandler extends KeyAdapter {
                 
                 private int pressCount = 1;  // the number of key presses since
                                              // waiting for 'any' key press

                /* The following methods are required
                 * for any class that extends the abstract
                 * class KeyAdapter.  They handle keyPressed,
                 * keyReleased and keyTyped events.
                 */
		public void keyPressed(KeyEvent e) {

                  // if waiting for keypress to start game, do nothing
                  if (waitingForKeyPress) {
                    return;
                  } // if
                  
                  // respond to move left, right or fire
//                  if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//                    leftPressed = true;
//                  } // if


		} // keyPressed

		public void keyReleased(KeyEvent e) {
                  // if waiting for keypress to start game, do nothing
                  if (waitingForKeyPress) {
                    return;
                  } // if
                  
                  // respond to move left, right or fire
//                  if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//                    leftPressed = false;
//                  } // if

		} // keyReleased

 	        public void keyTyped(KeyEvent e) {

                   // if waiting for key press to start game
 	           if (waitingForKeyPress) {
                     if (pressCount == 1) {
                       waitingForKeyPress = false;
                       pressCount = 0;
                     } else {
                       pressCount++;
                     } // else
                   } // if waitingForKeyPress

                   // if escape is pressed, end game
                   if (e.getKeyChar() == 27) {
                     System.exit(0);
                   } // if escape pressed

		} // keyTyped

	} // class KeyInputHandler


	/**
	 * Main Program
	 */
	public static void main(String [] args) {
        // instantiate this object
		new Game();
	} // main
} // Game
