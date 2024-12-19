/* Background.java
 * Creates an scrolling background.
 * Modified from - https://www.nutt.net/create-scrolling-background-java/
 * this version requires a 1920x1080 horizonally tiled image called
 * background.jpg saved in the same folder as the class files. 
 * This may be changed.
 */

import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;

public class Background {
	  private Image image; //backgroud image
    private int x; //x coordinate
    private int y; //y coordinate

 
    //constructor
    public Background() {
        this(0,0);
    }//Background
 
    //initializes constructor
    public Background(int x, int y) {
        this.x = x;
        this.y = y;
 
        // try to open the background image file 
        try {
        	image = ImageIO.read(getClass().getClassLoader().getResource("images/background.png"));
        	draw(image.getGraphics());
        }
        catch (Exception e) { System.out.println(e); }
 
    }//Background
 
   	//method that draws the image onto the Graphics object passed
    public void draw(Graphics window) {
        // Draw the first image
        window.drawImage(image, x, y, 1920, 1080, null);

        // Draw the second image immediately after the first
        window.drawImage(image, x + 1920, y, 1920, 1080, null);

        // Move the x position left for the next frame
        x -= 1;

        // Wrap x back to 0 when one full cycle completes
        if (x <= -1920) {
            x = 0;
        }// if
    }// draw
 
    public void setX(int x) {
        this.x = x;
    }//setX
    public int getX() {
        return this.x;
    }//getX
    public int getY() {
        return this.y;
    }//getY
    public int getImageWidth() {
        return 1920;
    }//getImageWidth

	public void setPosition(int i, int j) {
		// TODO Auto-generated method stub	
	}    
}//class Background