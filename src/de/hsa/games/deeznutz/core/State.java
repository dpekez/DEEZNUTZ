package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Launcher;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class State {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private Map<String, Integer> hs;
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

    public void update() {
        logger.finest("update FlattenedBoard");
        board.update(flattenedBoard());

        if (board.getMasterSquirrel() != null) {
            if (board.getMasterSquirrel().getEnergy() > highScore)
                logger.finest("update HighScore");
                highScore = board.getMasterSquirrel().getEnergy();
        }
    }

    FlattenedBoard flattenedBoard() {
        return new FlattenedBoard(board);
    }
}
