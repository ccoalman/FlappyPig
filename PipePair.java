public class PipePair {
    private PipeEntity topPipe;
    private PipeEntity bottomPipe;
    private int gapSize;  // Gap between top and bottom pipes

    public PipePair(Game game, String topSpriteRef, String bottomSpriteRef, int x, int initialY, int gapSize) {
        this.gapSize = gapSize;
        // Create top and bottom pipes, with initial positions
        this.topPipe = new PipeEntity(game, topSpriteRef, x, initialY, false);
        this.bottomPipe = new PipeEntity(game, bottomSpriteRef, x, initialY + gapSize, false);
    }

    public void move(long delta) {
        // Move both pipes horizontally and manage vertical movement together
        topPipe.move(delta);
        bottomPipe.move(delta);

        // Ensure the bottom pipe maintains the gap distance from the top pipe
        bottomPipe.setY(topPipe.getY() + gapSize);
    }

    public void collidedWith(Entity other) {
        topPipe.collidedWith(other);
        bottomPipe.collidedWith(other);
    }

    public PipeEntity getTopPipe() {
        return topPipe;
    }

    public PipeEntity getBottomPipe() {
        return bottomPipe;
    }
}