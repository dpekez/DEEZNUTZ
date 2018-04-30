package entities;

import core.EntityContext;
import core.XY;

public class BadBeast extends Character {

    private static final int DEFAULT_ENERGY = -150;
    private int bitesLeft;


    public BadBeast(XY location) {
        super(DEFAULT_ENERGY, location);
        this.bitesLeft = 7;
    }


    @Override
    public void nextStep(EntityContext entityContext) {
        //todo
    }

    public void bites() {
        bitesLeft--;
    }

    public int getBitesLeft() {
        return this.bitesLeft;
    }

    @Override
    public String toString() {
        return "BadBeast{ " + super.toString() + " }";
    }

}