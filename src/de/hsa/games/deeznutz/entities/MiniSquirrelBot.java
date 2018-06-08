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
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * The MiniSquirrelBot contains all the Information your BotAI have to know.
 * The class define the Bot ViewDistance for example, so he can't see the whole Board.
 * TheMiniSquirrelBot is a instance from MiniSquirrel so it has all the attributes from MiniSquirrel.
 */

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

    /**
     * At the nextStep methode you checke if the MiniSquirrelBot is stunned.
     * Also you catch all the Invocations.
     * After that you call the nextStep methode from your BotAI over the ControllerontextInterface
     *
     * @param context //TODO
     */
    @Override
    public void nextStep(EntityContext context) {
        // Not needed here, otherwise the minisquirrel would move twice (random + bot move)
        //super.nextStep(context);
        // But we still need this little line from the super method
        updateEnergy(-1);

        if (isStunned())
            return;

        ControllerContext view = new ControllerContextImpl(context, this);

        InvocationHandler handler = (proxy, method, args) -> {
            logger.finest("MiniBot(ID: " + getId() + ") invoked: " + method.getName() + "(" + Arrays.toString(args) + ")");
            return method.invoke(view, args);
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

    /**
     * Here you define all the MiniSquirrelBot specific rules
     */
    public class ControllerContextImpl implements ControllerContext {
        private final EntityContext context;
        private final MiniSquirrel miniSquirrel;

        ControllerContextImpl(EntityContext context, MiniSquirrel miniSquirrel) {
            this.context = context;
            this.miniSquirrel = miniSquirrel;
        }

        /**
         * The getViewerLowerLeft methode check the lowestLeft point of the filed of view.
         * If the lowestLeft coordinate is outside the filed the lowerLeft coordinate is automaticly the lowestLeft coordinate of the Board.
         * @return new XY(x,y)
         */

        @Override
        public XY getViewLowerLeft() {
            return XYsupport.viewLowerLeft(context, VIEW_DISTANCE, locate());
        }

        /**
         * The getViewerUpperRight methode check the highestRight point of the filed of view.
         * If this coordinate is also outside the board the highestRight coordinate is the highestRight coordinate of the Board.
         * @return new XY(x,y)
         */

        @Override
        public XY getViewUpperRight() {
            return XYsupport.viewUpperRight(context, VIEW_DISTANCE, locate());
        }

        /**
         * The mthode returns the current position of the MiniSquirrelBot
         * @return new XY (x,y)
         */
        @Override
        public XY locate() {
            return getLocation();
        }

        /**
         * Here you can find EntityTypes at specific positions.
         * @param target the coordinate of the field where you want to get the EntityType
         * @return EntityType
         * @throws OutOfViewException it throws this Exception if No entity is in the searchVector
         */
        @Override
        public EntityType getEntityAt(XY target) throws OutOfViewException {
            if (!XYsupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                logger.finer("No Entity in the searchVector");
                throw new OutOfViewException("No Entity in the searchVector");
            }
            return context.getEntityType(target);
        }

        /**
         * The methode checks an Entity if the Entity is your daddy-MasterSquirrel or one of your sibling MiniSquirrels
         * @param target the coordinate of the Entity you want to check
         * @return true if it is your MasterSquirrel/MiniSquirrel and false if it isn't
         * @throws OutOfViewException when no Entity is in the searchVector
         */
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

        /**
         * This methode collect the energy of an certain impactRadius.
         * The MiniSquirrelbot scan the filed in the impactRadius and get all the EntityTypes.
         * The impactArea is the rounded result of (impactRadius² * π)
         *  The formular to calculate the EnergyLoss is (200 * (MinisquirrelEnergy / impactArea) * (1-distance form the Entity / impactRadius)
         *  Now you iterate throw all the coordinates in the ImpactRadius:
         *  The GoodBests, Badbeasts, GoodPlants, BadPlants and Mini or masterSquirrels from the Enemy return the EnergyLoss.
         *  the EnergyLoss will be add at the ende and give tahn the TotalImplodeEnergy
         *  The EnergyLoss decrease the Energy of the GoodBeast or Goodplants if the energy of the GoodEntitys is under 0
         *  the Entity will be killed and replaced.
         *  Also the EnergyLoss invrease the Energy of the Badentitys if there energy is higher than 0 the Entity will be killed.
         *  The EnergyLoss decrease also the energy of the MasterSquirrel but his energy can't go under 0 so you can get all of the rest energy of him
         *  if his current energy is lower than the EnergyLoss.
         *  After all the MiniSquirrel which Implode will die and send all the collected energy to his daddy MasterSquirrel
         * @param impactRadius : radius of the impact circle
         */
        @Override
        public void implode(int impactRadius) {
            logger.fine("Implode Method called");
            if (!(impactRadius >= 2 && impactRadius <= 10))
                return;

            int startX = locate().getX() - impactRadius;
            int startY = locate().getY() - impactRadius;
            int stopX = locate().getX() + impactRadius;
            int stopY = locate().getY() + impactRadius;

            if (startX <= 0)
                startX = 0;
            if (startY <= 0)
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

        /**
         * This methode decrease the distance between the MiniSquirrelBot and his Daddy
         * @return a new movevector
         */
        @Override
        public XY directionOfMaster() {
            return XYsupport.decreaseDistance(miniSquirrel.getLocation(), miniSquirrel.getLocation());
        }

        /**
         *
         * @return the remaining StepNumber
         */
        @Override
        public long getRemainingSteps() {
            return context.getGameDurationLeft();
        }
    }

}
