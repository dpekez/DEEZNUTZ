package de.hsa.games.deeznutz.entities;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.BotControllerFactory;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.OutOfViewException;
import de.hsa.games.deeznutz.core.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

public class MiniSquirrelBot extends MiniSquirrel {
    private static Logger logger = Logger.getLogger(MiniSquirrelBot.class.getName());

    private BotControllerFactory botControllerFactory;
    private BotController botController;
    private static final int VIEW_DISTANCE = 21;

    MiniSquirrelBot(int energy, XY location, MasterSquirrel daddy, BotControllerFactory botControllerFactory) {
        super(energy, location, daddy);
        this.botControllerFactory = botControllerFactory;
        this.botController = botControllerFactory.createMiniBotController();
    }

    @Override
    public void nextStep(EntityContext context) {
        // Not needed here, otherwise the minisquirrel would move twice (random + bot move)
        //super.nextStep(context);
        // But we still need this little line from the super method
        updateEnergy(-1);

        if (isStunned())
            return;

        ControllerContext view = new ControllerContextImpl(context);

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Logger.getLogger(Launcher.class.getName()).finer("MiniBot(ID: " + getId() + ") invoked: " + method.getName() + "(" + Arrays.toString(args) + ")");
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
        return "MiniSquirrelBot{ " + super.toString() + " }";
    }

    public class ControllerContextImpl implements ControllerContext {
        private EntityContext context;

        ControllerContextImpl(EntityContext context) {
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
        public EntityType getEntityAt(XY target) throws OutOfViewException {
            if (!XYsupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                Logger.getLogger(Launcher.class.getName()).finer("No Entity in the searchVector");
                throw new OutOfViewException("No Entity in the searchVector");
            }
            return context.getEntityType(target);
        }

        @Override
        public boolean isMine(XY target) throws OutOfViewException {
            if (!XYsupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                Logger.getLogger(Launcher.class.getName()).finer("Daddy not reachable");
                throw new OutOfViewException("Daddy not reachable");
            }
            try {
                return context.getEntityType(target).equals(MiniSquirrelBot.this.getDaddy());
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void move(XY direction) {
            context.tryMove(MiniSquirrelBot.this, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {
            //kann keine MiniSquirrelBot spawnen
        }

        @Override
        public void implode(int impactRadius) {
            Logger.getLogger(Launcher.class.getName()).info("Implode Method called");
            if (!(impactRadius >= 2 && impactRadius <= 10))
                return;

            int impactArea = (int) Math.round(Math.pow(impactRadius, 2) * Math.PI);
            int totalImplosionEnergy = 0;
            int minX = -impactRadius, minY = -impactRadius;

            for (int x = minX; x < impactRadius; x++)
                for (int y = minY; y < impactRadius; y++) {
                    Entity entity = context.getEntity(new XY(getLocation().getX() + x, getLocation().getY() + y));
                    int distance = (int) this.locate().distanceFrom(entity.getLocation());
                    int energyLoss = (200 * (MiniSquirrelBot.this.getEnergy() / impactArea) * (1 - distance / impactRadius));

                    switch (entity.getEntityType()) {
                        case BAD_BEAST:
                        case BAD_PLANT:
                            entity.updateEnergy(-energyLoss);
                            if (entity.getEnergy() >= 0)
                                context.killAndReplace(entity);
                            break;
                        case GOOD_PLANT:
                        case GOOD_BEAST:
                            entity.updateEnergy(energyLoss);
                            if (entity.getEnergy() <= 0)
                                context.killAndReplace(entity);
                            break;
                        case MINI_SQUIRREL_BOT:
                        case MINI_SQUIRREL:
                            if (MiniSquirrelBot.this.getDaddy() == ((MiniSquirrel) entity).getDaddy())
                                continue;
                            entity.updateEnergy(energyLoss);
                            if (entity.getEnergy() <= 0)
                                context.killAndReplace(entity);
                            break;
                        case MASTER_SQUIRREL:
                        case MASTER_SQUIRREL_BOT:
                            MasterSquirrel masterSquirrel = (MasterSquirrel) entity;
                            if (!(masterSquirrel.isMyChild(MiniSquirrelBot.this)))
                                if (entity.getEnergy() < -energyLoss)
                                    energyLoss = -entity.getEnergy();
                            entity.updateEnergy(energyLoss);
                            break;
                    }
                    totalImplosionEnergy = totalImplosionEnergy - energyLoss;
                }
            MiniSquirrelBot.this.getDaddy().updateEnergy(totalImplosionEnergy);
        }

        @Override
        public int getEnergy() {
            return MiniSquirrelBot.this.getEnergy();
        }

        @Override
        public XY directionOfMaster() {
            return XYsupport.assignMoveVector(MiniSquirrelBot.this.getDaddy().getLocation().reduceVector(MiniSquirrelBot.this.getLocation()));
        }

        @Override
        public long getRemainingSteps() {
            return 0;
        }
    }

}
