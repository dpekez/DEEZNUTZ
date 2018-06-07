package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.entities.Entity;

import java.util.Random;

public class XYsupport {

    /**
     * Generates a random move vector, skips zero vectors (0, 0).
     * Generates vectors like  1, -1
     * -1,  0
     * 0,  1
     * 1,  1
     * ...
     *
     * @return the generated vector
     */

    public static XY generateRandomMoveVector() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(3) - 1;
            y = random.nextInt(3) - 1;
        } while (x == 0 && y == 0);
        return new XY(x, y);
    }

    /**
     * Generates and returns a random and empty location inside of the
     * walled game board.
     *
     * @param boardSize the board size
     * @param entities  an array of every entity currently on the board
     * @return the random location
     */

    public static XY generateRandomLocation(XY boardSize, Entity[] entities) {
        Random random = new Random();
        boolean isNotEmpty;
        XY xy;
        do {
            isNotEmpty = false;
            int randomX = random.nextInt(boardSize.getX() - 2) + 1;
            int randomY = random.nextInt(boardSize.getY() - 2) + 1;
            xy = new XY(randomX, randomY);
            for (Entity entity : entities)
                if (entity != null && entity.getLocation().getX() == xy.getX() &&
                        entity.getLocation().getY() == xy.getY()) {
                    isNotEmpty = true;
                }
        } while (isNotEmpty);
        return xy;
    }

    public static boolean isInRange(XY start, XY target, int viewDistance) {
        if (Math.abs(start.getX() - target.getX()) > (viewDistance - 1) / 2 ||
                (Math.abs(start.getY() - target.getY()) > (viewDistance - 1) / 2)) {
            return false;
        }


        return true;
    }

    public static XY decreaseDistance(XY start, XY target) {
        int xDiff = target.getX() - start.getX();
        int yDiff = target.getY() - start.getY();
        int moveX, moveY;

        moveX = Integer.compare(xDiff, 0);
        moveY = Integer.compare(yDiff, 0);

        return new XY(moveX, moveY);
    }

    public static XY viewLowerLeft(EntityContext context, int viewDistance, XY location) {
        int x = location.getX() - (viewDistance - 1) / 2;
        int y = location.getY() + (viewDistance - 1) / 2;

        if (x < 0)
            x = 0;

        if (y > context.getSize().getY())
            y = context.getSize().getY();

        return new XY(x, y);
    }

    public static XY viewUpperRight(EntityContext context, int viewDistance, XY location) {
        int x = location.getX() + (viewDistance - 1) / 2;
        int y = location.getY() - (viewDistance - 1) / 2;

        if (x > context.getSize().getX())
            x = context.getSize().getX();

        if (y < 0)
            y = 0;

        return new XY(x, y);
    }

}
