import console.ScanException;
import console.UI;
import core.Board;
import core.State;

import java.io.IOException;

public abstract class Game {

    protected State state;
    protected UI ui;
    public static final int FPS = 10;
    private boolean threaded;


    Game(State state, boolean threaded) {
        this.state = state;
        this.threaded = threaded;
    }


    public void run() throws IOException, ScanException {

        while (true) {
            render();
            processInput();
            update();

            if(threaded) {
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected abstract void render();

    protected abstract void processInput() throws IOException, ScanException;

    protected void update() {

        state.update();

    }

}
