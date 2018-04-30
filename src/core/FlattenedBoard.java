package core;

import entities.*;

public class FlattenedBoard implements BoardView, EntityContext {

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

        for (Entity entity: entityArray) {
            int x, y;

            if(entity != null) {
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
        //todo
    }

    @Override
    public void tryMove(GoodBeast goodBeast, XY moveDirection) {
        //todo
    }


    @Override
    public void tryMove(BadBeast badBeast, XY moveDirection) {
        //todo
    }


    @Override
    public void tryMove(MasterSquirrel masterSquirrel, XY moveDirection) {
        //todo
    }


    /**
     * Scans area with an offset of +-6 for the nearest player entity.
     *
     * @param pos   the start position for the offset
     * @return      the nearest player
     */

    @Override
    public Player nearestPlayerEntity(XY pos) {

        //define scan area
        int viewDistance = 6;

        int startX = pos.getX() - viewDistance;
        if (startX < 0)
            startX = 0;

        int endX = pos.getX() + viewDistance;
        if (endX >= getSize().getX())
            endX = getSize().getX()-1;

        int startY = pos.getY() - viewDistance;
        if (startY < 0)
            startY = 0;

        int endY = pos.getY() + viewDistance;
        if (endY >= getSize().getY())
            endY = getSize().getY()-1;


        //find nearest player
        Player player = null;

        for(int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {

                Entity entity = cells[x][y];

                if (entity instanceof Player) {
                    if (player == null) {
                        player = (Player) entity;
                    } else if (pos.distanceFrom(entity.getLocation()) < pos.distanceFrom(player.getLocation())) {
                        player = (Player) entity;
                    }
                }

            }
        }

        return player;
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
