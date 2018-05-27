package core;

public class BoardConfig {

    private final int width = 40;
    private final int height = 30;
    private final int wallQuant = 10;
    private final int numberOfBots = 1;
    private final int badPlantQuant = 5;
    private final int badBeastQuant = 5;
    private final int goodBeastQuant = 5;
    private final int goodPlantQuant = 5;
    private final int waitingTimeBeast = 4;
    private final int playerEnityViewDistance = 6;
    private final int pointsOfBadMiniSquirrel = 150;
    private final XY boardSize = new XY(width, height);

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

    int getNumberOfBots() {
        return numberOfBots;
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