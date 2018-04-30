package entities;

import core.XY;

public class Wall extends Entity {

    private static final int DEFAULT_ENERGY = -10;


    public Wall(XY location) {
        super(DEFAULT_ENERGY, location);
    }


    @Override
    public String toString() {
        return "Wall{ " + super.toString() + " }";
    }

}

