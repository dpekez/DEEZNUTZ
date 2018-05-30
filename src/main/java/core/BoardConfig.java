package core;

import java.io.*;
import java.util.Properties;

public class BoardConfig {

    private final String mainBotPath = "botimpls.mozartuss.BrainFactory";
    private final String secondaryBotPath = "botimpls.potato.PotatoControllerFactory";

    private final int width = 40;
    private final int height = 30;
    private final int wallQuant;
    private final int badPlantQuant;
    private final int badBeastQuant = 5;
    private final int goodBeastQuant = 5;
    private final int goodPlantQuant = 5;
    private final int waitingTimeBeast = 4;
    private final int playerEnityViewDistance = 6;
    private final int pointsOfBadMiniSquirrel = 150;
    private final XY boardSize = new XY(width, height);

    public BoardConfig(String propertiesFile) {
        Properties properties = new Properties();

        try {
            Reader reader = new FileReader(propertiesFile);
            properties.load(reader);
        } catch (IOException e){
            // todo: logger
        }

        //width = Integer.parseInt(properties.getProperty("width", "40"));
        //height = Integer.parseInt(properties.getProperty("height", "30"));
        wallQuant = Integer.parseInt(properties.getProperty("wallQuant", "10"));
        badPlantQuant = Integer.parseInt(properties.getProperty("badPlantQuant", "5"));
    }

    public void saveProperties() {
        Properties properties = new Properties();

        //properties.setProperty("width", "" + width);
        //properties.setProperty("width", "" + height);
        properties.setProperty("wallQuant", "" + wallQuant);
        properties.setProperty("badPlantQuant", "" + badPlantQuant);

        try {
            System.out.println("writing file");
            Writer writer = new FileWriter("default.properties");
            properties.store(writer, "DEEZ NUTZ");
        } catch (IOException e){
            e.printStackTrace();
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

    int getPointsOfBadMiniSquirrel() {
        return pointsOfBadMiniSquirrel;
    }

    int getPlayerEnityViewDistance() {
        return playerEnityViewDistance;
    }
}