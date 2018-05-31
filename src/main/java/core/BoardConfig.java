package core;

import java.io.*;
import java.util.Properties;

public class BoardConfig {

    private final int width;
    private final int height;
    private final XY boardSize;
    private final int wallQuant;
    private final int badPlantQuant;
    private final int badBeastQuant;
    private final int goodBeastQuant;
    private final int goodPlantQuant;
    private final int waitingTimeBeast;
    private final int playerEntityViewDistance;
    private final int collisionPointsWithAlienMS;
    private final String mainBotPath;
    private final String secondaryBotPath;

    public BoardConfig(String propertiesFile) {
        Properties properties = new Properties();

        try {
            Reader reader = new FileReader(propertiesFile);
            properties.load(reader);
        } catch (IOException e){
            // todo: logger
        }

        width = Integer.parseInt(properties.getProperty("width", "45"));
        height = Integer.parseInt(properties.getProperty("height", "30"));
        // boardSize dynamically generated, no need to save it in properties file
        boardSize = new XY(width, height);
        wallQuant = Integer.parseInt(properties.getProperty("wallQuant", "10"));
        badPlantQuant = Integer.parseInt(properties.getProperty("badPlantQuant", "5"));
        badBeastQuant = Integer.parseInt(properties.getProperty("badBeastQuant", "5"));
        goodBeastQuant = Integer.parseInt(properties.getProperty("goodBeastQuant", "5"));
        goodPlantQuant = Integer.parseInt(properties.getProperty("goodPlantQuant", "5"));
        waitingTimeBeast = Integer.parseInt(properties.getProperty("waitingTimeBeast", "4"));
        playerEntityViewDistance = Integer.parseInt(properties.getProperty("playerEntityViewDistance", "6"));
        collisionPointsWithAlienMS = Integer.parseInt(properties.getProperty("collisionPointsWithAlienMS", "150"));
        mainBotPath = properties.getProperty("mainBotPath", "botimpls.mozartuss.BrainFactory");
        secondaryBotPath = properties.getProperty("secondaryBotPath", "botimpls.potato.PotatoControllerFactory");
    }

    public void saveProperties() {
        Properties properties = new Properties();

        properties.setProperty("width", "" + width);
        properties.setProperty("height", "" + height);
        properties.setProperty("wallQuant", "" + wallQuant);
        properties.setProperty("badPlantQuant", "" + badPlantQuant);
        properties.setProperty("badBeastQuant", "" + badBeastQuant);
        properties.setProperty("goodBeastQuant", "" + goodBeastQuant);
        properties.setProperty("goodPlantQuant", "" + goodPlantQuant);
        properties.setProperty("waitingTimeBeast", "" + waitingTimeBeast);
        properties.setProperty("playerEntityViewDistance", "" + playerEntityViewDistance);
        properties.setProperty("collisionPointsWithAlienMS", "" + collisionPointsWithAlienMS);
        properties.setProperty("botimpls.mozartuss.BrainFactory", mainBotPath);
        properties.setProperty("botimpls.potato.PotatoControllerFactory", secondaryBotPath);

        try {
            System.out.println("writing file");
            Writer writer = new FileWriter("default.properties");
            properties.store(writer, "DEEZ NUTZ");
        } catch (IOException e){
            //todo: logger
        }
    }

    public String getMainBotPath() {
        return mainBotPath;
    }

    String getSecondaryBotPath() {
        return secondaryBotPath;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    int getWallQuant() {
        return wallQuant;
    }

    public XY getBoardSize() {
        return boardSize;
    }

    int getBadBeastQuant() {
        return badBeastQuant;
    }

    int getBadPlantQuant() {
        return badPlantQuant;
    }

    int getGoodBeastQuant() {
        return goodBeastQuant;
    }

    int getGoodPlantQuant() {
        return goodPlantQuant;
    }

    int getWaitingTimeBeast() {
        return waitingTimeBeast;
    }

    int getCollisionPointsWithAlienMS() {
        return collisionPointsWithAlienMS;
    }

    int getPlayerEntityViewDistance() {
        return playerEntityViewDistance;
    }
}