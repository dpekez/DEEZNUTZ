package entities;

import botapi.BotControllerFactory;
import core.MoveCommand;
import core.XY;

public abstract class MasterSquirrel extends Player {

    private BotControllerFactory factory;

    private static final int DEFAULT_ENERGY = 1000;
    MoveCommand moveCommand;

    MasterSquirrel(XY location) {
        super(DEFAULT_ENERGY, location);
    }

    @Override
    public void updateEnergy(int energy) {
        if(getEnergy() + energy >= 0)
            super.updateEnergy(energy);
        else
            super.updateEnergy(- getEnergy());
    }

    public void setMoveCommand(MoveCommand moveCommand) {
        this.moveCommand = moveCommand;
    }

    public boolean isMyChild(MiniSquirrel squirrelToCheck) {
        return this == squirrelToCheck.getDaddy();
    }

    BotControllerFactory getFactory() {
        return factory;
    }

    @Override
    public String toString() {
        return "MasterSquirrel{ " + super.toString() + '}';
    }

    public void setFactory(BotControllerFactory factory) {
        this.factory = factory;
    }
}
