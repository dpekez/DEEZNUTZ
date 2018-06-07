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
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private BotControllerFactory botControllerFactory;
    private BotController botController;
    private static final int VIEW_DISTANCE = 31;

    public MasterSquirrelBot(XY location, BotControllerFactory botControllerFactory, String name) {
        super(location, name);
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
                logger.finest("MasterBot(ID: " + getId() + ") invoked: " + method.getName() + "(" + Arrays.toString(args) + ")");
                return method.invoke(view, args);
            }
        };

        ControllerContext proxyInstance = (ControllerContext) Proxy.newProxyInstance(
                ControllerContext.class.getClassLoader(),
                new Class[]{ControllerContext.class},
                handler);

        botController.nextStep(proxyInstance);
    }

    @Override
    public String toString() {
        return "MasterSquirrelBot{ " + super.toString() + " }";
    }

    public class ControllerContextImpl implements ControllerContext {
        private EntityContext context;

        public ControllerContextImpl(EntityContext context) {
            this.context = context;
        }

        @Override
        public XY getViewLowerLeft() {
            int x = getLocation().getX() - (VIEW_DISTANCE - 1) / 2;
            int y = getLocation().getY() + (VIEW_DISTANCE - 1) / 2;

            if (x < 0)
                x = 0;

            if (y > context.getSize().getY())
                y = context.getSize().getY();

            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            int x = getLocation().getX() + (VIEW_DISTANCE - 1) / 2;
            int y = getLocation().getY() - (VIEW_DISTANCE - 1) / 2;

            if (x > context.getSize().getX())
                x = context.getSize().getX();

            if (y < 0)
                y = 0;

            return new XY(x, y);
        }

        @Override
        public XY locate() {
            return getLocation();
        }

        @Override
        public EntityType getEntityAt(XY target) {
            if (!XYsupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                logger.finer("No Entity in searchVecotr");
                throw new OutOfViewException("Kein Entity in Sichtweite (MasterBot)");
            }
            return context.getEntityType(target);
        }

        @Override
        public boolean isMine(XY target) {
            if (!XYsupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                logger.finer("No Entity in searchVector");
                throw new OutOfViewException("Kein entity in Sichtweite (master)");
            }
            try {
                if (MasterSquirrelBot.this.isMyChild((MiniSquirrel) context.getEntity(target)))
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
                MiniSquirrelBot miniSquirrelBot = new MiniSquirrelBot(energy, getLocation().addVector(direction), MasterSquirrelBot.this, botControllerFactory, MasterSquirrelBot.this.getName());
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
            return context.getGameDurationLeft();
        }
    }

}
