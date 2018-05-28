package entities;

import botapi.BotController;
import botapi.ControllerContext;
import botapi.OutOfViewException;
import core.*;

import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MiniSquirrelBot extends MiniSquirrel {
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final BotController controller;

    public MiniSquirrelBot(int energy, XY location, MasterSquirrel daddy) {
        super(energy, location, daddy);
        this.controller = daddy.getFactory().createMiniBotController();
    }

    @Override
    public void nextStep(EntityContext context) {
        super.nextStep(context);
        MiniSquirrelBot.ControllerContextImpl view = new MiniSquirrelBot.ControllerContextImpl(context, this);
        DebugHandler handler = new DebugHandler(view);
        ControllerContext proxyView = (ControllerContext) Proxy.newProxyInstance(
                ControllerContext.class.getClassLoader(),
                new Class[]{ControllerContext.class},
                handler);

        if (isStunned())
            return;
        controller.nextStep(proxyView);
    }


    public class ControllerContextImpl implements ControllerContext {
        final int viewDistanceMiniBot = 10;
        private EntityContext context;
        private MiniSquirrel miniSquirrel;

        ControllerContextImpl(EntityContext context, MiniSquirrel miniSquirrel) {
            this.context = context;
            this.miniSquirrel = miniSquirrel;
        }

        @Override
        public XY getViewLowerLeft() {
            int x = (locate().getX() - viewDistanceMiniBot) < 0 ? 0 : locate().getX() - viewDistanceMiniBot;
            int y = (locate().getY() - viewDistanceMiniBot) < 0 ? 0 : locate().getY() - viewDistanceMiniBot;
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            int x = (locate().getX() + viewDistanceMiniBot) > (context.getSize().getX()) ? context.getSize().getX() : locate().getX() + viewDistanceMiniBot;
            int y = (locate().getY() + viewDistanceMiniBot) > (context.getSize().getY()) ? context.getSize().getY() : locate().getY() + viewDistanceMiniBot;
            return new XY(x, y);
        }

        @Override
        public XY locate() {
            return miniSquirrel.getLocation();
        }

        @Override
        public EntityType getEntityAt(XY xy) throws OutOfViewException {
            if (!XYsupport.isInRange(xy, getViewLowerLeft(), getViewUpperRight())) {
                logger.log(Level.WARNING, "Kein Entity in Sichtweite (MiniBot)");
                throw new OutOfViewException("Kein Entity in Sichtweite (MiniBot)");
            }
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
            return XYsupport.assignMoveVector(miniSquirrel.getDaddy().getLocation().reduceVector(miniSquirrel.getLocation()));
        }

        @Override
        public long getRemainingSteps() {
            return 0;
        }
    }
}
