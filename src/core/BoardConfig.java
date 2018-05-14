package core;

public class BoardConfig {

    private final int width = 60;
    private final int height = 10;
    private final XY boardSize = new XY(width, height);
    private final int badBeastQuant = 5;
    private final int goodBeastQuant = 5;
    private final int badPlantQuant = 5;
    private final int goodPlantQuant = 5;
    private final int wallQuant = 20;
    private final int pointsOfBadMiniSquirrel = 150;
    private final int waitingTimeBeast = 4;
    private final int goodBeastViewDistance = 6;
    private final int badBestViewDistance = 6;
    private final int playerEnityViewDistance = 6;


    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    public XY getBoardSize() {
        return boardSize;
    }

    int getBadBeastQuant() {
        return badBeastQuant;
    }

    int getGoodBeastQuant() {
        return goodBeastQuant;
    }

    int getBadPlantQuant() {
        return badPlantQuant;
    }

    int getGoodPlantQuant() {
        return goodPlantQuant;
    }

    int getWallQuant() {
        return wallQuant;
    }

    int getPointsOfBadMiniSquirrel() {
        return pointsOfBadMiniSquirrel;
    }

    int getWaitingTimeBeast() {
        return waitingTimeBeast;
    }

    int getGoodBeastViewDistance() {
        return goodBeastViewDistance;
    }

    int getBadBestViewDistance() {
        return badBestViewDistance;
    }

    int getPlayerEnityViewDistance() {
        return playerEnityViewDistance;
    }
}