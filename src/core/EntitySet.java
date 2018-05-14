package core;

import entities.Character;
import entities.Entity;

public class EntitySet {

    private Entity[] entities;
    private int h;


    EntitySet(int size) {
        this.entities = new Entity[size];
    }


    void add(Entity entity) {
        if(h < entities.length) {
            entities[h++] = entity;
        } else {
            System.out.println("EntitySet voll!");
        }
    }

    void remove(Entity entity) {
        for(int i = 0; i < entities.length; i++)
            if(entity.equals(entities[i])) {
                entities[i] = null;
                break;
            }
    }


    /**
     * Moving all entities inside of EntitySet.
     * Checking if entity is a character since only characters have a nextStep() implementation.
     */

    void moveEntities(EntityContext entityContext) {
        for(Entity entity: entities) {
            if(entity instanceof Character)
                ((Character)entity).nextStep(entityContext);
        }
    }


    /**
     * Generating a copy of the EntitySet array.
     * Used by Board.class, XY.class, etc.
     *
     * @return  the newly generated array set instead of the reference
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
