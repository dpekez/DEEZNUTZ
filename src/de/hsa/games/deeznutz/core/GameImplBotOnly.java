package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Game;
import de.hsa.games.deeznutz.console.ConsoleUI;

public class GameImplBotOnly extends Game {

    public GameImplBotOnly(boolean threaded, BoardConfig boardConfig) {
        super(boardConfig);
        super.threaded = threaded;
        ui = new ConsoleUI(state, threaded);
    }

    @Override
    public void processInput() {
        // Bot only implementation, no user interaction
    }

    @Override
    public void render() {
        ui.render(state.flattenedBoard());
    }

    @Override
    public String message() {
        return state.getBoard().getMainMasterSquirrel().getName() + " Energy: " + state.getBoard().getMainMasterSquirrel().getEnergy() + "\n" +
                state.getBoard().getSecondaryMasterSquirrel().getName() + " Energy: " + state.getBoard().getSecondaryMasterSquirrel().getEnergy();
    }

}
