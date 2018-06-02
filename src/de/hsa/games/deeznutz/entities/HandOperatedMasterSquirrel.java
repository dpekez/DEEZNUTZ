package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.core.EntityContext;
import de.hsa.games.deeznutz.core.XY;
import java.util.logging.Logger;

public class HandOperatedMasterSquirrel extends MasterSquirrel {

    public HandOperatedMasterSquirrel(XY location) {
        super(location);
    }

    @Override
    public void nextStep(EntityContext context) {
        super.nextStep(context);

        if (isStunned()) {
            Logger.getLogger(Launcher.class.getName()).info("MasterSquirrel is stunned");
            return;
        }

        context.tryMove(this, moveCommand.getMoveDirection());
    }

    @Override
    public String toString() {
        return "HandOperatedMasterSquirrel{ " + super.toString() + '}';
    }

}
