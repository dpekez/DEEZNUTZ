package core;

import console.ScanException;
import console.UI;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Game {

    final static Logger logger = Logger.getLogger(Game.class.getName());

    private static final int FPS = 10;
    public UI ui;
    State state;
    public boolean threaded;

    Game(State state) {
        this.state = state;
    }

    public void run() throws ScanException {

        while (true) {
            logger.log(Level.FINEST, "start render()");
            render();
            logger.log(Level.FINEST, "start processInput()");
            processInput();

            if (threaded) {
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.log(Level.FINEST, "start update()");
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

    public abstract String message();

}
