import console.*;
import core.*;
import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;

import java.io.IOException;


public class GameImpl extends Game {

    protected UI ui;
    private MasterSquirrel masterSquirrel;


    GameImpl(boolean threaded) {
        super(new State(), threaded);
        ui = new ConsoleUI(state, threaded);

        masterSquirrel = new HandOperatedMasterSquirrel(XY.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities()));

        state.getBoard().insertMasterSquirrel(masterSquirrel);
    }


    @Override
    protected void processInput() throws IOException, ScanException {

        MoveCommand moveCommand;

        moveCommand = ui.getCommand();
        masterSquirrel.setMoveCommand(moveCommand);

    }

    @Override
    protected void render() {
        ui.render(state.flattenedBoard());
    }

}
