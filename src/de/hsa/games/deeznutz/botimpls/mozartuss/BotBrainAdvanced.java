package de.hsa.games.deeznutz.botimpls.mozartuss;

import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.OutOfViewException;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;

public class BotBrainAdvanced {

    public boolean isWalkable(XY coordinate, ControllerContext context) {
        EntityType entityILikeToEat = context.getEntityAt(coordinate);
        try {
            if (context.getEntityAt(context.locate()) == EntityType.MINI_SQUIRREL || context.getEntityAt(context.locate()) == EntityType.MINI_SQUIRREL_BOT) {
                if (context.getEntityAt(context.locate()) == EntityType.MASTER_SQUIRREL || context.getEntityAt(context.locate()) == EntityType.MASTER_SQUIRREL_BOT) {
                    return context.isMine(coordinate);
                } else {
                    return entityILikeToEat != EntityType.WALL
                            && entityILikeToEat != EntityType.BAD_BEAST
                            && entityILikeToEat != EntityType.BAD_PLANT
                            && entityILikeToEat != EntityType.MINI_SQUIRREL
                            && entityILikeToEat != EntityType.MINI_SQUIRREL_BOT;
                }
            }

        } catch (OutOfViewException e) {
            return true;
        }
        return entityILikeToEat != EntityType.WALL
                && entityILikeToEat != EntityType.BAD_BEAST
                && entityILikeToEat != EntityType.BAD_PLANT
                && entityILikeToEat != EntityType.MASTER_SQUIRREL
                && entityILikeToEat != EntityType.MASTER_SQUIRREL_BOT;
    }


}


