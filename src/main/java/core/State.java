package core;


import java.util.logging.Level;
import java.util.logging.Logger;

public class State {

    private int highScore;
    private Board board;

    State() {
        board = new Board(new BoardConfig());
        update();
    }

    public int getHighScore() {
        return highScore;
    }

    public Board getBoard() {
        return board;
    }

    void update() {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.log(Level.FINER, "start update() from State");
        board.update(flattenedBoard());


        if (board.getMasterSquirrel() != null) {
            if (board.getMasterSquirrel().getEnergy() > highScore)
                highScore = board.getMasterSquirrel().getEnergy();
        }
    }

    FlattenedBoard flattenedBoard() {
        return new FlattenedBoard(board);
    }
}
