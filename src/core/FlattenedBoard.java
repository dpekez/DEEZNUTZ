package core;

import entities.*;

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
    public EntityType getEntityType(XY xy) {
        return getEntityType(xy.getX(), xy.getY());
    }

    @Override
    public int getWaitingTimeBeast() {
        return board.getConfig().getWaitingTimeBeast();
    }

    @Override
    public int getGoodBeastViewDistance() {
        return board.getConfig().getGoodBeastViewDistance();
    }

    @Override
    public int getBadBeastViewDistance() {
        return board.getConfig().getBadBestViewDistance();
    }

    @Override
    public int getPlayerViewDistance() {
        return board.getConfig().getPlayerEnityViewDistance();
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
        EntityType entityType = getEntityType(moveDirection);
        //XY nextPosition = miniSquirrel.getLocation().addVector(moveDirection);
        Entity nextEntity = cells[moveDirection.getX()][moveDirection.getY()];

        switch (entityType) {
            case WALL:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                miniSquirrel.isStunnedNextRound();
                checkEnergyOfMiniSquirrel(miniSquirrel);
                break;

            case BAD_BEAST:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                ((BadBeast) nextEntity).bite();
                if (((BadBeast) nextEntity).getBitesLeft() == 0)
                    killAndReplace(nextEntity);
                break;

            case GOOD_BEAST:
            case BAD_PLANT:
            case GOOD_PLANT:
                miniSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                if (!checkEnergyOfMiniSquirrel(miniSquirrel)) {
                    miniSquirrel.move(moveDirection);
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

            default:
                checkEnergyOfMiniSquirrel(miniSquirrel);
                miniSquirrel.move(moveDirection);
        }
        miniSquirrel.updateEnergy(-1);
    }


    @Override
    public void tryMove(GoodBeast goodBeast, XY moveDirection) {
        XY nextPosition = goodBeast.getLocation().addVector(moveDirection);
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];

        if (nextEntity == null) {
            goodBeast.move(moveDirection);
            updateFlattenedBoard();
        }

        if (nextEntity instanceof Player) {
            nextEntity.updateEnergy(goodBeast.getEnergy());
            killAndReplace(goodBeast);
        }

    }

    @Override
    public void tryMove(BadBeast badBeast, XY moveDirection) {
        XY nextPosition = badBeast.getLocation().addVector(moveDirection);
        Entity nextEntity = cells[nextPosition.getX()][nextPosition.getY()];

        if (nextEntity == null) {
            badBeast.move(moveDirection);
            updateFlattenedBoard();
            return;
        }

        if (nextEntity instanceof MiniSquirrel) {
            nextEntity.updateEnergy(badBeast.getEnergy());

            if (nextEntity.getEnergy() <= 0)
                kill(nextEntity);

            badBeast.bite();

            if (badBeast.getBitesLeft() == 0)
                killAndReplace(badBeast);

        } else if (nextEntity instanceof MasterSquirrel) {
            nextEntity.updateEnergy(badBeast.getEnergy());

            badBeast.bite();

            if (badBeast.getBitesLeft() == 0)
                killAndReplace(badBeast);
        }

    }

    @Override
    public void tryMove(MasterSquirrel masterSquirrel, XY moveDirection) {
        XY mD = masterSquirrel.getLocation().addVector(moveDirection);
        EntityType entityType = getEntityType(mD);
        //XY nextPosition = masterSquirrel.getLocation().addVector(moveDirection);
        Entity nextEntity = cells[mD.getX()][mD.getY()];

        switch (entityType) {
            case WALL:
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                masterSquirrel.isStunnedNextRound();
                break;

            case BAD_BEAST:
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                ((BadBeast) nextEntity).bite();
                if (((BadBeast) nextEntity).getBitesLeft() == 0)
                    killAndReplace(nextEntity);
                break;

            case GOOD_BEAST:
            case GOOD_PLANT:
            case BAD_PLANT:
                masterSquirrel.updateEnergy(nextEntity.getEnergy());
                killAndReplace(nextEntity);
                break;

            case MINI_SQUIRREL:
                MiniSquirrel miniSquirrel = (MiniSquirrel) nextEntity;
                int energy;
                if (masterSquirrel.isMyChild(miniSquirrel)) {
                    energy = miniSquirrel.getEnergy();
                } else {
                    energy = board.getConfig().getPointsOfBadMiniSquirrel();
                }
                masterSquirrel.updateEnergy(energy);
                kill(miniSquirrel);
                masterSquirrel.move(mD);
                break;

            case MASTER_SQUIRREL:
                break;

            default:
                masterSquirrel.move(mD);
        }
    }

    private boolean checkEnergyOfMiniSquirrel(MiniSquirrel miniSquirrel) {
        if (miniSquirrel.getEnergy() <= 0) {
            kill(miniSquirrel);
            return true;
        } else {
            return false;
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
        int viewDistance = board.getConfig().getGoodBeastViewDistance();

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
                if (entity instanceof Player) {
                    if (nearestPlayer == null) {
                        nearestPlayer = (Player) entity;
                    } else if (pos.distanceFrom(entity.getLocation()) < pos.distanceFrom(nearestPlayer.getLocation())) {
                        nearestPlayer = (Player) entity;
                    }
                }
            }
        }
        return nearestPlayer;
    }

    @Override
    public void kill(Entity entity) {
        board.remove(entity);
    }

    @Override
    public void killAndReplace(Entity entity) {
        board.remove(entity);

        if (entity instanceof BadBeast)
            board.insert(new BadBeast(XY.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        else if (entity instanceof GoodBeast)
            board.insert(new GoodBeast(XY.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        else if (entity instanceof BadPlant)
            board.insert(new BadPlant(XY.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
        else
            board.insert(new GoodPlant(XY.generateRandomLocation(board.getConfig().getBoardSize(), board.getEntities())));
    }

}
