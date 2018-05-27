package core;


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
