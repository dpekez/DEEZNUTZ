package entities;

import core.EntityContext;
import core.XY;

public class BadBeast extends Character {

    private static final int DEFAULT_ENERGY = -150;
    private int stepCount = 0;
    private int bitesLeft;


    public BadBeast(XY location) {
        super(DEFAULT_ENERGY, location);
        this.bitesLeft = 7;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void nextStep(EntityContext entityContext) {
        stepCount++;

        if(stepCount == 4) {
            stepCount = 0;

            Player nearestPlayer = entityContext.nearestPlayerEntity(getLocation());

            if (nearestPlayer == null) {
                entityContext.tryMove(this, XY.generateRandomMoveVector());
                return;
            }

            int xDiff = nearestPlayer.getLocation().getX() - getLocation().getX();
            int yDiff = nearestPlayer.getLocation().getY() - getLocation().getY();

            int moveX, moveY;

            moveX = Integer.compare(xDiff, 0);
            moveY = Integer.compare(yDiff, 0);

            entityContext.tryMove(this, new XY(moveX, moveY));
        }
    }

    public void bite() {
        bitesLeft--;

    }

    public int getBitesLeft() {
        return this.bitesLeft;
    }

    @Override
    public String toString() {
        return "BadBeast{ " + super.toString() + " }";
    }

}