package entities;

import core.EntityContext;
import core.XY;

public class BadBeast extends Character {

    private static final int DEFAULT_ENERGY = -150;
    private int bitesLeft = 7;


    public BadBeast(XY location) {
        super(DEFAULT_ENERGY, location);
    }


    @Override
    public void nextStep(EntityContext entityContext) {
        //todo
    }

    @Override
    public String toString() {
        return "BadBeast{ " + super.toString() + " }";
    }

}
