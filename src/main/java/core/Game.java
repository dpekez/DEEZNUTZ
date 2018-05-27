package core;

import console.ScanException;
import console.UI;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Game {

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final int FPS = 10;
    public UI ui;
    State state;
    private boolean threaded;

    Game(State state, boolean threaded) {
        this.state = state;
        this.threaded = threaded;
    }

    public void run() throws ScanException {

        while (true) {
            logger.log(Level.FINER, "start render()");
            render();
            logger.log(Level.FINER, "start processInput()");
            processInput();
            if (threaded) {
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.log(Level.FINER, "start update()");
            update();
        }
    }

    public UI getUi() {
        return ui;
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    protected abstract void render();

    private void update() {
        state.update();
    }

    public State getState() {
        return this.state;
    }

    protected void setState(State state) {
        this.state = state;
    }

    public abstract void processInput() throws ScanException;

}
