package de.hsa.games.deeznutz.core;

import java.util.logging.Level;
import java.util.logging.Logger;

public class State {
    private static final Logger logger = Logger.getLogger(State.class.getName());

    private int highScore;
    private Board board;

    State(BoardConfig boardConfig) {
        board = new Board(boardConfig);
        update();
    }

    public int getHighScore() {
        return highScore;
    }

    public Board getBoard() {
        return board;
    }

    void update() {
        logger.log(Level.FINEST, "start update from State");
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
