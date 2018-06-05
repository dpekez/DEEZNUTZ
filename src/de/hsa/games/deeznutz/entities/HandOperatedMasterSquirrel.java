package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.core.EntityContext;
import de.hsa.games.deeznutz.core.XY;
import java.util.logging.Logger;

public class HandOperatedMasterSquirrel extends MasterSquirrel {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    public HandOperatedMasterSquirrel(XY location, String name) {
        super(location, name);
    }

    @Override
    public void nextStep(EntityContext context) {
        super.nextStep(context);

        if (isStunned()) {
            logger.info("MasterSquirrel is stunned");
            return;
        }

        context.tryMove(this, moveCommand.getMoveDirection());
    }

    @Override
    public String toString() {
        return "HandOperatedMasterSquirrel{ " + super.toString() + '}';
    }

}
