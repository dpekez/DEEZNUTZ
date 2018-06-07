package de.hsa.games.deeznutz;

import de.hsa.games.deeznutz.console.ScanException;
import de.hsa.games.deeznutz.core.State;
import java.util.logging.Logger;

public abstract class Game {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private int fps;
    public UI ui;
    public State state;
    public boolean threaded;

    public Game(State state) {
        this.state = state;
        fps = Launcher.boardConfig.getFps();
    }

    public void run() throws ScanException {

        while (true) {
            logger.finest("start render()");
            render();
            logger.finest("start processInput()");
            processInput();

            if (threaded) {
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.finest("start update()");
            //state.getBoard().removeImplodingMinis();

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

    public void increaseFps(int step) {
        fps += step;
    }

    public void decreaseFps(int step) {
        if (fps - step <= 0)
            fps = 1;
        else
            fps -= step;
    }

    public int getFps() {
        return fps;
    }

}
