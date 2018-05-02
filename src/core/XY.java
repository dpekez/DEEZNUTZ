package core;

import entities.Entity;

import java.util.Random;

public final class XY {

    private final int x;
    private final int y;


    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public XY addVector(XY vector) {
        return new XY(x + vector.x, y + vector.y);
    }


    /**
     * Generates a random move vector, skips zero vectors (0, 0).
     * Generates vectors like  1, -1
     *                        -1,  0
     *                         0,  1
     *                         1,  1
     *                         ...
     *
     * @return  the generated vector
     */

    public static XY generateRandomMoveVector() {
        Random random = new Random();
        int x, y;

        do {

            x = random.nextInt(2) - 1;
            y = random.nextInt(2) - 1;

        } while(x == 0 && y == 0);

        return new XY(x, y);
    }


    /**
     * Generates and returns a random and empty location inside of the
     * walled game board.
     *
     * @param boardSize the board size
     * @param entities  an array of every entity currently on the board
     * @return          the random location
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

            for(Entity entity: entities)
                if(entity != null && entity.getLocation().getX() == xy.x &&
                                     entity.getLocation().getY() == xy.y) {
                    isNotEmpty = true;
                }

        } while(isNotEmpty);

        return xy;
    }


    /**
     * Calculates the distance between two locations.
     *
     * @param xy Position of Comparision
     * @return the length of the XY Object to the other one
     */

    public double distanceFrom(XY xy) {
        return Math.sqrt(Math.pow(xy.getX() - x, 2) + Math.pow(xy.getY() - y, 2));
    }


    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }

}
