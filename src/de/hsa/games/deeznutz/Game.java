package de.hsa.games.deeznutz;

import de.hsa.games.deeznutz.console.ScanException;
import de.hsa.games.deeznutz.core.State;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Game {

    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private static final int FPS = 10;
    public UI ui;
    public State state;
    public boolean threaded;

    public Game(State state) {
        this.state = state;
    }

    public void run() throws ScanException {

        while (true) {
            logger.finest("start render()");
            render();
            logger.finest("start processInput()");
            processInput();

            if (threaded) {
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.finest("start update()");
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
