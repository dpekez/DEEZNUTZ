import core.Board;
import core.State;

public abstract class Game {

    protected State state;
    private Board board;

    public Game(State state) {
        this.state = state;
    }

    public void run() {
        while (true) {
            render();
            processInput();
            update();
        }
    }

    protected abstract void render();

    protected abstract void processInput();

    protected void update() {
        state.update();
    }

}
