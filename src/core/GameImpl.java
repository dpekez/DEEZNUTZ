package core;

import console.ConsoleUI;
import console.GameCommandType;
import console.NotEnoughEnergyException;
import console.ScanException;
import entities.HandOperatedMasterSquirrel;
import entities.MasterSquirrel;

public class GameImpl extends Game {
    private MasterSquirrel masterSquirrel;

    public GameImpl(boolean threaded) {
        super(new State(), threaded);
        ui = new ConsoleUI(state, threaded);
        masterSquirrel = new HandOperatedMasterSquirrel(XY.generateRandomLocation(state.getBoard().getConfig().getBoardSize(), state.getBoard().getEntities()));
        state.getBoard().insertMasterSquirrel(masterSquirrel);
    }

    @Override
    protected void processInput() throws ScanException {
        MoveCommand moveCommand = ui.getCommand();
        masterSquirrel.setMoveCommand(moveCommand);
    }

    @Override
    public void render() {
        ui.render(state.flattenedBoard());
    }

    public void help() {
        for (GameCommandType commandType : GameCommandType.values()) {
            System.out.println("<" + commandType.getName() + "> - " + commandType.getHelpText());
        }
    }

    public void exit() {
        System.out.println("Bye bye");
        System.exit(0);
    }

    public void all() {
        System.out.println(state.getBoard().getEntitySet());
    }

    public int masterEnergy() {
        return state.getBoard().getMasterSquirrel().getEnergy();
    }

    public void spawnMiniSquirrel(Object[] parameters) throws NotEnoughEnergyException {
        int energy = (Integer) parameters[0];
        XY direction = new XY((Integer) parameters[1], (Integer) parameters[2]);
        MasterSquirrel daddy = state.getBoard().getMasterSquirrel();
        if (state.getBoard().getMasterSquirrel().getEnergy() >= energy) {
            state.getBoard().insertMiniSquirrel(energy, direction, daddy);
        } else {
            throw new NotEnoughEnergyException("Das MasterSquirrel hat nur " + (state.getBoard().getMasterSquirrel().getEnergy()) + " Energie");
        }
    }
}
