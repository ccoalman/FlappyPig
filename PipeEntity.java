import java.awt.Rectangle;

public class PipeEntity extends Entity {
    private Game game;
//    private boolean isTop;  // To distinguish between top and bottom pipes
    private int baseY;  // The original Y position for reference
//    private int verticalRange = 50;  // Maximum vertical distance to move from the base position
    private double verticalSpeed = 0.1;  // Slow vertical speed for continuous movement
    private double currentVerticalOffset = 0;  // Current offset from the base Y position
    private boolean movingUp = true;  // Direction of vertical movement
    public boolean isPassed = false;

    public PipeEntity(Game g, String spriteRef, int x, int y, boolean isPassed) {
        super(spriteRef, x, y);  // Call to Entity constructor
        this.game = g;
        this.baseY = y;  // Set base Y position for movement reference
        this.isPassed = false;
    }

    @Override
    public void move(long delta) {
        // Move pipes left by a fixed speed
        this.setHorizontalMovement(-225); // Set speed for pipes to move left
        super.move(delta);  // Call the move method in Entity to update x position

        // Update vertical position for slow, continuous movement
        if (movingUp) {
            currentVerticalOffset -= verticalSpeed * delta;
            if (currentVerticalOffset <= -50) {  // Reached upper limit
                movingUp = false;  // Start moving down
            }
        } else {
            currentVerticalOffset += verticalSpeed * delta;
            if (currentVerticalOffset >= 50) {  // Reached lower limit
                movingUp = true;  // Start moving up
            }
        }
        // Apply the vertical offset to the pipe's Y position
        y = baseY + (int) currentVerticalOffset;
    }

    @Override
    public void collidedWith(Entity other) {
        // If it collides with the ship or bird, you may add code to handle game over
        if (other instanceof PigEntity) {
            game.notifyDeath();
        }
    }
    
    public void collected() {
    	this.isPassed = true;
    }

    // Accessor for checking if this is a top pipe
//    public boolean isTop() {
//        return isTop;
//    }

    // Get collision boundary
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
    }
}