package de.hsa.games.deeznutz.botimpls.mozartuss;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.OutOfViewException;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

import java.util.logging.Logger;

public class BotBrain {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    static XY moveToNearestGoodEntity(ControllerContext context) {
        XY moveDirection = XY.ZERO_ZERO;
        XY nearestMasterBot = BotBrain.nearestEntity(context, EntityType.MASTER_SQUIRREL_BOT);
        XY nearestMaster = BotBrain.nearestEntity(context, EntityType.MASTER_SQUIRREL);
        XY nearestMini = (BotBrain.nearestEntity(context, EntityType.MINI_SQUIRREL));
        XY nearestMiniBot = (BotBrain.nearestEntity(context, EntityType.MINI_SQUIRREL_BOT));
        XY nearestBP = BotBrain.nearestEntity(context, EntityType.BAD_PLANT);
        XY nearestBB = BotBrain.nearestEntity(context, EntityType.BAD_BEAST);
        XY nearestGB = BotBrain.nearestEntity(context, EntityType.GOOD_BEAST);
        XY nearestGP = BotBrain.nearestEntity(context, EntityType.GOOD_PLANT);
        XY nearestWW = BotBrain.nearestEntity(context, EntityType.WALL);
        XY nearestPositive;
        if (nearestGB.distanceFrom(context.locate()) < nearestGP.distanceFrom(context.locate()))
            nearestPositive = nearestGB;
        else nearestPositive = nearestGP;

        if (context.locate().distanceFrom(nearestBB) < 1) {
            if ((context.locate().distanceFrom(nearestPositive) > context.locate().distanceFrom(nearestBB))) {
                moveDirection = XYsupport.decreaseDistance(nearestBB, context.locate());
            }
        } else if ((context.locate().distanceFrom(nearestPositive)) < 16) {
            moveDirection = XYsupport.decreaseDistance(context.locate(), nearestPositive);
        } else if ((context.locate().distanceFrom(nearestBP)) < 1) {
            moveDirection = XYsupport.decreaseDistance(nearestBP, context.locate());
        } else if ((context.locate().distanceFrom(nearestWW)) < 1) {
            moveDirection = XYsupport.decreaseDistance(nearestWW, context.locate());
        } else if ((context.locate().distanceFrom(nearestMiniBot)) < 16) {
            moveDirection = XYsupport.decreaseDistance(context.locate(), nearestMiniBot);
        } else if ((context.locate().distanceFrom(nearestMini)) < 16) {
            moveDirection = XYsupport.decreaseDistance(context.locate(), nearestMini);
        } else if ((context.locate().distanceFrom(nearestMasterBot)) < 2) {
            moveDirection = XYsupport.decreaseDistance(nearestMasterBot, context.locate());
        } else if ((context.locate().distanceFrom(nearestMaster)) < 2) {
            moveDirection = XYsupport.decreaseDistance(nearestMaster, context.locate());
        } else {
            if ((context.locate().distanceFrom(new XY((context.getViewUpperRight().getX() / 2), (context.getViewLowerLeft().getY() / 2)))) <
                    (context.locate().distanceFrom(new XY((context.getViewLowerLeft().getX() / 2), (context.getViewUpperRight().getY() / 2))))) {
                moveDirection = XYsupport.decreaseDistance(context.locate(), new XY(context.getViewLowerLeft().getX(), context.getViewUpperRight().getY()));
            } else {
                moveDirection = XYsupport.decreaseDistance(context.locate(), new XY(context.getViewUpperRight().getX(), context.getViewLowerLeft().getY()));
            }
        }
        return moveDirection;
    }

    public static XY nearestEntity(ControllerContext context, EntityType type) {
        XY position = context.locate();
        int minX = context.getViewLowerLeft().getX();
        int minY = context.getViewUpperRight().getY();
        int maxX = context.getViewUpperRight().getX();
        int maxY = context.getViewLowerLeft().getY();

        try {
            XY nearestEntity = new XY(100, 100);
            for (int x = minX; x < maxX; x++) {
                for (int y = minY; y < maxY; y++) {
                    if (context.getEntityAt(new XY(x, y)) == type) {
                        double distanceTo = position.distanceFrom(new XY(x, y));
                        if (distanceTo < position.distanceFrom(nearestEntity)) {
                            nearestEntity = new XY(x, y);
                        }
                    }
                }
            }
            return nearestEntity;
        } catch (OutOfViewException e) {
            logger.finer("No Entity in vektor");
        }
        return null;
    }

    static boolean checkSpawnField(ControllerContext context, XY location) {
        try {
            EntityType entityType = context.getEntityAt(location);
            switch (entityType) {
                case NOTHING:
                    return true;
                default:
                    return false;
            }
        } catch (OutOfViewException e) {
            logger.finer("No Entity in Spawnfield");
        }
        return false;
    }
}
