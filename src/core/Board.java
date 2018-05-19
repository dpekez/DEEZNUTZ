package core;


import entities.*;


public class Board {

    private XYsupport xYsupport;
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

        //fill left and right border with walls
        for (int i = 0; i < boardConfig.getHeight(); i++) {
            insert(new Wall(new XY(0, i)));
            insert(new Wall(new XY(boardConfig.getWidth() - 1, i)));
        }

        //and now some random walls inside the walled board
        for (int i = 0; i < boardConfig.getWallQuant(); i++) {
            insert(new Wall(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getBadBeastQuant(); i++) {
            insert(new BadBeast(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getGoodBeastQuant(); i++) {
            insert(new GoodBeast(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getBadPlantQuant(); i++) {
            insert(new BadPlant(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

        for (int i = 0; i < boardConfig.getGoodPlantQuant(); i++) {
            insert(new GoodPlant(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities())));
        }

    }


    void update(EntityContext context) {
        entitySet.moveEntities(context);
    }

    public FlattenedBoard flatten() { //todo: wozu brauchen wir das?
        return new FlattenedBoard(this);
    }

    void remove(Entity e) {
        entitySet.remove(e);
    }

    void insert(Entity e) {
        entitySet.add(e);
    }

    public Entity[] getEntities() {
        return entitySet.getEntitySetArray();
    }

    BoardConfig getConfig() {
        return boardConfig;
    }

    EntitySet getEntitySet() {
        return entitySet;
    }

    void insertMasterSquirrel(MasterSquirrel masterSquirrel) {
        this.masterSquirrel = masterSquirrel;
        insert(masterSquirrel);
    }

    private void createBots() {
        for (int botsCount = 0; botsCount < boardConfig.getNumberOfBots(); botsCount++) {
            MasterSquirrel toAdd = new MasterSquirrelBot(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities()));
            insert(toAdd);
        }
    }

    public MasterSquirrel getMasterSquirrel() {
        return masterSquirrel;
    }

    void insertMiniSquirrel(int energy, XY direction, MasterSquirrel daddy) {
        XY location = masterSquirrel.getLocation().addVector(direction);
        if (masterSquirrel.getEnergy() >= energy) {
            masterSquirrel.updateEnergy(-energy);
            insert(new MiniSquirrel(energy, location, daddy));
        }
    }

    public void insertMiniSquirrelBot(int energy, XY direction, MasterSquirrel daddy) {
        XY location = masterSquirrel.getLocation().addVector(direction);
        if (masterSquirrel.getEnergy() >= energy) {
            masterSquirrel.updateEnergy(-energy);
            insert(new MiniSquirrelBot(energy, location, daddy));
        }
    }
}

