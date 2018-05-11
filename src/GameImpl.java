import console.*;
import core.*;
import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;

import java.io.IOException;


public class GameImpl extends Game {

    private UI ui;
    private MasterSquirrel masterSquirrel;


    GameImpl() {
        super(new State());
        ui = new ConsoleUI();
        masterSquirrel = new HandOperatedMasterSquirrel(XY.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities()));
        state.insertMaster(masterSquirrel);
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
