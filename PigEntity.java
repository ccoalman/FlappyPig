import java.awt.Rectangle;

public class PigEntity extends Entity {
    private Game game;
    private int velocityY = 0;  
    private final int GRAVITY = 12;
    private final int JUMP_STRENGTH = -500;

    public PigEntity(Game g, String spriteRef, int x, int y) {
        super(spriteRef, x, y); 
        this.game = g;
    }
    
    public void setSprite(String spriteRef) {
        this.sprite = SpriteStore.get().getSprite(spriteRef);
    }
    
    @Override
    public void move(long delta) {
        velocityY += GRAVITY;
        
        setVerticalMovement(velocityY);
        super.move(delta);

        if (y < 0) {
            y = 0;
            velocityY = 0;
        } else if (y > game.GAME_HEIGHT - sprite.getHeight()) {
            game.notifyDeath();
        }
    }

//    private int getHeight() {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	public void jump() {
        // Give the bird an upward velocity when it jumps
        velocityY = JUMP_STRENGTH;
    }

    public void resetVelocity() {
        velocityY = 0;
    }

    @Override
    public void collidedWith(Entity other) {
        // If the bird collides with a pipe, it’s game over
        if (other instanceof PipeEntity) {
            game.notifyDeath();
        }
    }

    // Get collision boundary for checking collisions with pipes
    public Rectangle getBounds() {

        return new Rectangle((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
    }
}