package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.core.XY;

public class BadPlant extends Entity {

    private static final int DEFAULT_ENERGY = -100;
    private static final String DEFAULT_NAME = "badplant";

    public BadPlant(XY location) {
        super(DEFAULT_ENERGY, location, DEFAULT_NAME);
    }

    @Override
    public String toString() {
        return "BadPlant{ " + super.toString() + " }";
    }
}
