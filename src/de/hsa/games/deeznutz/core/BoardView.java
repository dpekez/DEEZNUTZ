package de.hsa.games.deeznutz.core;

public interface BoardView {

    EntityType getEntityType(int x, int y);

    XY getSize();
}
