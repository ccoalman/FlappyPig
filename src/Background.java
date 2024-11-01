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
    private Image image;
    private int x;
    private int y;

    public Background() {
        this(0, 0);
    }

    public Background(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource("images/background.png"));
            if (image == null) {
                System.err.println("Background image could not be loaded!");
            }
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
    }

    public void draw(Graphics window) {
        if (image == null) return;  // Skip drawing if no image is loaded
        window.drawImage(image, getX(), getY(), 1920, 1080, null);
        this.x -= 1;
        if (this.x <= -1920) {
            this.x += 1920 * 2;
        }
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getImageWidth() { return 1920; }
}