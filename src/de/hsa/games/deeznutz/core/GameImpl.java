package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Game;
import de.hsa.games.deeznutz.console.ConsoleUI;
import de.hsa.games.deeznutz.console.ScanException;
import de.hsa.games.deeznutz.entities.HandOperatedMasterSquirrel;

public class GameImpl extends Game {
    private HandOperatedMasterSquirrel masterSquirrel;

    public GameImpl(boolean threaded, BoardConfig boardConfig) {
        super(new State(boardConfig));
        super.threaded = threaded;
        ui = new ConsoleUI(state, threaded);

        masterSquirrel = new HandOperatedMasterSquirrel(XYsupport.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities()));
        state.getBoard().insertHandOperatedMasterSquirrel(masterSquirrel);
    }

    @Override
    public void processInput() throws ScanException {
            MoveCommand moveCommand = ui.getCommand();
            masterSquirrel.setMoveCommand(moveCommand);
    }

    @Override
    public void render() {
        ui.render(state.flattenedBoard());
    }

    @Override
    public String message() {
        return "Player Energy: " + masterSquirrel.getEnergy();
    }

}
