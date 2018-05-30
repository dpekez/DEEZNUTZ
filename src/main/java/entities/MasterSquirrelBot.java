package entities;

import botapi.*;
import core.*;

import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterSquirrelBot extends MasterSquirrel {
    private static Logger logger = Logger.getLogger(MasterSquirrelBot.class.getName());

    private final BotController controller;

    public MasterSquirrelBot(XY location, BotControllerFactory factory) {
        super(location);
        setFactory(factory);
        this.controller = factory.createMasterBotController();
    }

    public class ControllerContextImpl implements ControllerContext {
        private EntityContext context;
        private MasterSquirrel masterSquirrel;
        final int viewDistanceMasterBot = 15;

        public ControllerContextImpl(EntityContext context, MasterSquirrel masterSquirrel) {
            this.context = context;
            this.masterSquirrel = masterSquirrel;
        }

        @Override
        public XY getViewLowerLeft() {
            int x = (locate().getX() - viewDistanceMasterBot) < 0 ? 0 : locate().getX() - viewDistanceMasterBot;
            int y = (locate().getY() - viewDistanceMasterBot) < 0 ? 0 : locate().getY() - viewDistanceMasterBot;
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            int x = (locate().getX() + viewDistanceMasterBot) > (context.getSize().getX()) ? context.getSize().getX() : locate().getX() + viewDistanceMasterBot;
            int y = (locate().getY() + viewDistanceMasterBot) > (context.getSize().getY()) ? context.getSize().getY() : locate().getY() + viewDistanceMasterBot;
            return new XY(x, y);
        }

        @Override
        public XY locate() {
            return masterSquirrel.getLocation();
        }

        @Override
        public EntityType getEntityAt(XY xy) throws OutOfViewException {
            if (!XYsupport.isInRange(xy, getViewLowerLeft(), getViewUpperRight())) {
                throw new OutOfViewException("Kein Entity in Sichtweite (MasterBot)");
            }
            return context.getEntityType(xy);
        }

        @Override
        public boolean isMine(XY xy) throws OutOfViewException {
            if (!XYsupport.isInRange(xy, getViewLowerLeft(), getViewUpperRight())) {
                logger.log(Level.FINER, "Kein Entity in Sichtweite (master)");
                throw new OutOfViewException("Kein entity in Sichtweite (master)");
            }
            try {
                if (masterSquirrel.isMyChild((MiniSquirrel) context.getEntiy(xy)))
                    return true;
            } catch (Exception e) {
                return false;
            }
            return false;
        }

        @Override
        public void move(XY direction) {
            context.tryMove(masterSquirrel, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {
            try {
                if (energy >= masterSquirrel.getEnergy()) {
                    throw new SpawnException("Nicht genug Energie");
                } else {
                    MiniSquirrelBot mini = new MiniSquirrelBot(energy, getLocation(), masterSquirrel);
                    context.insertEntity(mini);
                }
            } catch (SpawnException e) {
                logger.log(Level.WARNING, e.getMessage());
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

    @Override
    public void nextStep(EntityContext context) {
        super.nextStep(context);
        ControllerContextImpl view = new ControllerContextImpl(context, this);
        DebugHandler handler = new DebugHandler(view);
        ControllerContext proxyView = (ControllerContext) Proxy.newProxyInstance(
                ControllerContext.class.getClassLoader(),
                new Class[]{ControllerContext.class},
                handler);

        if (isStunned())
            return;
        controller.nextStep(proxyView);
    }

}
