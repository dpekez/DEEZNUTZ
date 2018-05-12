package core;


public class State {

    private int highScore;
    private Board board;


    public State() {
        board = new Board(new BoardConfig());
    }


    public int getHighScore() {
        return highScore;
    }

    public Board getBoard() {
        return board;
    }

    public void update() {
        board.update(flattenedBoard());

        if (board.getMasterSquirrel() != null) {
            if (board.getMasterSquirrel().getEnergy() > highScore)
                highScore = board.getMasterSquirrel().getEnergy();
        }

    }

    public FlattenedBoard flattenedBoard() {
        return new FlattenedBoard(board);
    }
}
