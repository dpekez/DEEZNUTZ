package entities;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.ControllerContext;
import core.EntityContext;
import core.EntityType;
import core.XY;

public class MasterSquirrelBot extends MasterSquirrel {
    private BotControllerFactory botControllerFactory;
    private BotController masterBotController;

    public MasterSquirrelBot(XY location) {
        super(location);
        this.botControllerFactory = new BotControllerFactory() {
            @Override
            public BotController createMiniBotController() {
                return view -> {
                    if (!isStunned()) {
                        XY direction = new XY(getLocation().getX(), getLocation().getY()); //TODO Direction(Glaube das Stimmt nicht)
                        view.move(direction);
                    }
                };
            }

            @Override
            public BotController createMasterBotController() {
                return null;
            }
        };
        this.masterBotController = botControllerFactory.createMasterBotController();
    }

    @Override
    public void nextStep(EntityContext context) {
        masterBotController.nextStep(new ControllerContextImpl(context, this));
    }

    class ControllerContextImpl implements ControllerContext {

        final double viewDistanceMasterBot = 15.5;
        private EntityContext context;
        private MasterSquirrel masterSquirrel;

        ControllerContextImpl(EntityContext context, MasterSquirrel masterSquirrel) {
            this.context = context;
            this.masterSquirrel = masterSquirrel;
        }

        @Override
        public XY getViewLowerLeft() {
            return masterSquirrel.getLocation().addVector(new XY(((int) -viewDistanceMasterBot), ((int) viewDistanceMasterBot)));
        }

        @Override
        public XY getViewUpperRight() {
            return masterSquirrel.getLocation().addVector(new XY(((int) viewDistanceMasterBot), ((int) -viewDistanceMasterBot)));
        }

        @Override
        public EntityType getEntityAt(XY xy) {
            return context.getEntityType(xy);
        }

        @Override
        public void move(XY direction) {
            context.tryMove(masterSquirrel, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {
            //TODO SPAWN MINI BOT
        }

        @Override
        public int getEnergy() {
            return masterSquirrel.getEnergy();
        }
    }
}
