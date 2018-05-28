package entities;

import core.EntityType;
import core.XY;

public abstract class Entity {

    private static int lastID = -1;
    private int id;
    private int energy;
    private XY location;

    public Entity(int energy, XY location) {
        this.id = ++lastID;
        this.energy = energy;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public XY getLocation() {
        return location;
    }

    public void setLocation(XY location) {
        this.location = location;
    }

    public void updateEnergy(int energy) {
        this.energy += energy;
    }

    public void move(XY vector) {
        location = location.addVector(vector);
    }

    public String toString() {
        return "Entity{ " + "id=" + id + ", energy=" + energy + ", location=" + location + '}';
    }

    public EntityType getEntityType() {
        if (this instanceof BadBeast)
            return EntityType.BAD_BEAST;
        else if (this instanceof GoodBeast)
            return EntityType.GOOD_BEAST;
        else if (this instanceof BadPlant)
            return EntityType.BAD_PLANT;
        else if (this instanceof GoodPlant)
            return EntityType.GOOD_PLANT;
        else if (this instanceof Wall)
            return EntityType.WALL;
        else if (this instanceof MiniSquirrel)
            return EntityType.MINI_SQUIRREL;
        else if (this instanceof MasterSquirrel)
            return EntityType.MASTER_SQUIRREL;
        else if (this instanceof MasterSquirrelBot)
            return EntityType.MASTER_SQUIRREL_BOT;
        else if (this instanceof MiniSquirrelBot)
            return EntityType.MINI_SQUIRREL_BOT;
        else
            return EntityType.NOTHING;
    }
}
