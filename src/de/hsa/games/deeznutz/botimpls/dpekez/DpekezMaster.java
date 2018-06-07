package de.hsa.games.deeznutz.botimpls.dpekez;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

import java.util.logging.Logger;

public class DpekezMaster implements BotController {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private int refreshSelector;
    private int selectedQ;
    private int miniSpawnThreshhold;
    private int miniSpawnThreshholdStepper;
    private int maxMiniEnergy;

    public DpekezMaster() {
        refreshSelector = 0;
        selectedQ = 1;
        miniSpawnThreshhold = 1500;
        miniSpawnThreshholdStepper = 1000;
        maxMiniEnergy = 2000;
    }

    @Override
    public void nextStep(ControllerContext context) {

        // spawn mini
        if (context.getEnergy() >= miniSpawnThreshhold && context.getRemainingSteps() > 400) {
            logger.fine("Trying to spawn Mini.");
            miniSpawnThreshhold += miniSpawnThreshholdStepper;
            miniSpawnThreshholdStepper *= 0.9;
            int miniEnergy = (int) (context.getEnergy() * 0.1);
            if (miniEnergy >= maxMiniEnergy)
                miniEnergy = maxMiniEnergy;
            XY pos = new XY(getMiniSpawnPosition(context).getX(), getMiniSpawnPosition(context).getY());
            if (pos != XY.ZERO_ZERO) {
                logger.fine("Spawning Mini.");
                context.spawnMiniBot(pos, miniEnergy);
            }
            return;
        }

        // set quadrant selector refresh rate
        if (refreshSelector <= 0) {
            refreshSelector = 30;
            selectedQ = qSelector(context);
        } else
            refreshSelector--;

        XY nearestEntity = null;
        switch (selectedQ) {
            case 0:
                nearestEntity = nearestEntityFinder(context, context.getViewLowerLeft().getX(), context.getViewUpperRight().getX(), context.getViewUpperRight().getY(), context.getViewLowerLeft().getY());
                break;
            case 1:
                nearestEntity = nearestEntityFinder(context, context.getViewLowerLeft().getX(), context.locate().getX(), context.getViewUpperRight().getY(), context.locate().getY());
                break;
            case 2:
                nearestEntity = nearestEntityFinder(context, context.locate().getX(), context.getViewUpperRight().getX(), context.getViewUpperRight().getY(), context.locate().getY());
                break;
            case 3:
                nearestEntity = nearestEntityFinder(context, context.getViewLowerLeft().getX(), context.locate().getX(), context.locate().getY(), context.getViewLowerLeft().getY());
                break;
            case 4:
                nearestEntity = nearestEntityFinder(context, context.locate().getX(), context.getViewUpperRight().getX(), context.locate().getY(), context.getViewLowerLeft().getY());
                break;
        }

        logger.fine("start: " + context.locate() + " ziel: " + nearestEntity);

        XY moveVector;

        // no entity in sight
        if (nearestEntity == null) {
            switch (selectedQ) {
                case 0:
                    logger.fine("nicht gut");
                    moveVector = XYsupport.generateRandomMoveVector();
                    break;
                case 1:
                    moveVector = XY.LEFT_UP;
                    break;
                case 2:
                    moveVector = XY.RIGHT_UP;
                    break;
                case 3:
                    moveVector = XY.LEFT_DOWN;
                    break;
                case 4:
                    moveVector = XY.RIGHT_DOWN;
                    break;
                default:
                    moveVector = XYsupport.generateRandomMoveVector();
            }
            //refreshSelector = 0;
        } else {
            moveVector = XYsupport.decreaseDistance(context.locate(), nearestEntity);
        }

        // bad beast evasion
        if (context.getEntityAt(context.locate().addVector(moveVector)) == EntityType.BAD_BEAST) {
            logger.fine("Evading bad beast.");
            refreshSelector = -5;
            moveVector = badBeastEvader(moveVector);
        }

        // bad plant evasion
        else if (context.getEntityAt(context.locate().addVector(moveVector)) == EntityType.BAD_PLANT) {
            logger.fine("Evading bad plant.");
            refreshSelector = -5;
            moveVector = badPlantEvader(moveVector);
        }

        // wall evasion
        else if (context.getEntityAt(context.locate().addVector(moveVector)) == EntityType.WALL) {
            logger.fine("Evading wall.");
            refreshSelector = -5;
            moveVector = wallEvader(moveVector);
        }

        // enemy evasion
        else if (context.getEntityAt(context.locate().addVector(moveVector)) == EntityType.MASTER_SQUIRREL_BOT) {
            //else if (!context.isMine(context.locate().addVector(moveVector))) {
            logger.fine("Evading own mini.");
            refreshSelector = -5;
            moveVector = enemyEvader(moveVector);
        }

        context.move(moveVector);
    }

    private int qSelector(ControllerContext context) {
        int highest = quantifier(context, context.getViewLowerLeft().getX(), context.getViewUpperRight().getX(), context.getViewUpperRight().getY(), context.getViewLowerLeft().getY());
        int q1 = quantifier(context, context.getViewLowerLeft().getX(), context.locate().getX(), context.getViewUpperRight().getY(), context.locate().getY());
        int q2 = quantifier(context, context.locate().getX(), context.getViewUpperRight().getX(), context.getViewUpperRight().getY(), context.locate().getY());
        int q3 = quantifier(context, context.getViewLowerLeft().getX(), context.locate().getX(), context.locate().getY(), context.getViewLowerLeft().getY());
        int q4 = quantifier(context, context.locate().getX(), context.getViewUpperRight().getX(), context.locate().getY(), context.getViewLowerLeft().getY());

        int selected = 0;

        if (q1 > highest) {
            selected = 2;
            highest = q2;
        }
        if (q2 > highest) {
            selected = 2;
            highest = q2;
        }
        if (q3 > highest) {
            selected = 3;
            highest = q3;
        }
        if (q4 > highest) {
            selected = 4;
        }

        logger.fine(selected + " selected");
        return selected;
    }

    private int quantifier(ControllerContext context, int startX, int stopX, int startY, int stopY) {
        int quantity = 0;
        for (int x = startX; x < stopX; x++) {
            for (int y = startY; y < stopY; y++) {
                //if (context.getEntityAt(new XY(x, y)) == EntityType.MINI_SQUIRREL_BOT)
                //    quantity += 7;
                if (context.getEntityAt(new XY(x, y)) == EntityType.GOOD_BEAST)
                    quantity += 4;
                if (context.getEntityAt(new XY(x, y)) == EntityType.GOOD_PLANT)
                    quantity += 3;
                if (context.getEntityAt(new XY(x, y)) == EntityType.MINI_SQUIRREL)
                    quantity -= 5;
                if (context.getEntityAt(new XY(x, y)) == EntityType.BAD_BEAST)
                    quantity -= 2;
                if (context.getEntityAt(new XY(x, y)) == EntityType.BAD_PLANT)
                    quantity -= 1;
                if (context.getEntityAt(new XY(x, y)) == EntityType.WALL)
                    quantity -= 1;
                if (context.getEntityAt(new XY(x, y)) == EntityType.NOTHING)
                    quantity += 1;
            }
        }
        logger.fine("Quantity: " + quantity);
        return quantity;
    }

    private XY nearestEntityFinder(ControllerContext context, int startX, int stopX, int startY, int stopY) {
        XY nearestEntity = null;
        for (int x = startX; x < stopX; x++) {
            for (int y = startY; y < stopY; y++) {
                if (context.getEntityAt(new XY(x, y)) != EntityType.GOOD_PLANT
                        && context.getEntityAt(new XY(x, y)) != EntityType.GOOD_BEAST
                    /*&& context.getEntityAt(new XY(x, y)) != EntityType.MINI_SQUIRREL_BOT*/) {
                    continue;
                }
                if (nearestEntity == null) {
                    nearestEntity = new XY(x, y);
                    logger.fine("Found first near entity: " + nearestEntity);
                } else if (context.locate().distanceFrom(new XY(x, y)) < context.locate().distanceFrom(nearestEntity)) {
                    nearestEntity = new XY(x, y);
                    logger.fine("Found nearer entity: " + nearestEntity);
                }
            }
        }

        return nearestEntity;
    }

    private XY enemyEvader(XY nextLocation) {
        //todo
        return wallEvader(nextLocation);
    }

    private XY badBeastEvader(XY nextLocation) {
        //todo
        return wallEvader(nextLocation);
    }

    private XY badPlantEvader(XY nextLocation) {
        //todo
        return wallEvader(nextLocation);
    }

    private XY wallEvader(XY nextLocation) {
        if (nextLocation == XY.RIGHT)
            return XY.RIGHT_DOWN;
        else if (nextLocation == XY.RIGHT_DOWN)
            return XY.DOWN;
        else if (nextLocation == XY.DOWN)
            return XY.LEFT_DOWN;
        else if (nextLocation == XY.LEFT_DOWN)
            return XY.LEFT;
        else if (nextLocation == XY.LEFT)
            return XY.LEFT_DOWN;
        else if (nextLocation == XY.LEFT_UP)
            return XY.UP;
        else if (nextLocation == XY.UP)
            return XY.RIGHT_UP;
        else if (nextLocation == XY.RIGHT_UP)
            return XY.RIGHT;
        else
            return XYsupport.generateRandomMoveVector();
    }

    private XY getMiniSpawnPosition(ControllerContext context) {
        logger.fine("Searching Mini Spawn Position from: " + context.locate());
        XY moveVector;
        boolean isEmpty;
        int maxMiniSpawnPositionSearchTries = 8;

        do {
            if (--maxMiniSpawnPositionSearchTries == 0) {
                logger.warning("No Mini Spawn Position found!");
                return XY.ZERO_ZERO;
            }

            isEmpty = false;
            moveVector = XYsupport.generateRandomMoveVector();

            if (context.getEntityAt(context.locate().addVector(moveVector)) == EntityType.NOTHING) {
                isEmpty = true;
                logger.fine("Found Mini Spawn Position: " + context.locate().addVector(moveVector));
            }

        } while (!isEmpty);

        logger.fine("Found Mini Spawn Direction: " + moveVector);
        return moveVector;
    }

}
