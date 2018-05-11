import console.*;
import core.*;
import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameImpl extends Game {

    private UI ui;
    private MasterSquirrel masterSquirrel;
    private Board board;


    GameImpl() {
        super(new State());
        ui = new ConsoleUI();
        masterSquirrel = new HandOperatedMasterSquirrel(XY.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities()));
        state.insertMaster(masterSquirrel);
    }


    @Override
    protected void processInput() throws IOException, ScanException {
        CommandScanner commandScanner = new CommandScanner(GameCommandType.values(), new BufferedReader(new InputStreamReader(System.in)));
        MoveCommand moveCommand;
        moveCommand = ui.getCommand();

        masterSquirrel.setMoveCommand(moveCommand);

    }

    @Override
    protected void render() {
        ui.render(state.flattenedBoard());
    }

}
