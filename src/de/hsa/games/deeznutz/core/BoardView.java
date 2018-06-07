package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.entities.Entity;

public interface BoardView {

    EntityType getEntityType(int x, int y);

    Entity getEntity(int x, int y);

    XY getSize();

}
