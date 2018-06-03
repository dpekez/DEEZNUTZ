package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.entities.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FlattenedBoard implements BoardView, EntityContext {
    private Board board;
    private Entity[][] cells;

    FlattenedBoard(Board board) {
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
    public XY getSize() {
        return board.getConfig().getBoardSize();
    }

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
                miniSquirrel.isStunnedNextRound();
                break;
            case GOOD_PLANT:
            case BAD_PLANT:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                if (miniSquirrel.getEnergy() <= 0)
                    kill(miniSquirrel);
                break;
            case BAD_BEAST:
            case GOOD_BEAST:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                if (miniSquirrel.getEnergy() <= 0) {
                    kill(miniSquirrel);
                } else {
                    miniSquirrel.move(moveDirection);
                    updateFlattenedBoard();
                }
                break;
            case MASTER_SQUIRREL:
                MasterSquirrel masterSquirrel = (MasterSquirrel) nextEntity;

                if (masterSquirrel.isMyChild(miniSquirrel)) {
                    masterSquirrel.updateEnergy(miniSquirrel.getEnergy());
                    kill(miniSquirrel);
                } else {
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
                Logger.getLogger(Launcher.class.getName()).fine("Hitting on Master/Mini " + goodBeast);
                nextEntity.updateEnergy(goodBeast.getEnergy());
                killAndReplace(goodBeast);
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
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                ((BadBeast) nextEntity).bite();
                if (((BadBeast) nextEntity).getBitesLeft() == 0)
                    killAndReplace(nextEntity);
                break;
            case GOOD_PLANT:
            case BAD_PLANT:
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                break;
            case MINI_SQUIRREL:
                Logger.getLogger(Launcher.class.getName()).fine("Hitting on MiniSquirrel " + masterSquirrel);
                MiniSquirrel miniSquirrel = (MiniSquirrel) nextEntity;
                int energy;
                if (masterSquirrel.isMyChild(miniSquirrel)) {
                    Logger.getLogger(Launcher.class.getName()).fine("Is my child " + masterSquirrel);
                    energy = miniSquirrel.getEnergy();
                    kill(miniSquirrel);
                } else {
                    Logger.getLogger(Launcher.class.getName()).fine("Is not my child " + masterSquirrel);
                    energy = board.getConfig().getCollisionPointsWithAlienMS();
                }
                masterSquirrel.updateEnergy(energy);
                masterSquirrel.move(moveDirection);
                updateFlattenedBoard();
                break;
            case GOOD_BEAST:
                Logger.getLogger(Launcher.class.getName()).fine("Hitting on GoodBeast " + masterSquirrel);
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

    @Override
    public void kill(Entity entity) {
        board.remove(entity);
        updateFlattenedBoard();
    }

    @Override
    public void killAndReplace(Entity entity) {
        Logger.getLogger(Launcher.class.getName()).fine("Remove and replace " + entity);
        board.remove(entity);
        if (entity instanceof BadBeast)
            board.insert(new BadBeast(XYsupport.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        else if (entity instanceof GoodBeast)
            board.insert(new GoodBeast(XYsupport.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        else if (entity instanceof BadPlant)
            board.insert(new BadPlant(XYsupport.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        else
            board.insert(new GoodPlant(XYsupport.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
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
}
