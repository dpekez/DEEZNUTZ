package botapi.botimpl.brain;

import botapi.BotController;
import botapi.ControllerContext;
import botapi.OutOfViewException;
import core.EntityType;
import core.XY;

public class MiniBotBrain implements BotController {

    @Override
    public void nextStep(ControllerContext view) {
        XY maxSize = view.getViewUpperRight();
        int impactRadius = 5;
        boolean shouldImplode = implodeCondition(view, impactRadius);

        if (shouldImplode)
            view.implode(impactRadius);

        XY move = BotBrain.moveToNearestGoodEntity(view, maxSize);
        if (view.getEnergy() < 4000) {
            view.move(move);
        } else {
            move = view.directionOfMaster();
            view.move(move);
        }
    }

    private boolean implodeCondition(ControllerContext view, int impactRadius) {
        int counterToImplode = 0;
        boolean shouldImpode = false;

        for (int i = 0; i < impactRadius; i++) {
            for (int j = 0; j < impactRadius; j++) {
                try {
                    int x = view.locate().getX() + i;
                    int y = view.locate().getY() + j;

                    // Begrenzung setzen
                    if (x > view.getViewUpperRight().getX())
                        x = view.getViewUpperRight().getX();
                    else if (x < view.getViewLowerLeft().getX())
                        x = view.getViewLowerLeft().getX();

                    if (y < view.getViewUpperRight().getY())
                        y = view.getViewUpperRight().getY();
                    else if (y > view.getViewLowerLeft().getY())
                        y = view.getViewLowerLeft().getY();

                    EntityType checkEntity = view.getEntityAt(view.locate().addVector(new XY(x, y)));
                    if (checkEntity == EntityType.GOOD_BEAST || checkEntity == EntityType.GOOD_PLANT) {
                        counterToImplode++;
                    }
                } catch (OutOfViewException e) {
                    e.printStackTrace();
                }
            }
        }
        if (counterToImplode >= 3)
            shouldImpode = true;
        return shouldImpode;
    }
}
