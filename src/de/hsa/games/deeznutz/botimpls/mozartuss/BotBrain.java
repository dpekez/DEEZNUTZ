package de.hsa.games.deeznutz.botimpls.mozartuss;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.OutOfViewException;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

import java.util.logging.Logger;

public class BotBrain {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    XY moveToNearestGoodEntity(ControllerContext context) {
        XY moveDirection = XY.ZERO_ZERO;
        XY nearestBP = nearestEntity(context, EntityType.BAD_PLANT);
        XY nearestBB = nearestEntity(context, EntityType.BAD_BEAST);
        XY nearestGB = nearestEntity(context, EntityType.GOOD_BEAST);
        XY nearestGP = nearestEntity(context, EntityType.GOOD_PLANT);
        XY nearestWW = nearestEntity(context, EntityType.WALL);
        XY nearestPositive;

        if (nearestGB.distanceFrom(context.locate()) < nearestGP.distanceFrom(context.locate()))
            nearestPositive = nearestGB;
        else nearestPositive = nearestGP;

        if (context.locate().distanceFrom(nearestBB) < 1) {
            logger.finer("flee from badBeast");
            if ((context.locate().distanceFrom(nearestPositive) > context.locate().distanceFrom(nearestBB))) {
                moveDirection = XYsupport.decreaseDistance(nearestBB, context.locate());
            }
        } else if ((context.locate().distanceFrom(nearestPositive)) < 16) {
            logger.finer("shrink the distance between Squirrel and positiveEntities");
            moveDirection = XYsupport.decreaseDistance(context.locate(), nearestPositive);
        } else if ((context.locate().distanceFrom(nearestBP)) < 1) {
            moveDirection = XYsupport.decreaseDistance(nearestBP, context.locate());
        } else if ((context.locate().distanceFrom(nearestWW)) < 1) {
            moveDirection = XYsupport.decreaseDistance(nearestWW, context.locate());
        } else {
            moveDirection = XYsupport.generateRandomMoveVector();
        }
        return moveDirection;
    }

    private XY nearestEntity(ControllerContext context, EntityType type) {
        logger.finer("searching for the nearest entities");
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
            logger.finer("No Entity in vector");
        }
        return null;
    }

    boolean checkSpawnField(ControllerContext context, XY location) {
        try {
            EntityType entityType = context.getEntityAt(location);
            return entityType == EntityType.NOTHING;
        } catch (OutOfViewException e) {
            logger.finer("No Entity in spawnfield");
        }
        return false;
    }
}
