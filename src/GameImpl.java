import console.*;
import core.State;
import core.XY;
import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;
import entities.MiniSquirrel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    protected void processInput() throws ScanException ,IOException  {
        CommandScanner commandScanner = new CommandScanner(GameCommandType.values(), new BufferedReader(new InputStreamReader(System.in)));

        Command command = commandScanner.next();

        Object[] params = command.getParams();

    }

    public void spawnMiniSquirrel(){
        System.out.println("Spawn MiniSquirrel");
        int energy = 200;
        MiniSquirrel miniSquirrel = new MiniSquirrel(energy, (XY.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities())),masterSquirrel.isMyChild(this.spawnMiniSquirrel()));
    }

    @Override
    protected void render() {
        ui.render(state.flattenedBoard());
    }
}
