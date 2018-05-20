package entities;

import botapi.BotController;
import botapi.ControllerContext;
import botapi.OutOfViewException;
import core.EntityContext;
import core.EntityType;
import core.XY;
import core.XYsupport;

public class MiniSquirrelBot extends MiniSquirrel {
    private BotController miniBotController;

    public MiniSquirrelBot(int energy, XY location, MasterSquirrel daddy) {
        super(energy, location, daddy);
        this.miniBotController = daddy.getFactory().createMiniBotController();
    }

    @Override
    public void nextStep(EntityContext context) {
        miniBotController.nextStep(new ControllerContextImpl(context, this));
    }

    public static class ControllerContextImpl implements ControllerContext {
        final double viewDistanceMiniBot = 10.5;
        private EntityContext context;
        private MiniSquirrel miniSquirrel;

        public ControllerContextImpl(EntityContext context, MiniSquirrel miniSquirrel) {
            this.context = context;
            this.miniSquirrel = miniSquirrel;
        }

        @Override
        public XY getViewLowerLeft() {
            return miniSquirrel.getLocation().addVector(new XY(((int) -viewDistanceMiniBot), ((int) viewDistanceMiniBot)));
        }

        @Override
        public XY getViewUpperRight() {
            return miniSquirrel.getLocation().addVector(new XY(((int) viewDistanceMiniBot), ((int) -viewDistanceMiniBot)));
        }

        @Override
        public XY locate() {
            return miniSquirrel.getLocation();
        }

        @Override
        public EntityType getEntityAt(XY xy) throws OutOfViewException {
            if (!XYsupport.isInRange(xy, getViewLowerLeft(), getViewUpperRight()))
                throw new OutOfViewException("Kein Entity in Sichtweite");
            return context.getEntityType(xy);
        }

        @Override
        public boolean isMine(XY xy) throws OutOfViewException {
            if (!XYsupport.isInRange(xy, getViewLowerLeft(), getViewUpperRight()))
                throw new OutOfViewException("Daddy nicht in Sichtweite");
            try {
                return context.getEntityType(xy).equals(miniSquirrel.getDaddy());
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void move(XY direction) {
            context.tryMove(miniSquirrel, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {
            //kann keine MiniSquirrelBot spawnen
        }

        @Override
        public void implode(int impactRadius) {
            //TODO implde
        }

        @Override
        public int getEnergy() {
            return miniSquirrel.getEnergy();
        }

        @Override
        public XY directionOfMaster() {
            int x = 0;
            int y = 0;
            XY vector = miniSquirrel.getLocation().reduceVector(miniSquirrel.getDaddy().getLocation());
            if (vector.getY() > 0) {
                y = 1;
            } else if (vector.getY() < 0) {
                y = -1;
            }
            if (vector.getX() > 0) {
                x = 1;
            } else if (vector.getX() < 0) {
                x = -1;
            }
            return new XY(x, y);
        }

        @Override
        public long getRemainingSteps() {
            return 0;
        }
    }
}
