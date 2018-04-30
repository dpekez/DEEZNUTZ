package entities;

import core.EntityContext;
import core.XY;

public class GoodBeast extends Character {

    private static final int DEFAULT_ENERGY = 200;


    public GoodBeast(XY location) {
        super(DEFAULT_ENERGY, location);
    }

    @Override
    public void nextStep(EntityContext entityContext) {
        //todo
    }

    @Override
    public String toString() {
        return "GoodBeast{ " + super.toString() + " }";
    }

}

