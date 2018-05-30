package core;

import console.ConsoleUI;
import console.ScanException;
import entities.HandOperatedMasterSquirrel;

public class GameImpl extends Game {
    private HandOperatedMasterSquirrel masterSquirrel;

    public GameImpl(boolean threaded, BoardConfig boardConfig) {
        super(new State(boardConfig));
        super.threaded = threaded;
        ui = new ConsoleUI(state, threaded);

        masterSquirrel = new HandOperatedMasterSquirrel(XYsupport.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities()));
        state.getBoard().insertMasterSquirrel(masterSquirrel);
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
