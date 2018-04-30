package core;

import entities.Character;
import entities.Entity;

public class EntitySet {

    private Entity[] entities;
    private int h;


    public EntitySet(int size) {
        this.entities = new Entity[size];
    }


    public void add(Entity entity) {
        if(h < entities.length) {
            entities[h++] = entity;
        } else {
            System.out.println("EntitySet voll!");
        }
    }

    public void remove(Entity entity) {
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

    public void moveEntities(EntityContext entityContext) {
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

    public Entity[] getEntitySetArray() {
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
        String s = "";

        for(int i=0; i<entities.length; i++) {
            if(entities[i] != null) {
                s += entities[i] + "\n";
            }
        }

        return s;
    }

}
