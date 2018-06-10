package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.entities.*;

import java.util.logging.Logger;

/**
 * All the movements and their consequences are checked in this class.
 */

public class FlattenedBoard implements BoardView, EntityContext {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private Board board;
    private Entity[][] cells;

    public FlattenedBoard(Board board) {
        this.board = board;
        updateFlattenedBoard();
    }

    private void updateFlattenedBoard() {
        BoardConfig config = board.getConfig();
        Entity[][] newFlatBoard = new Entity[config.getWidth()][config.getHeight()];

        Entity[] entityArray = board.getEntities();

        for (Entity entity : entityArray) {
            int x, y;

            if (entity != null) {
                x = entity.getLocation().getX();
                y = entity.getLocation().getY();

                if (x < config.getWidth() && x >= 0 && y < config.getHeight() && y >= 0) {
                    newFlatBoard[x][y] = entity;
                }
            }
        }
        cells = newFlatBoard;
    }

    @Override
    public int getWaitingTimeBeast() {
        return board.getConfig().getWaitingTimeBeast();
    }

    @Override
    public int getPlayerViewDistance() {
        return board.getConfig().getPlayerEntityViewDistance();
    }

    @Override
    public EntityType getEntityType(XY xy) {
        return getEntityType(xy.getX(), xy.getY());
    }

    @Override
    public EntityType getEntityType(int x, int y) {

        if (cells[x][y] == null)
            return EntityType.NOTHING;

        return EntityType.getType(cells[x][y]);
    }

    @Override
    public Entity getEntity(int x, int y) {
        if (cells[x][y] == null)
            return null;

        return cells[x][y];
    }

    @Override
    public XY getSize() {
        return board.getConfig().getBoardSize();
    }

    /**
     * This methode define the Entity rules.
     * What happen if a MasterSquirrel is moving against a wall or a Badbeast ,etc...
     *
     * @param miniSquirrel, goodBeast, badBeats, masterSquirrel
     * @param moveDirection of the Entity
     */

    @Override
    public void tryMove(MiniSquirrel miniSquirrel, XY moveDirection) {
        XY nextPosition = miniSquirrel.getLocation().addVector(moveDirection);
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];

        switch (getEntityType(nextPosition)) {
            case NOTHING:
                miniSquirrel.move(moveDirection);
                updateFlattenedBoard();
                break;
            case WALL:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                miniSquirrel.stun();
                break;
            case BAD_BEAST:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                break;
            case GOOD_PLANT:
            case BAD_PLANT:
            case GOOD_BEAST:
                logger.fine("Hitting on Beast " + miniSquirrel);

                miniSquirrel.updateEnergy(nextEntity.getEnergy());

                if (miniSquirrel.getEnergy() <= 0)
                    kill(miniSquirrel);

                killAndReplace(nextEntity);
                miniSquirrel.move(moveDirection);
                updateFlattenedBoard();
                break;
            case MASTER_SQUIRREL:
            case MASTER_SQUIRREL_BOT:
                logger.fine("Hitting on master " + miniSquirrel);

                MasterSquirrel masterSquirrel = (MasterSquirrel) nextEntity;

                if (masterSquirrel.isMyChild(miniSquirrel)) {
                    logger.fine("Is my daddy " + miniSquirrel);
                    logger.fine("Daddy energy before:" + masterSquirrel.getEnergy());
                    masterSquirrel.updateEnergy(miniSquirrel.getEnergy());
                    logger.fine("Daddy energy after:" + masterSquirrel.getEnergy());
                    kill(miniSquirrel);
                } else {
                    logger.fine("Is not my daddy " + miniSquirrel);
                    kill(miniSquirrel);
                }
                break;
            case MINI_SQUIRREL:
                if (miniSquirrel.getDaddy() != ((MiniSquirrel) nextEntity).getDaddy()) {
                    kill(miniSquirrel);
                    kill(nextEntity);
                }
                break;
        }
    }

    @Override
    public void tryMove(GoodBeast goodBeast, XY moveDirection) {
        XY nextPosition = goodBeast.getLocation().addVector(moveDirection);
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];

        switch (getEntityType(nextPosition)) {
            case NOTHING:
                goodBeast.move(moveDirection);
                updateFlattenedBoard();
                break;
            case MINI_SQUIRREL_BOT:
            case MASTER_SQUIRREL_BOT:
            case MASTER_SQUIRREL:
            case MINI_SQUIRREL:
                logger.fine("Hitting on Master/Mini " + goodBeast);
                nextEntity.updateEnergy(goodBeast.getEnergy());
                //killAndReplace(goodBeast);
                break;
        }
    }

    @Override
    public void tryMove(BadBeast badBeast, XY moveDirection) {
        XY nextPosition = badBeast.getLocation().addVector(moveDirection);
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];

        switch (getEntityType(nextPosition)) {
            case NOTHING:
                badBeast.move(moveDirection);
                updateFlattenedBoard();
                break;
            case MINI_SQUIRREL:
            case MINI_SQUIRREL_BOT:
                nextEntity.updateEnergy(badBeast.getEnergy());
                if (nextEntity.getEnergy() <= 0)
                    kill(nextEntity);
                badBeast.bite();
                if (badBeast.getBitesLeft() == 0)
                    killAndReplace(badBeast);
                break;
            case MASTER_SQUIRREL_BOT:
            case MASTER_SQUIRREL:
                nextEntity.updateEnergy(badBeast.getEnergy());
                badBeast.bite();
                if (badBeast.getBitesLeft() == 0)
                    killAndReplace(badBeast);
                break;
        }
    }

    @Override
    public void tryMove(MasterSquirrel masterSquirrel, XY moveDirection) {
        XY nextPosition = masterSquirrel.getLocation().addVector(moveDirection);
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];

        switch (getEntityType(nextPosition)) {
            case NOTHING:
                masterSquirrel.move(moveDirection);
                updateFlattenedBoard();
                break;
            case WALL:
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                masterSquirrel.stun();
                break;
            case BAD_BEAST:
            case GOOD_PLANT:
            case BAD_PLANT:
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                break;
            case MINI_SQUIRREL:
            case MINI_SQUIRREL_BOT:
                logger.fine("Hitting on MiniSquirrel " + masterSquirrel);
                MiniSquirrel miniSquirrel = (MiniSquirrel) nextEntity;
                int energy;
                if (masterSquirrel.isMyChild(miniSquirrel)) {
                    logger.fine("Is my child " + masterSquirrel);
                    energy = miniSquirrel.getEnergy();
                    kill(miniSquirrel);
                } else {
                    logger.fine("Is not my child " + masterSquirrel);
                    energy = board.getConfig().getCollisionPointsWithAlienMS();
                    kill(nextEntity);
                }
                masterSquirrel.updateEnergy(energy);
                masterSquirrel.move(moveDirection);
                updateFlattenedBoard();
                break;
            case GOOD_BEAST:
                logger.fine("Hitting on GoodBeast " + masterSquirrel);
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                masterSquirrel.move(moveDirection);
                updateFlattenedBoard();
                break;
        }
    }

    /**
     * Scans area with an offset of +-6 for the nearest player entity.
     *
     * @param pos the start position for the offset
     * @return the nearest player
     */
    @Override
    public Player nearestPlayerEntity(XY pos) {
        //define scan area
        int viewDistance = getPlayerViewDistance();

        int startX = pos.getX() - viewDistance;
        if (startX < 0)
            startX = 0;

        int endX = pos.getX() + viewDistance;
        if (endX >= getSize().getX())
            endX = getSize().getX() - 1;

        int startY = pos.getY() - viewDistance;
        if (startY < 0)
            startY = 0;

        int endY = pos.getY() + viewDistance;
        if (endY >= getSize().getY())
            endY = getSize().getY() - 1;

        //find nearest player
        Player nearestPlayer = null;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                Entity entity = cells[x][y];
                if (!(entity instanceof Player))
                    continue;
                if (nearestPlayer == null) {
                    nearestPlayer = (Player) entity;
                } else if (pos.distanceFrom(entity.getLocation()) < pos.distanceFrom(nearestPlayer.getLocation())) {
                    nearestPlayer = (Player) entity;
                }
            }
        }
        return nearestPlayer;
    }

    /**
     * Kill the enemy and delete it from tehe EntityList
     *
     * @param entity which will be killed
     */

    @Override
    public void kill(Entity entity) {
        logger.fine("Entity with ID: " + entity.getId() + " and Energy: " + entity.getEnergy() + " killed.");
        board.remove(entity);
        updateFlattenedBoard();
    }

    /**
     * Kill an Entity and insert the Entity again with a random position on the board.
     *
     * @param entity which will be killed and replaced
     */

    @Override
    public void killAndReplace(Entity entity) {
        board.remove(entity);
        if (entity instanceof BadBeast)
            board.insert(new BadBeast(XYsupport.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        else if (entity instanceof GoodBeast)
            board.insert(new GoodBeast(XYsupport.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        else if (entity instanceof BadPlant)
            board.insert(new BadPlant(XYsupport.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        else
            board.insert(new GoodPlant(XYsupport.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        logger.fine("Entity with ID: " + entity.getId() + " and Energy: " + entity.getEnergy() + " killed and replaced with: TODO");
        updateFlattenedBoard();
    }

    @Override
    public Entity getEntity(XY xy) {
        return cells[xy.getX()][xy.getY()];
    }

    @Override
    public void insertEntity(Entity entity) {
        board.insert(entity);
    }

    @Override
    public int getGameDurationLeft() {
        return board.getGameDurationLeft();
    }

    @Override
    public void setImplosionRadius(int radius) {
        board.setImplosionRadius(radius);
    }

    @Override
    public void addImplodingMinis(MiniSquirrel mini) {
        board.addImplodingMinis(mini);
    }

    @Override
    public void removeImplodingMinis() {
        board.removeImplodingMinis();
    }

}
