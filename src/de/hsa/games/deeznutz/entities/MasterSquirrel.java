package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.botapi.BotControllerFactory;
import de.hsa.games.deeznutz.core.MoveCommand;
import de.hsa.games.deeznutz.core.XY;

public abstract class MasterSquirrel extends Player {

    private static final int DEFAULT_ENERGY = 1000;
    MoveCommand moveCommand;
    private BotControllerFactory factory;

    MasterSquirrel(XY location) {
        super(DEFAULT_ENERGY, location);

    }

    @Override
    public void updateEnergy(int energy) {
        if (getEnergy() + energy >= 0)
            super.updateEnergy(energy);
        else
            super.updateEnergy(-getEnergy());
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

    void setFactory(BotControllerFactory factory) {
        this.factory = factory;
    }

    @Override
    public String toString() {
        return "MasterSquirrel{ " + super.toString() + '}';
    }
}
