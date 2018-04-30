import console.ConsoleUI;
import console.UI;
import core.MoveCommand;
import core.State;
import core.XY;
import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;

public class GameImpl extends Game {

    private UI ui;
    private MasterSquirrel masterSquirrel;

    public GameImpl() {
        super(new State());
        ui = new ConsoleUI();
        masterSquirrel = new HandOperatedMasterSquirrel(XY.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities()));
        state.insertMaster(masterSquirrel);
    }


    @Override
    protected void processInput() {
        MoveCommand cmd;

        do {
            cmd = ui.getMoveCommand();
        } while (cmd == null);

        masterSquirrel.setMoveCommand(cmd);

    }

    @Override
    protected void render() {
        ui.render(state.flattenedBoard());
    }
}
