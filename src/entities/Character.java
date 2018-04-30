package entities;

import core.EntityContext;
import core.XY;

public abstract class Character extends Entity {

    public Character(int energy, XY xy) {
        super(energy, xy);
    }

    public abstract void nextStep(EntityContext entityContext);

}
