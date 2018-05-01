package entities;

import core.EntityContext;
import core.XY;

public class BadBeast extends Character {

    private static final int DEFAULT_ENERGY = -150;
    private int bitesLeft;


    public BadBeast(XY location) {
        super(DEFAULT_ENERGY, location);
        this.bitesLeft = 7;
    }


    @Override
    public void nextStep(EntityContext entityContext) {
        Player nearestPlayer = entityContext.nearestPlayerEntity(getLocation());

        if(nearestPlayer == null) {
            entityContext.tryMove(this, XY.generateRandomMoveVector());
            return;
        }

        int xDiff = nearestPlayer.getLocation().getX() - getLocation().getX();
        int yDiff = nearestPlayer.getLocation().getY() - getLocation().getY();

        int moveX, moveY;

        if(xDiff >= 0)
            moveX = 1;
        else
            moveX = -1;

        if(yDiff >= 0)
            moveY = 1;
        else
            moveY = -1;

        entityContext.tryMove(this, new XY(moveX, moveY));
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