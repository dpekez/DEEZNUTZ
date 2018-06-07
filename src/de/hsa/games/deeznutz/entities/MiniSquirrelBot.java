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

public class MiniSquirrelBot extends MiniSquirrel {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());
    private static final int VIEW_DISTANCE = 21;
    private BotControllerFactory botControllerFactory;
    private BotController botController;

    MiniSquirrelBot(int energy, XY location, MasterSquirrel daddy, BotControllerFactory botControllerFactory, String name) {
        super(energy, location, daddy, name);
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

        ControllerContext view = new ControllerContextImpl(context, this);

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                logger.finest("MiniBot(ID: " + getId() + ") invoked: " + method.getName() + "(" + Arrays.toString(args) + ")");
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
        private final EntityContext context;
        private final MiniSquirrel miniSquirrel;

        ControllerContextImpl(EntityContext context, MiniSquirrel miniSquirrel) {
            this.context = context;
            this.miniSquirrel = miniSquirrel;
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
        public EntityType getEntityAt(XY target) throws OutOfViewException {
            if (!XYsupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                logger.finer("No Entity in the searchVector");
                throw new OutOfViewException("No Entity in the searchVector");
            }
            return context.getEntityType(target);
        }

        @Override
        public boolean isMine(XY target) throws OutOfViewException {
            if (!XYsupport.isInRange(locate(), target, VIEW_DISTANCE))
                throw new OutOfViewException("Daddy not reachable");
            try {
                return context.getEntity(target).equals(miniSquirrel.getDaddy());
            } catch (Exception e) {
                logger.finer("Daddy not reachable");
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
            logger.fine("Implode Method called");
            if (!(impactRadius >= 2 && impactRadius <= 10))
                return;

            int startX = locate().getX() - impactRadius;
            int startY = locate().getY() - impactRadius;
            int stopX = locate().getX() + impactRadius;
            int stopY = locate().getY() + impactRadius;

            if (startX < 0)
                startX = 0;
            if (startY < 0)
                startY = 0;
            if (stopX > getViewUpperRight().getX())
                stopX = getViewUpperRight().getX();
            if (stopY > getViewLowerLeft().getY())
                stopY = getViewLowerLeft().getY();

            int impactArea = (int) Math.round(Math.pow(impactRadius, 2) * Math.PI);
            int totalImplosionEnergy = 0;

            for (int x = startX; x < stopX; x++) {
                for (int y = startY; y < stopY; y++) {
                    if (context.getEntity(new XY(x, y)) == null)
                        continue;
                    if (x == locate().getX() && y == locate().getY())
                        continue;
                    if (context.getEntityType(new XY(x, y)).equals(EntityType.WALL))
                        continue;

                    Entity entity = context.getEntity(new XY(x, y));

                    int distance = (int) this.locate().distanceFrom(entity.getLocation());
                    int energyLoss = (200 * (miniSquirrel.getEnergy() / impactArea) * (1 - distance / impactRadius));

                    switch (entity.getEntityType()) {
                        case BAD_BEAST:
                        case BAD_PLANT:
                            logger.fine("Imploding on" + "Entity Type: " + entity.getEntityType() + ", Entity ID: " + entity.getId());
                            entity.updateEnergy(energyLoss);
                            energyLoss = 0;
                            if (entity.getEnergy() >= 0)
                                context.killAndReplace(entity);
                            break;
                        case GOOD_PLANT:
                        case GOOD_BEAST:
                            logger.fine("Imploding on" + "Entity Type: " + entity.getEntityType() + ", Entity ID: " + entity.getId());
                            entity.updateEnergy(-energyLoss);
                            if (entity.getEnergy() <= 0)
                                context.killAndReplace(entity);
                            break;
                        case MINI_SQUIRREL_BOT:
                        case MINI_SQUIRREL:
                            if (miniSquirrel.getDaddy() == ((MiniSquirrel) entity).getDaddy())
                                continue;
                            logger.fine("Imploding on" + "Entity Type: " + entity.getEntityType() + ", Entity ID: " + entity.getId());
                            entity.updateEnergy(-energyLoss);
                            if (entity.getEnergy() <= 0)
                                context.killAndReplace(entity);
                            break;
                        case MASTER_SQUIRREL:
                        case MASTER_SQUIRREL_BOT:
                            MasterSquirrel masterSquirrel = (MasterSquirrel) entity;
                            if (masterSquirrel.isMyChild(miniSquirrel))
                                continue;

                            if (entity.getEnergy() < -energyLoss) {
                                energyLoss = entity.getEnergy();
                                updateEnergy(-energyLoss);
                            } else {
                                updateEnergy(-energyLoss);
                            }
                            logger.fine("Imploding on" + "Entity Type: " + entity.getEntityType() + ", Entity ID: " + entity.getId());
                            break;
                        default:
                            energyLoss = 0;
                    }
                    totalImplosionEnergy = totalImplosionEnergy + energyLoss;
                }
            }
            logger.fine("Imploding: Total implosion energy: " + totalImplosionEnergy);
            context.kill(miniSquirrel);
            miniSquirrel.getDaddy().updateEnergy(totalImplosionEnergy);
            context.addImplodingMinis(miniSquirrel);
            context.setImplosionRadius(impactRadius);
        }

        @Override
        public int getEnergy() {
            return miniSquirrel.getEnergy();
        }

        @Override
        public XY directionOfMaster() {
            return XYsupport.decreaseDistance(miniSquirrel.getLocation(), miniSquirrel.getLocation());
        }

        @Override
        public long getRemainingSteps() {
            return context.getGameDurationLeft();
        }
    }

}
