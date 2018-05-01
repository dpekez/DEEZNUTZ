package core;

import entities.*;

public class Board {

    private int entityCount; //todo: implement an entity counter
    private EntitySet entitySet;
    private BoardConfig boardConfig;


    public Board(BoardConfig boardConfig) {
        this.boardConfig = boardConfig;
        entitySet = new EntitySet(boardConfig.getHeight() * boardConfig.getWidth());

        //fill upper and bottom border with walls
        for(int i=0; i < boardConfig.getWidth(); i++) {
            entitySet.add(new Wall(new XY(i, 0)));
            entitySet.add(new Wall(new XY(i, boardConfig.getHeight()-1)));
        }

        //fill left and right border with walls
        for(int i=0; i < boardConfig.getHeight(); i++) {
            entitySet.add(new Wall(new XY(0, i)));
            entitySet.add(new Wall(new XY(boardConfig.getWidth()-1, i)));
        }

        //and now some random walls inside the walled board
        //todo: (low priority) algorithm to get connected walls
        for(int i=0; i < boardConfig.getWallQuant(); i++) {
            entitySet.add(new Wall(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for(int i=0; i < boardConfig.getBadBeastQuant(); i++) {
            entitySet.add(new BadBeast(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for(int i=0; i < boardConfig.getGoodBeastQuant(); i++) {
            entitySet.add(new GoodBeast(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for(int i=0; i < boardConfig.getBadPlantQuant(); i++) {
            entitySet.add(new BadPlant(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for(int i=0; i < boardConfig.getGoodPlantQuant(); i++) {
            entitySet.add(new GoodPlant(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
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
    }

    public void insert(Entity e) {
        entitySet.add(e);
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

    @Override
    public String toString() {
        return "Board{" +
                "entityCount=" + entityCount +
                '}';
    }

}

