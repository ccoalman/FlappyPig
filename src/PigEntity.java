import java.awt.Rectangle;

public class PigEntity extends Entity {
    private Game game;
    private int velocityY = 0;  
    private final int GRAVITY = 1;  
    private final int JUMP_STRENGTH = -15; 

    public PigEntity(Game g, String spriteRef, int x, int y) {
        super(spriteRef, x, y); 
        this.game = g;
    }

    @Override
    public void move(long delta) {
        velocityY += GRAVITY;
        
        setVerticalMovement(velocityY);
        super.move(delta);

        if (y < 0) {
            y = 0;
            velocityY = 0;
        } else if (y > game.GAME_HEIGHT - getHeight()) {
            game.notifyDeath();
        }
    }

    private int getHeight() {
		return 0;
	}

	public void jump() {
        velocityY = JUMP_STRENGTH;
    }

    @Override
    public void collidedWith(Entity other) {
        if (other instanceof PipeEntity) {
            game.notifyDeath();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
    }
}