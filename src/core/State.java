package core;

import entities.MasterSquirrel;
import entities.MiniSquirrel;

public class State {

    private int highScore;
    private Board board;
    private MasterSquirrel masterSquirrel;

    public State() {
        board = new Board(new BoardConfig());
    }

    public void insertMaster(MasterSquirrel masterSquirrel) {
        this.masterSquirrel = masterSquirrel;
        board.insert(masterSquirrel);
    }


    public int getHighScore() {
        return highScore;
    }

    public Board getBoard() {
        return board;
    }

    public void update() {
        board.update(flattenedBoard());

        if (masterSquirrel != null) {
            if (masterSquirrel.getEnergy() > highScore)
                highScore = masterSquirrel.getEnergy();
        }

    }

    public FlattenedBoard flattenedBoard() {
        return new FlattenedBoard(board);
    }
}
