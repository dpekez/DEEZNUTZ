package entities;

import botapi.*;
import core.EntityContext;
import core.EntityType;
import core.XY;
import core.XYsupport;

public class MasterSquirrelBot extends MasterSquirrel {

    private final BotController controller;

    public MasterSquirrelBot(XY location, BotControllerFactory factory) {
        super(location);
        setFactory(factory);
        this.controller = factory.createMasterBotController();
    }

    @Override
    public void nextStep(EntityContext context) {
        ControllerContextImpl view = new ControllerContextImpl(context, this);
        if (!isStunned())
            controller.nextStep(view);
    }

    public class ControllerContextImpl implements ControllerContext {

        final int viewDistanceMasterBot = 15;
        private EntityContext context;
        private MasterSquirrel masterSquirrel;

        public ControllerContextImpl(EntityContext context, MasterSquirrel masterSquirrel) {
            this.context = context;
            this.masterSquirrel = masterSquirrel;
        }

        @Override
        public XY getViewLowerLeft() {
            int x = (locate().getX() - viewDistanceMasterBot) < 0 ? 0 : locate().getX() - viewDistanceMasterBot;
            int y = locate().getY() - viewDistanceMasterBot < 0 ? 0 : locate().getY() - viewDistanceMasterBot;
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            int x = locate().getX() + viewDistanceMasterBot > context.getSize().getX() ? context.getSize().getX() : locate().getX() + viewDistanceMasterBot;
            int y = locate().getY() + viewDistanceMasterBot > context.getSize().getY() ? context.getSize().getY() : locate().getY();
            return new XY(x, y);
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
