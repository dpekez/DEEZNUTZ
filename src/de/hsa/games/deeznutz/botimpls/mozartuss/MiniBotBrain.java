package de.hsa.games.deeznutz.botimpls.mozartuss;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.OutOfViewException;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

import java.util.logging.Logger;

public class MiniBotBrain implements BotController {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private BotBrain brain = new BotBrain();

    @Override
    public void nextStep(ControllerContext view) {
        XY move;
        int impactRadius = 5;
        boolean shouldImplode;
        shouldImplode = implodeCondition(view, impactRadius);

        if (shouldImplode) {
            logger.fine("MiniSquirrelBot implode!");
            view.implode(impactRadius);
        }
        XY nearestMB = brain.nearestEntity(view, EntityType.MASTER_SQUIRREL_BOT);
        XY nearestM = brain.nearestEntity(view, EntityType.MASTER_SQUIRREL);

        if ((view.locate().distanceFrom(nearestM) < 1) || (view.locate().distanceFrom(nearestMB) < 1))
            move = XYsupport.decreaseDistance(nearestM, view.locate());
        else
            move = brain.moveToNearestGoodEntity(view);
        if (view.getEnergy() < 1000) {
            view.move(move);
        } else {
            move = view.directionOfMaster();
            view.move(move);
        }
    }

    private boolean implodeCondition(ControllerContext context, int impactRadius) {
        int entitiesCounter = 0;

        int startX = context.locate().getX() - impactRadius;
        int startY = context.locate().getY() - impactRadius;
        int stopX = context.locate().getX() + impactRadius;
        int stopY = context.locate().getY() + impactRadius;

        if (startX < 0)
            startX = 0;
        if (startY < 0)
            startY = 0;
        if (stopX > context.getViewUpperRight().getX())
            stopX = context.getViewUpperRight().getX();
        if (stopY > context.getViewLowerLeft().getY())
            stopY = context.getViewLowerLeft().getY();

        logger.finest("startX:" + startX + " stopX:" + stopX + " startY:" + startY + " stopY:" + stopY);

        for (int x = startX; x < stopX; x++) {
            for (int y = startY; y < stopY; y++) {
                try {
                    if (new XY(x, y) == context.locate())
                        continue;
                    EntityType checkEntity = context.getEntityAt(new XY(x, y));
                    switch (checkEntity) {
                        case WALL:
                        case NOTHING:
                            break;
                        case MASTER_SQUIRREL_BOT:
                        case MASTER_SQUIRREL:
                        case MINI_SQUIRREL:
                        case MINI_SQUIRREL_BOT:
                            if (!context.isMine(new XY(x, y)))
                                entitiesCounter++;
                            else
                                break;
                            break;
                        case GOOD_BEAST:
                        case GOOD_PLANT:
                        case BAD_PLANT:
                        case BAD_BEAST:
                            entitiesCounter++;
                    }

                } catch (OutOfViewException e) {
                    logger.fine("No Entity inside of implode search vector.");
                }
            }
        }

        logger.fine("Entities inside impact radius: " + entitiesCounter);
        return (entitiesCounter >= 4);
    }

}
