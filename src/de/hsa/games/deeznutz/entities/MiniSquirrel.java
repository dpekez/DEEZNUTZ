package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.core.EntityContext;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

public class MiniSquirrel extends Player {
    private final MasterSquirrel daddy;
    private static final String DEFAULT_NAME = "minisquirrel";

    public MiniSquirrel(int energy, XY location, MasterSquirrel daddy) {
        super(energy, location, DEFAULT_NAME);
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
        return "MiniSquirrel{ daddyID=" + daddy.getId() + " " + super.toString() + " }";
    }
}
