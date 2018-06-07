package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Game;
import de.hsa.games.deeznutz.console.ConsoleUI;
import de.hsa.games.deeznutz.console.ScanException;

public class GameImpl extends Game {

    public GameImpl(boolean threaded, BoardConfig boardConfig) {
        super(boardConfig);
        super.threaded = threaded;
        ui = new ConsoleUI(state, threaded);
    }

    @Override
    public void processInput() throws ScanException {
            MoveCommand moveCommand = ui.getCommand();
            state.getBoard().getMainMasterSquirrel().setMoveCommand(moveCommand);
    }

    @Override
    public void render() {
        ui.render(state.flattenedBoard());
    }

    @Override
    public String message() {
        return "Player Energy: " + state.getBoard().getMainMasterSquirrel().getEnergy();
    }

}
