package de.hsa.games.deeznutz.botapi;

import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;

public interface ControllerContext {

    /**
     * @return lower left corner of the view rectangle
     */
    XY getViewLowerLeft();

    /**
     * @return upper right corner of the view rectangle
     */
    XY getViewUpperRight();

    /**
     * @param xy : cell coordinates
     * @return the type of the entity at that position or none
     * @throws OutOfViewException if xy is outside the view
     */
    EntityType getEntityAt(XY xy) throws OutOfViewException;

    /**
     * @param direction : one of XY.UP, XY.DOWN, ...
     *                  XY.ZERO_ZERO means I want to pause
     */
    void move(XY direction);

    /**
     * @param direction : one of XY.UP, XY.DOWN, ...
     * @param energy    : start energy of the min, at least 100
     * @throws SpawnException if either direction or energy is invalid
     */
    void spawnMiniBot(XY direction, int energy) throws SpawnException;

    /**
     * @return the current energy of the player entity
     */
    int getEnergy();

    /**
     * @return my cell coordinates, i. e. the position of the controlled player entity
     */
    XY locate();

    /**
     * @param xy : cell coordinates
     * @return true, if entity at xy is my master or one of my minis resp.
     * @throws OutOfViewException if xy is outside the view
     */
    boolean isMine(XY xy) throws OutOfViewException;

    /**
     * Very destructive event (see specification for details).
     * Can only be called for mini bot, otherwise exception.
     *
     * @param impactRadius : radius of the impact circle
     */
    void implode(int impactRadius);

    /**
     * @return the direction where the master can be found
     */
    XY directionOfMaster();

    /**
     * @return how many steps are left until the end of the current round
     */
    long getRemainingSteps();

    /**
     * implementation is optional
     *
     * @param text : the comment of the bot during fighting, e. g. ouch
     */
    default void shout(String text) {
    }
}

