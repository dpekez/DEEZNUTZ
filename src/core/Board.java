package core;


import entities.*;


public class Board {

    private EntitySet entitySet;
    private BoardConfig boardConfig;
    private MasterSquirrel masterSquirrel;

    Board(BoardConfig boardConfig) {
        this.boardConfig = boardConfig;
        entitySet = new EntitySet(boardConfig.getHeight() * boardConfig.getWidth());

        //fill upper and bottom border with walls
        for (int i = 0; i < boardConfig.getWidth(); i++) {
            insert(new Wall(new XY(i, 0)));
            insert(new Wall(new XY(i, boardConfig.getHeight() - 1)));
        }

        for (int i = 0; i < boardConfig.getHeight(); i++) {
            insert(new Wall(new XY(0, i)));
            insert(new Wall(new XY(boardConfig.getWidth() - 1, i)));
        }

        for (int i = 0; i < boardConfig.getWallQuant(); i++) {
            insert(new Wall(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getBadBeastQuant(); i++) {
            insert(new BadBeast(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getGoodBeastQuant(); i++) {
            insert(new GoodBeast(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getBadPlantQuant(); i++) {
            insert(new BadPlant(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getGoodPlantQuant(); i++) {
            insert(new GoodPlant(XY.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }
    }

    void insert(Entity e) {
        entitySet.add(e);
    }

    void remove(Entity e) {
        entitySet.remove(e);
    }

    EntitySet getEntitySet() {
        return entitySet;
    }

    BoardConfig getConfig() {
        return boardConfig;
    }

    public MasterSquirrel getMasterSquirrel() {
        return masterSquirrel;
    }

    public Entity[] getEntities() {
        return entitySet.getEntitySetArray();
    }

    void update(EntityContext context) {
        entitySet.moveEntities(context);
    }

    void insertMasterSquirrel(MasterSquirrel masterSquirrel) {
        this.masterSquirrel = masterSquirrel;
        insert(masterSquirrel);
    }

    void insertMiniSquirrel(int energy, XY direction, MasterSquirrel daddy) {
        XY location = masterSquirrel.getLocation().addVector(direction);
        if (masterSquirrel.getEnergy() >= energy) {
            masterSquirrel.updateEnergy(-energy);
            insert(new MiniSquirrel(energy, location, daddy));
        }
    }
}
