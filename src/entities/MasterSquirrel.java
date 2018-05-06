package entities;

import core.MoveCommand;
import core.XY;

public abstract class MasterSquirrel extends Player {

    private static final int DEFAULT_ENERGY = 1000;
    private MiniSquirrel miniSquirrel;


    public MasterSquirrel(XY location) {
        super(DEFAULT_ENERGY, location);
    }

    @Override
    public void updateEnergy(int energy) {
        if(getEnergy() + energy >= 0)
            super.updateEnergy(energy);
        else
            super.updateEnergy(- getEnergy());
    }


    public boolean isMyChild(MiniSquirrel squirrelToCheck) {
        return this == squirrelToCheck.getDaddy();
    }

    @Override
    public String toString() {
        return "MasterSquirrel{ " + super.toString() + '}';
    }
}
