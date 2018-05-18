package entities;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.ControllerContext;
import core.EntityContext;
import core.EntityType;
import core.XY;

public class MiniSquirrelBot extends MiniSquirrel {
    private MasterSquirrel daddy;
    private BotController miniBotController;
    private BotControllerFactory botControllerFactory;

    public MiniSquirrelBot(int energy, XY location, MasterSquirrel daddy) {
        super(energy, location, daddy);
        this.daddy = daddy;
        this.miniBotController = botControllerFactory.createMiniBotController();
    }

    @Override
    public void nextStep(EntityContext context) {
        miniBotController.nextStep(new ControllerContextImpl(context, this));
    }

    class ControllerContextImpl implements ControllerContext {
        final double viewDistanceMiniBot = 10.5;
        private EntityContext context;
        private MiniSquirrel miniSquirrel;

        ControllerContextImpl(EntityContext context, MiniSquirrel miniSquirrel) {
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
        public EntityType getEntityAt(XY xy) {
            if (context.getEntityType(xy) != null) {
                return context.getEntityType(xy);
            }
            return null;
        }

        @Override
        public void move(XY direction) {
            context.tryMove(miniSquirrel, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {
        }

        @Override
        public int getEnergy() {
            return miniSquirrel.getEnergy();
        }
    }
}
