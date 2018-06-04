package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Launcher;
import java.util.Map;
import java.util.logging.Logger;

public class State {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private Board board;
    private Map<String, Integer> highScore;

    public State(BoardConfig boardConfig) {
        board = new Board(boardConfig);
    }

    public Board getBoard() {
        return board;
    }

    public void update() {
        logger.finest("Update FlattenedBoard.");
        board.update(flattenedBoard());
    }

    FlattenedBoard flattenedBoard() {
        return new FlattenedBoard(board);
    }

}
