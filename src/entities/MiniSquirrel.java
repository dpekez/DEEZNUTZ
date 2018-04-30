package entities;

import core.EntityContext;
import core.XY;

public class MiniSquirrel extends Player {

    private final int masterSquirrelID;

    public MiniSquirrel(int energy, XY location, int masterSquirrelID) {
        super(energy, location);
        this.masterSquirrelID = masterSquirrelID;
    }


    public int getMasterSquirrelID() {
        return masterSquirrelID;
    }

    @Override
    public void nextStep(EntityContext entityContext) {
        //todo
    }

    @Override
    public String toString() {
        return "MiniSquirrel{ " + super.toString() + " }";
    }

}
