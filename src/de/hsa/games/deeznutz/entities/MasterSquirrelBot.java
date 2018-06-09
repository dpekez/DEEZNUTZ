package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.BotControllerFactory;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.OutOfViewException;
import de.hsa.games.deeznutz.core.EntityContext;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

public class MasterSquirrelBot extends MasterSquirrel {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());
    private static final int VIEW_DISTANCE = 31;
    private BotControllerFactory botControllerFactory;
    private BotController botController;

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

        ControllerContext view = new ControllerContextImpl(context, this);

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
        private final EntityContext context;
        private final MasterSquirrel masterSquirrel;

        ControllerContextImpl(EntityContext context, MasterSquirrel masterSquirrel) {
            this.context = context;
            this.masterSquirrel = masterSquirrel;
        }

        @Override
        public XY getViewLowerLeft() {
            return XYsupport.viewLowerLeft(context, VIEW_DISTANCE, locate());
        }

        @Override
        public XY getViewUpperRight() {
            return XYsupport.viewUpperRight(context, VIEW_DISTANCE, locate());
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
                throw new OutOfViewException("Kein entity in Sichtweite (master)");
            }
            try {
                if (masterSquirrel.isMyChild((MiniSquirrel) context.getEntity(target)))
                    return true;
            } catch (Exception e) {
                logger.finer("No Entity in searchVector");
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
            if (energy <= getEnergy()) {
                MiniSquirrelBot miniSquirrelBot = new MiniSquirrelBot(energy, getLocation().addVector(direction), masterSquirrel, botControllerFactory, masterSquirrel.getName());
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
            return masterSquirrel.getEnergy();
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
