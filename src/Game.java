import console.ScanException;
import console.UI;
import core.State;

import java.io.IOException;

public abstract class Game {

    private static final int FPS = 1;
    State state;
    UI ui;
    private boolean threaded;


    Game(State state, boolean threaded) {
        this.state = state;
        this.threaded = threaded;
    }

    void run() throws IOException, ScanException {

        while (true) {
            render();
            processInput();
            if(threaded) {
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            update();
        }
    }

    protected abstract void render();

    protected abstract void processInput() throws IOException, ScanException;

    private void update() {
        state.update();
    }
}
