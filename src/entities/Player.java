package entities;

import core.EntityContext;
import core.XY;

public abstract class Player extends Character {

    private int stunnedRounds = 0;

    public Player(int energy, XY xy) {
        super(energy, xy);
    }


    public void stun() {
        stunnedRounds = 3;
    }

    @Override
    public void nextStep(EntityContext context) {
        if (stunnedRounds > 0)
            stunnedRounds--;
    }

    public boolean isStunned() {
        if(stunnedRounds > 0)
            return true;
        return false;

    }

    public boolean isStunnedNextRound() {
        if(stunnedRounds > 1)
            return true;
        return false;
    }



}
