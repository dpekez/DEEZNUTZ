package entities;

import core.MoveCommand;
import core.XY;

public abstract class MasterSquirrel extends Player {

    private static final int DEFAULT_ENERGY = 1000;
    private MiniSquirrel miniSquirrel;
    protected MoveCommand moveCommand;

    public MasterSquirrel(XY location) {
        super(DEFAULT_ENERGY, location);
    }


    public void setMoveCommand(MoveCommand moveCommand) {
        this.moveCommand = moveCommand;
    }

    public boolean isMyChild(MiniSquirrel squirrelToCheck) {
        return this == squirrelToCheck.getDaddy();
    }

    @Override
    public String toString() {
        return "MasterSquirrel{ " + super.toString() + '}';
    }
}
