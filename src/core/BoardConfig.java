package core;

public class BoardConfig {

    private final int width = 60;
    private final int height = 10;
    private final XY boardSize = new XY(width, height);
    private final int badBeastQuant = 10;
    private final int goodBeastQuant = 10;
    private final int badPlantQuant = 10;
    private final int goodPlantQuant = 10;
    private final int wallQuant = 30;
    private final int pointsOfBadMiniSquirrel = 150;
    private final int waitingTimeBeast = 4;
    private final int goodBeastViewDistance = 6;
    private final int badBestViewDistance = 6;
    private final int playerEnityViewDistance = 6;


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public XY getBoardSize() {
        return boardSize;
    }

    public int getBadBeastQuant() {
        return badBeastQuant;
    }

    public int getGoodBeastQuant() {
        return goodBeastQuant;
    }

    public int getBadPlantQuant() {
        return badPlantQuant;
    }

    public int getGoodPlantQuant() {
        return goodPlantQuant;
    }

    public int getWallQuant() {
        return wallQuant;
    }

    public int getPointsOfBadMiniSquirrel() {
        return pointsOfBadMiniSquirrel;
    }

    public int getWaitingTimeBeast() {
        return waitingTimeBeast;
    }

    public int getGoodBeastViewDistance() {
        return goodBeastViewDistance;
    }

    public int getBadBestViewDistance() {
        return badBestViewDistance;
    }

    public int getPlayerEnityViewDistance() {
        return playerEnityViewDistance;
    }
}