package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.entities.Character;
import de.hsa.games.deeznutz.entities.Entity;
import java.util.ArrayList;

public class EntitySet {
    private ArrayList<Entity> entities;

    EntitySet() {
        entities = new ArrayList<>();
    }

    void add(Entity entity) {
        entities.add(entity);
    }

    void remove(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Moving all entities inside of EntitySet.
     * Checking if entity is a character since only characters have a nextStep() implementation.
     */
    void moveEntities(EntityContext entityContext) {
        for (Entity entity: new ArrayList<>(entities)) {
            if (entity instanceof Character)
                ((Character) entity).nextStep(entityContext);
        }
    }

    /**
     * Generating a copy of the EntitySet array.
     * Used by Board.class, XY.class, etc.
     *
     * @return the newly generated array set instead of the reference
     */
    Entity[] getEntitySetArray() {
        Entity[] newArray = new Entity[entities.size()];
        int index = 0;
        for (Entity entity: entities) {
            if (entity != null) {
                newArray[index++] = entity;
            }
        }
        return newArray;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Entity entity: entities) {
            if (entity != null) {
                s.append(entity).append("\n");
            }
        }
        return s.toString();
    }

}
