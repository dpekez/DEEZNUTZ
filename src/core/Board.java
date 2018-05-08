package core;

import entities.*;

public class Board {

    private int entityCount;
    private EntitySet entitySet;
    private BoardConfig boardConfig;


    public Board(BoardConfig boardConfig) {
        this.boardConfig = boardConfig;
        entitySet = new EntitySet(boardConfig.getHeight() * boardConfig.getWidth());

        //fill upper and bottom border with walls
        for(int i=0; i < boardConfig.getWidth(); i++) {
            insert(new Wall(new XY(i, 0)));
            insert(new Wall(new XY(i, boardConfig.getHeight()-1)));
        }

        //fill left and right border with walls
        for(int i=0; i < boardConfig.getHeight(); i++) {
            insert(new Wall(new XY(0, i)));
            insert(new Wall(new XY(boardConfig.getWidth()-1, i)));
        }

        //and now some random walls inside the walled board
        for(int i=0; i < boardConfig.getWallQuant(); i++) {
            insert(new Wall(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for(int i=0; i < boardConfig.getBadBeastQuant(); i++) {
            insert(new BadBeast(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for(int i=0; i < boardConfig.getGoodBeastQuant(); i++) {
            insert(new GoodBeast(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for(int i=0; i < boardConfig.getBadPlantQuant(); i++) {
            insert(new BadPlant(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for(int i=0; i < boardConfig.getGoodPlantQuant(); i++) {
            insert(new GoodPlant(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

    }


    public void update(EntityContext context) {
        entitySet.moveEntities(context);
    }

    public FlattenedBoard flatten() {
        return new FlattenedBoard(this);
    }

    public void remove(Entity e) {
        entitySet.remove(e);
        entityCount--;
    }

    public void insert(Entity e) {
        entitySet.add(e);
        entityCount++;
    }

    public Entity[] getEntities() {
        return entitySet.getEntitySetArray();
    }

    public BoardConfig getConfig() {
        return boardConfig;
    }

    public int getEntityCount() {
        return entityCount;
    }

    public EntitySet getEntitySet() {
        return entitySet;
    }

    @Override
    public String toString() {
        return "Board{" +
                "entityCount=" + entityCount +
                '}';
    }

}

