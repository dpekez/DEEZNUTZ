package entities;

import core.EntityContext;
import core.XY;
import core.XYsupport;

public class MiniSquirrel extends Player {
    private final MasterSquirrel daddy;

    public MiniSquirrel(int energy, XY location, MasterSquirrel daddy) {
        super(energy, location);
        this.daddy = daddy;
    }

    public MasterSquirrel getDaddy() {
        return daddy;
    }

    @Override
    public void nextStep(EntityContext entityContext) {
        updateEnergy(-1);
        entityContext.tryMove(this, XYsupport.generateRandomMoveVector());
    }

    @Override
    public String toString() {
        return "MiniSquirrel{ " + super.toString() + " }";
    }
}
