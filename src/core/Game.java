package core;


import console.ScanException;
import console.UI;

public abstract class Game {

    private static final int FPS = 10;
    State state;
    public UI ui;
    private boolean threaded;

    Game(State state, boolean threaded) {
        this.state = state;
        this.threaded = threaded;
    }

    public void run() throws ScanException {
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

    protected abstract void render();

    private void update() {
        state.update();
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public State getState() {
        return this.state;
    }

    protected void setState(State state) {
        this.state = state;
    }

    protected abstract void processInput() throws ScanException;
}
