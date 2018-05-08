package entities;

import core.EntityContext;
import core.XY;


public class HandOperatedMasterSquirrel extends MasterSquirrel {

    public HandOperatedMasterSquirrel(XY location) {
        super(location);
    }

    @Override
    public void nextStep(EntityContext context) {
        System.out.println(this);
        super.nextStep(context);

        if (isStunned())
            return;

        context.tryMove(this, moveCommand.getMoveDirection());

    }

    @Override
    public String toString() {
        return "HandOperatedMasterSquirrel{ " + super.toString() + '}';
    }

}
