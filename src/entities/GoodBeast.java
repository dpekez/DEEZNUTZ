package entities;

import core.EntityContext;
import core.XY;

public class GoodBeast extends Character {

    private static final int DEFAULT_ENERGY = 200;
    private int stepCount = 0;

    public GoodBeast(XY location) {
        super(DEFAULT_ENERGY, location);
    }

    @Override
    public void nextStep(EntityContext entityContext) {
        stepCount++;

        if(stepCount == 4) {
            stepCount = 0;

            Player nearestPlayer = entityContext.nearestPlayerEntity(getLocation());

            if(nearestPlayer == null) {
                entityContext.tryMove(this, XY.generateRandomMoveVector());
                return;
            }

            int xDiff = nearestPlayer.getLocation().getX() - getLocation().getX();
            int yDiff = nearestPlayer.getLocation().getY() - getLocation().getY();

            int moveX, moveY;

            if(xDiff >= 0)
                moveX = -1;
            else
                moveX = 1;

            if(yDiff >= 0)
                moveY = -1;
            else
                moveY = 1;

            entityContext.tryMove(this, new XY(moveX, moveY));

        }
    }

    @Override
    public String toString() {
        return "GoodBeast{ " + super.toString() + " }";
    }

}

