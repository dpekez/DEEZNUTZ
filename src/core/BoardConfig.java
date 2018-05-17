package core;

public class BoardConfig {

    private final int width = 80;
    private final int height = 80;
    private final XY boardSize = new XY(width, height);
    private final int badBeastQuant = 30;
    private final int goodBeastQuant = 25;
    private final int badPlantQuant = 25;
    private final int goodPlantQuant = 28;
    private final int wallQuant = 41;
    private final int pointsOfBadMiniSquirrel = 150;
    private final int waitingTimeBeast = 80;
    private final int goodBeastViewDistance = 6;
    private final int badBestViewDistance = 6;
    private final int playerEnityViewDistance = 6;

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

    int getBadBestViewDistance() {
        return badBestViewDistance;
    }

    int getGoodBeastViewDistance() {
        return goodBeastViewDistance;
    }

    int getPointsOfBadMiniSquirrel() {
        return pointsOfBadMiniSquirrel;
    }

    int getPlayerEnityViewDistance() {
        return playerEnityViewDistance;
    }
}