package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.core.XY;

public class BadPlant extends Entity {

    private static final int DEFAULT_ENERGY = -100;

    public BadPlant(XY location) {
        super(DEFAULT_ENERGY, location);
    }

    @Override
    public String toString() {
        return "BadPlant{ " + super.toString() + " }";
    }
}
