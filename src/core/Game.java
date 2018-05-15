package core;

import console.ScanException;
import console.UI;

import java.io.IOException;

public abstract class Game {

    private static final int FPS = 1;
    State state;
    public UI ui;
    private boolean threaded;


    Game(State state, boolean threaded) {
        this.state = state;
        this.threaded = threaded;
    }

    public void run() throws IOException, ScanException {

        while (true) {
            render();
            processInput();
            if (threaded) {
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            update();
        }
    }

    public UI getUi() {
        return ui;
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public State getState() {
        return this.state;
    }

    protected abstract void render();

    protected abstract void processInput() throws IOException, ScanException;

    public void update() {
        state.update();
    }
}
