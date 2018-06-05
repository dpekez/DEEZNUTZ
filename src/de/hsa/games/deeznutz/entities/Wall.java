package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.core.XY;

public class Wall extends Entity {

    private static final int DEFAULT_ENERGY = -10;
    private static final String DEFAULT_NAME = "wall";

    public Wall(XY location) {
        super(DEFAULT_ENERGY, location, DEFAULT_NAME);
    }

    @Override
    public String toString() {
        return "Wall{ " + super.toString() + " }";
    }

}
