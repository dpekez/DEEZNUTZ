package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Launcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class BoardConfig {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private final int fps;
    private final int width;
    private final int height;
    private final XY boardSize;
    private final int gameMode;
    private final int playerMode;
    private final int gameDuration;
    private final int gameRounds;
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
            logger.info("Loading boardconfig from file.");
            properties.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            logger.warning("Loading boardconfig failed, using default config!");
        }

        fps = Integer.parseInt(properties.getProperty("fps", "10"));
        width = Integer.parseInt(properties.getProperty("width", "45"));
        height = Integer.parseInt(properties.getProperty("height", "30"));
        // boardSize is dynamically generated, no need to load/save from/to properties file
        boardSize = new XY(width, height);
        gameMode = Integer.parseInt(properties.getProperty("gameMode", "3"));
        playerMode = Integer.parseInt(properties.getProperty("playerMode", "3"));
        gameDuration = Integer.parseInt(properties.getProperty("gameDuration", "700"));
        gameRounds = Integer.parseInt(properties.getProperty("gameRounds", "3"));
        wallQuant = Integer.parseInt(properties.getProperty("wallQuant", "25"));
        badPlantQuant = Integer.parseInt(properties.getProperty("badPlantQuant", "12"));
        badBeastQuant = Integer.parseInt(properties.getProperty("badBeastQuant", "10"));
        goodBeastQuant = Integer.parseInt(properties.getProperty("goodBeastQuant", "12"));
        goodPlantQuant = Integer.parseInt(properties.getProperty("goodPlantQuant", "12"));
        waitingTimeBeast = Integer.parseInt(properties.getProperty("waitingTimeBeast", "4"));
        playerEntityViewDistance = Integer.parseInt(properties.getProperty("playerEntityViewDistance", "6"));
        collisionPointsWithAlienMS = Integer.parseInt(properties.getProperty("collisionPointsWithAlienMS", "150"));
        mainBotPath = properties.getProperty("mainBotPath", "de.hsa.games.deeznutz.botimpls.potato.PotatoFactory");
        secondaryBotPath = properties.getProperty("secondaryBotPath", "de.hsa.games.deeznutz.botimpls.potato.PotatoFactory");
    }

    /**
     * No real usage as of now.
     * Future usage could be when in-game setting manipulation is implemented.
     */
    @SuppressWarnings("unused")
    public void saveProperties(String propertiesFile) {
        Properties properties = new Properties();

        properties.setProperty("fps", "" + fps);
        properties.setProperty("width", "" + width);
        properties.setProperty("height", "" + height);
        properties.setProperty("gameMode", "" + gameMode);
        properties.setProperty("playerMode", "" + playerMode);
        properties.setProperty("gameDuration", "" + gameDuration);
        properties.setProperty("gameRounds", "" + gameRounds);
        properties.setProperty("wallQuant", "" + wallQuant);
        properties.setProperty("badPlantQuant", "" + badPlantQuant);
        properties.setProperty("badBeastQuant", "" + badBeastQuant);
        properties.setProperty("goodBeastQuant", "" + goodBeastQuant);
        properties.setProperty("goodPlantQuant", "" + goodPlantQuant);
        properties.setProperty("waitingTimeBeast", "" + waitingTimeBeast);
        properties.setProperty("playerEntityViewDistance", "" + playerEntityViewDistance);
        properties.setProperty("collisionPointsWithAlienMS", "" + collisionPointsWithAlienMS);
        properties.setProperty("mainBotPath", mainBotPath);
        properties.setProperty("secondaryBotPath", secondaryBotPath);

        try {
            logger.info("Writing boardconfig to file.");
            properties.store(new FileOutputStream(propertiesFile), "Properties");
        } catch (IOException e) {
            logger.warning("Writing boardconfig failed!");
        }
    }

    public int getFps() {
        return fps;
    }

    public int getGameMode() {
        return gameMode;
    }

    public int getPlayerMode() {
        return playerMode;
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

    public int getGameRounds() {
        return gameRounds;
    }

    public int getGameDuration() {
        return gameDuration;
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
