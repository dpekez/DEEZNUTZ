package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.entities.*;

public enum EntityType {

    BAD_BEAST, GOOD_BEAST, BAD_PLANT, GOOD_PLANT, MASTER_SQUIRREL, MINI_SQUIRREL, WALL, NOTHING, MINI_SQUIRREL_BOT, MASTER_SQUIRREL_BOT;

    public static EntityType getType(Entity entity) {
        if (entity instanceof BadBeast)
            return EntityType.BAD_BEAST;
        else if (entity instanceof GoodBeast)
            return EntityType.GOOD_BEAST;
        else if (entity instanceof BadPlant)
            return EntityType.BAD_PLANT;
        else if (entity instanceof GoodPlant)
            return EntityType.GOOD_PLANT;
        else if (entity instanceof HandOperatedMasterSquirrel)
            return EntityType.MASTER_SQUIRREL;
        else if (entity instanceof MiniSquirrelBot)
            return EntityType.MINI_SQUIRREL_BOT;
        else if (entity instanceof MiniSquirrel)
            return EntityType.MINI_SQUIRREL;
        else if (entity instanceof Wall)
            return EntityType.WALL;
        else if (entity instanceof MasterSquirrelBot)
            return EntityType.MASTER_SQUIRREL_BOT;
        else
            return NOTHING;
    }
}
