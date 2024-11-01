import java.awt.Rectangle;

public class PipeEntity extends Entity {
    private Game game;
    private boolean isTop;  // To distinguish between top and bottom pipes

    public PipeEntity(Game g, String spriteRef, int x, int y, boolean isTop) {
        super(spriteRef, x, y);  // Call to Entity constructor
        this.game = g;
        this.isTop = isTop;
    }

    @Override
    public void move(long delta) {
        // Move pipes left by a fixed speed
        this.setHorizontalMovement(-100); // Set speed for pipes to move left
        super.move(delta);  // Call the move method in Entity to update x position
    }

    @Override
    public void collidedWith(Entity other) {
        // If it collides with the ship or bird, you may add code to handle game over
        if (other instanceof PigEntity) {
            game.notifyDeath();
        }
    }

    // Accessor for checking if this is a top pipe
    public boolean isTop() {
        return isTop;
    }

    // Get collision boundary
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
    }
}