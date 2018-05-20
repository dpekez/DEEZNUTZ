package botapi.botimpl;


import botapi.ControllerContext;
import botapi.OutOfViewException;
import core.EntityType;
import core.XY;

public class FindGoodEntities {

    private static XY nearestEntity(ControllerContext context, EntityType type) {
        XY position = context.locate();
        int minX = context.getViewLowerLeft().getX();
        int minY = context.getViewLowerLeft().getY();
        int maxX = context.getViewUpperRight().getX();
        int maxY = context.getViewUpperRight().getY();

        try {
            XY nearestEntity = new XY(31, 31);
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

    static XY moveToNearestGoodEntity(ControllerContext context) {
        XY moveDirection = XY.ZERO_ZERO;
        XY nearestBP = FindGoodEntities.nearestEntity(context, EntityType.BAD_PLANT);
        XY nearestBB = FindGoodEntities.nearestEntity(context, EntityType.BAD_BEAST);
        XY nearestGB = FindGoodEntities.nearestEntity(context, EntityType.GOOD_BEAST);
        XY nearestGP = FindGoodEntities.nearestEntity(context, EntityType.GOOD_PLANT);

        if (context.locate().distanceFrom(nearestBB) < 5)
            if ((context.locate().distanceFrom(nearestGB) > context.locate().distanceFrom(nearestBB)) || (context.locate().distanceFrom(nearestGP) > context.locate().distanceFrom(nearestBB)))
                moveDirection = moveTo(context, nearestBB);

            else if ((context.locate().distanceFrom(nearestBP)) < 2)
                moveDirection = moveTo(context, nearestBP);

            else if ((context.locate().distanceFrom(nearestGB)) < 16)
                moveDirection = moveTo(context, nearestGB);

            else if ((context.locate().distanceFrom(nearestGP)) < 16)
                moveDirection = moveTo(context, nearestGP);

        return moveDirection;
    }

    private static XY moveTo(ControllerContext context, XY nearestEntity) {
        XY moveDirection;
        int x = 0;
        int y = 0;
        XY vector = context.locate().reduceVector(nearestEntity);
        if (vector.getX() > 0) {
            x = 1;
        }
        if (vector.getY() > 0) {
            y = 1;
        }
        moveDirection = new XY(x, y);
        return moveDirection;
    }
}
