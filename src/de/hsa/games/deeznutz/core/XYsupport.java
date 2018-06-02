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

    static XY generateRandomLocation(XY boardSize, Entity[] entities) {
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

    public static boolean isInRange(XY m, XY lL, XY uR) {
        return (m.getX() >= lL.getX()) && (m.getX() <= uR.getX()) && (m.getY() >= lL.getY()) && (m.getY() <= uR.getY());
    }

    public static XY assignMoveVector(XY xy) {
        int newX, newY;
        int oldX = xy.getX();
        int oldY = xy.getY();
        if (oldX == 0) {
            if (oldY == 0)
                return XY.ZERO_ZERO;
            else if (oldY < 0) {
                return XY.UP;
            } else {
                return XY.DOWN;
            }
        } else if (oldY == 0) {
            if (oldX < 0) {
                return XY.LEFT;
            } else {
                return XY.RIGHT;
            }
        } else if (oldX < 0) {
            if (oldY < 0) {
                return XY.LEFT_UP;
            } else {
                return XY.LEFT_DOWN;
            }
        } else {
            if (oldY < 0) {
                return XY.RIGHT_UP;
            } else {
                return XY.RIGHT_DOWN;
            }
        }
    }

    public static XY decreaseDistance(XY start, XY target) {
        int xDiff = target.getX() - start.getX();
        int yDiff = target.getY() - start.getY();
        int moveX, moveY;

        moveX = Integer.compare(xDiff, 0);
        moveY = Integer.compare(yDiff, 0);

        return new XY(moveX, moveY);
    }

}
