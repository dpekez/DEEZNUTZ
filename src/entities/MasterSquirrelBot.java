package entities;

import botapi.*;
import core.EntityContext;
import core.EntityType;
import core.XY;
import core.XYsupport;

public class MasterSquirrelBot extends MasterSquirrel {

    private BotControllerFactory botControllerFactory;
    private BotController masterBotController;

    public MasterSquirrelBot(XY location) {
        super(location);

        this.botControllerFactory = new BotControllerFactory() {
            @Override
            public BotController createMiniBotController() {
                return null;
            }

            @Override
            public BotController createMasterBotController() {
                return view -> {
                    if (!isStunned()) {
                        XY direction = XYsupport.generateRandomMoveVector();
                        view.move(direction);
                    }
                };
            }
        };
        this.masterBotController = botControllerFactory.createMasterBotController();
    }

    @Override
    public void nextStep(EntityContext context) {
        masterBotController.nextStep(new ControllerContextImpl(context, this));
    }

    class ControllerContextImpl implements ControllerContext {

        final int viewDistanceMasterBot = 15;
        private EntityContext context;
        private MasterSquirrel masterSquirrel;

        ControllerContextImpl(EntityContext context, MasterSquirrel masterSquirrel) {
            this.context = context;
            this.masterSquirrel = masterSquirrel;
        }

        @Override
        public XY getViewLowerLeft() {
            return getLocation().addVector(new XY(-viewDistanceMasterBot, -viewDistanceMasterBot));
        }

        @Override
        public XY getViewUpperRight() {
            return getLocation().addVector(new XY(viewDistanceMasterBot, viewDistanceMasterBot));

        }

        @Override
        public XY locate() {
            return masterSquirrel.getLocation();
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
                throw new OutOfViewException("Kein entity in Sichtweite");
            return false;
        }

        @Override
        public void move(XY direction) {
            context.tryMove(masterSquirrel, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {

            if (energy >= masterSquirrel.getEnergy()) {
                try {
                    throw new SpawnException("Nicht genug Energie");
                } catch (SpawnException e) {
                    e.printStackTrace();
                }
            } else {
                //TODO spawn MiniSquirrelBot
            }
        }

        @Override
        public void implode(int impactRadius) {
            //masterSquirrel kann nicht implodieren
        }

        @Override
        public int getEnergy() {
            return masterSquirrel.getEnergy();
        }

        @Override
        public XY directionOfMaster() {
            return XY.ZERO_ZERO;
        }

        @Override
        public long getRemainingSteps() {
            return 0;
        }
    }
}
