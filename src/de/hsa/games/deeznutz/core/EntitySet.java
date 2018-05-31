package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.entities.Character;
import de.hsa.games.deeznutz.entities.Entity;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EntitySet {
    private static final Logger logger = Logger.getLogger(EntitySet.class.getName());

    private Entity[] entities;
    private int h;

    EntitySet(int size) {
        this.entities = new Entity[size];
    }

    void add(Entity entity) {
        logger.log(Level.FINEST, "Add entity" + entity);
        if (h < entities.length) {
            entities[h++] = entity;
        } else {
            logger.log(Level.INFO, "EntitySet is full");
        }
    }

    void remove(Entity entity) {
        logger.log(Level.FINEST, "Remove entity" + entity);
        for (int i = 0; i < entities.length; i++)
            if (entity.equals(entities[i])) {
                entities[i] = null;
                break;
            }
    }

    /**
     * Moving all entities inside of EntitySet.
     * Checking if entity is a character since only characters have a nextStep() implementation.
     */
    void moveEntities(EntityContext entityContext) {
        for (Entity entity : entities) {
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
        Entity[] newArray = new Entity[entities.length];
        int index = 0;
        for (Entity entity : entities) {
            if (entity != null) {
                newArray[index++] = entity;
            }
        }
        return newArray;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Entity entity : entities) {
            if (entity != null) {
                s.append(entity).append("\n");
            }
        }
        return s.toString();
    }
}
