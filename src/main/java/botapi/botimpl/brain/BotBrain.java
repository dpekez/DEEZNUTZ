package botapi.botimpl.brain;


import botapi.ControllerContext;
import botapi.OutOfViewException;
import core.EntityType;
import core.XY;
import core.XYsupport;

import static java.lang.Math.PI;

class BotBrain {

    static XY stuck(ControllerContext context, XY direction) {
        int numberOfRotation = 1;
        XY checkPosition = context.locate().addVector(direction);

        boolean stuck = true;
        if ((freeFieldSpace(context, checkPosition))) {
            return direction;
        }
        XY newVector;
        while (!stuck) {
            newVector = BotBrain.rotation(direction, numberOfRotation);
            checkPosition = context.locate().addVector(newVector);
            if (freeFieldSpace(context, checkPosition)) {
                return newVector;
            } else {
                numberOfRotation++;

                if (numberOfRotation > 3) {
                    return direction.times(-1);
                }
            }
        }
        return null;
    }

    private static XY rotation(XY direction, int numberOfRotation) {
        int x = (int) Math.round(direction.getX() * Math.cos(PI / 4 * numberOfRotation)
                - direction.getY() * Math.sin(PI / 4 * numberOfRotation));
        int y = (int) Math.round(direction.getX() * Math.sin(PI / 4 * numberOfRotation)
                + direction.getY() * Math.cos(PI / 4 * numberOfRotation));
        return new XY(x, y);
    }

    private static XY nearestEntity(ControllerContext context, EntityType type) {
        XY position = context.locate();
        int minX = context.getViewLowerLeft().getX();
        int minY = context.getViewLowerLeft().getY();
        int maxX = context.getViewUpperRight().getX();
        int maxY = context.getViewUpperRight().getY();

        try {
            XY nearestEntity = new XY(15, 15);
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
            e.printStackTrace();
        }
        return null;
    }

    static XY moveToNearestGoodEntity(ControllerContext context, XY maxSize) {
        XY moveDirection = XY.ZERO_ZERO;
        XY nearestBB = BotBrain.nearestEntity(context, EntityType.BAD_BEAST);
        XY nearestGB = BotBrain.nearestEntity(context, EntityType.GOOD_BEAST);
        XY nearestGP = BotBrain.nearestEntity(context, EntityType.GOOD_PLANT);

        if (context.locate().distanceFrom(nearestBB) < 5) {
            if ((context.locate().distanceFrom(nearestGB) >= context.locate().distanceFrom(nearestBB)) & (context.locate().distanceFrom(nearestGP) >= context.locate().distanceFrom(nearestBB))) {
                moveDirection = XYsupport.assignMoveVector(context.locate().reduceVector(nearestBB));
            }
        } else if ((context.locate().distanceFrom(nearestGB)) < 31) {
            moveDirection = XYsupport.assignMoveVector(nearestGB.reduceVector(context.locate()));
        } else if ((context.locate().distanceFrom(nearestGP)) < 31) {
            moveDirection = XYsupport.assignMoveVector(nearestGP.reduceVector(context.locate()));
        }
        return moveDirection;
    }

    static boolean freeFieldSpace(ControllerContext context, XY location) {
        try {
            EntityType entityType = (context.getEntityAt(location));
            switch (entityType) {
                case GOOD_BEAST:
                case GOOD_PLANT:
                case NOTHING:
                    return true;
                case BAD_PLANT:
                case BAD_BEAST:
                case WALL:
                    return false;
                case MINI_SQUIRREL:
                case MASTER_SQUIRREL:
                    return context.isMine(location);
            }
        } catch (OutOfViewException e) {
            e.printStackTrace();
        }
        return false;
    }
}
