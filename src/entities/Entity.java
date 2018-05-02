package entities;

import core.XY;

public abstract class Entity {

    private int id;
    private static int lastID = -1;
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
        return "Entity{ " +
                "id=" + id +
                ", energy=" + energy +
                ", location=" + location +
                '}';
    }

}
