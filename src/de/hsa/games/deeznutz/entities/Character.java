package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.core.EntityContext;
import de.hsa.games.deeznutz.core.XY;

public abstract class Character extends Entity {

    Character(int energy, XY xy) {
        super(energy, xy);
    }

    public abstract void nextStep(EntityContext entityContext);
}
