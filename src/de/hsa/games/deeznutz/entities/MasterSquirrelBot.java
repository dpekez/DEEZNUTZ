package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.*;
import de.hsa.games.deeznutz.core.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

public class MasterSquirrelBot extends MasterSquirrel {

    private BotControllerFactory botControllerFactory;
    private BotController botController;
    private static final int VIEW_DISTANCE = 31;

    public MasterSquirrelBot(XY location, BotControllerFactory botControllerFactory) {
        super(location);
        this.botControllerFactory = botControllerFactory;
        this.botController = botControllerFactory.createMasterBotController();
    }

    @Override
    public void nextStep(EntityContext context) {
        super.nextStep(context);

        if (isStunned())
            return;

        ControllerContext view = new ControllerContextImpl(context);

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Logger.getLogger(Launcher.class.getName()).info("MasterBot(ID: " + getId() + ") invoked: " + method.getName() + "(" + Arrays.toString(args) + ")");
                return method.invoke(view, args);
            }
        };

        ControllerContext proxyInstance = (ControllerContext) Proxy.newProxyInstance(
                ControllerContext.class.getClassLoader(),
                new Class[]{ControllerContext.class},
                handler);

        botController.nextStep(proxyInstance);
    }

    public class ControllerContextImpl implements ControllerContext {
        private EntityContext context;

        public ControllerContextImpl(EntityContext context) {
            this.context = context;
        }

        @Override
        public XY getViewLowerLeft() {
            int x = (locate().getX() - VIEW_DISTANCE) < 0 ? 0 : locate().getX() - VIEW_DISTANCE;
            int y = (locate().getY() - VIEW_DISTANCE) < 0 ? 0 : locate().getY() - VIEW_DISTANCE;
            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            int x = (locate().getX() + VIEW_DISTANCE) > (context.getSize().getX()) ? context.getSize().getX() : locate().getX() + VIEW_DISTANCE;
            int y = (locate().getY() + VIEW_DISTANCE) > (context.getSize().getY()) ? context.getSize().getY() : locate().getY() + VIEW_DISTANCE;
            return new XY(x, y);
        }

        @Override
        public XY locate() {
            return getLocation();
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
                Logger.getLogger(Launcher.class.getName()).finer("Kein Entity in Sichtweite (master)");
                throw new OutOfViewException("Kein entity in Sichtweite (master)");
            }
            try {
                if (MasterSquirrelBot.this.isMyChild((MiniSquirrel) context.getEntiy(xy)))
                    return true;
            } catch (Exception e) {
                return false;
            }
            return false;
        }

        @Override
        public void move(XY direction) {
            context.tryMove(MasterSquirrelBot.this, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {
            if (energy <= getEnergy()) {
                MiniSquirrelBot miniSquirrelBot = new MiniSquirrelBot(energy, getLocation().addVector(direction), MasterSquirrelBot.this, botControllerFactory);
                context.insertEntity(miniSquirrelBot);
                updateEnergy(-energy);
            }
        }

        @Override
        public void implode(int impactRadius) {
            // masterSquirrel kann nicht implodieren
        }

        @Override
        public int getEnergy() {
            return MasterSquirrelBot.this.getEnergy();
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
