package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.core.XY;

public class GoodPlant extends Entity {

    private static final int DEFAULT_ENERGY = 100;
    private static final String DEFAULT_NAME = "goodplant";

    public GoodPlant(XY location) {
        super(DEFAULT_ENERGY, location, DEFAULT_NAME);
    }

    @Override
    public String toString() {
        return "GoodPlant{ " + super.toString() + " }";
    }

}
