package core;


import botapi.BotControllerFactory;
import entities.*;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Board {

    private XYsupport xYsupport;
    private EntitySet entitySet;
    private BoardConfig boardConfig;
    private HandOperatedMasterSquirrel masterSquirrel;
    private BotControllerFactory factory;
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Board(BoardConfig boardConfig) {

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

    public void insert(Entity e) {
        entitySet.add(e);
    }

    public Entity[] getEntities() {
        return entitySet.getEntitySetArray();
    }

    public BoardConfig getConfig() {
        return boardConfig;
    }

    public EntitySet getEntitySet() {
        return entitySet;
    }

    void insertMasterSquirrel(HandOperatedMasterSquirrel masterSquirrel) {
        this.masterSquirrel = masterSquirrel;
        insert(masterSquirrel);
    }

    public void createBots() {
        for (int botsCount = 0; botsCount < boardConfig.getNumberOfBots(); botsCount++) {
            try {
                BotControllerFactory factory = (BotControllerFactory) Class.forName("botapi.botimpl.BrainFactory").newInstance();
                MasterSquirrelBot masterSquirrelBot = new MasterSquirrelBot(XYsupport.generateRandomLocation(boardConfig.getBoardSize(), getEntities()), factory);
                insert(masterSquirrelBot);
            } catch (ClassNotFoundException e) {
                logger.log(Level.WARNING, "factory wurde nicht gefunden");
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }

        }
    }

    public MasterSquirrel getMasterSquirrel() {
        return masterSquirrel;
    }

    public void insertMiniSquirrel(int energy, XY direction, MasterSquirrel daddy) {
        XY location = masterSquirrel.getLocation().addVector(direction);
        if (masterSquirrel.getEnergy() >= energy) {
            masterSquirrel.updateEnergy(-energy);
            insert(new MiniSquirrel(energy, location, daddy));
        }
    }

}

