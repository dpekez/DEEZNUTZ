package botimpl.mozartuss;

import botapi.BotController;
import botapi.ControllerContext;
import botapi.OutOfViewException;
import core.EntityType;
import core.XY;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MiniBotBrain implements BotController {
    private static final Logger logger = Logger.getLogger(MiniBotBrain.class.getName());

    @Override
    public void nextStep(ControllerContext view) {
        int impactRadius = 3;
        boolean shouldImplode = implodeCondition(view, impactRadius);

        if (shouldImplode) {
            logger.log(Level.INFO, "MiniSquirrelBot implode!");
            view.implode(impactRadius);
        }

        XY move = BotBrain.moveToNearestGoodEntity(view);
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

        int x = view.locate().getX() + impactRadius;
        int y = view.locate().getY() + impactRadius;

        // Begrenzung setzen
        if (x > view.getViewUpperRight().getX())
            x = view.getViewUpperRight().getX() - 1;
        else if (x < view.getViewLowerLeft().getX())
            x = view.getViewLowerLeft().getX();

        if (y < view.getViewUpperRight().getY())
            y = view.getViewUpperRight().getY() - 1;
        else if (y > view.getViewLowerLeft().getY())
            y = view.getViewLowerLeft().getY();

        for (int i = 0; i < impactRadius; i++) {
            for (int j = 0; j < impactRadius; j++) {
                try {
                    EntityType checkEntity = view.getEntityAt(view.locate().addVector(new XY(x, y)));
                    if (checkEntity == EntityType.GOOD_BEAST || checkEntity == EntityType.GOOD_PLANT) {
                        counterToImplode++;
                    }
                } catch (OutOfViewException e) {
                    logger.log(Level.WARNING, "No Entity in the searchVector");
                }
            }
        }
        if (counterToImplode >= 3)
            shouldImpode = true;
        return shouldImpode;
    }
}
