import console.ScanException;
import core.Board;
import core.State;

import java.io.IOException;

public abstract class Game {

    protected State state;
    private Board board;

    public Game(State state) {
        this.state = state;
    }

    public void run() throws IOException, ScanException {
        while (true) {
            render();
            processInput();
            update();
        }
    }

    protected abstract void render();

    protected abstract void processInput() throws IOException, ScanException;

    protected void update() {
        state.update();
    }

}
