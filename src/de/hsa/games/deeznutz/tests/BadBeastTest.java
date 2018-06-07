import de.hsa.games.deeznutz.core.EntityContext;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.entities.*;
import junit.framework.Assert;
import org.junit.Test;

public class BadBeastTest {

    private BadBeast badBeast = new BadBeast(new XY(3, 3));

    /**
     * Method: nextStep(EntityContext entityContext)
     */
    @Test
    public void testNextStep() {
        EntityContextImpl context = new EntityContextImpl();

        badBeast.nextStep(context);

        Assert.assertFalse(context.moved);

        for (int i = 0; i <= badBeast.getStepCount(); i++) {
            badBeast.nextStep(context);
        }
        Assert.assertTrue(context.moved);

        Assert.assertTrue(context.object instanceof XY);

        Assert.assertEquals(new XY(3, 4), badBeast.getLocation());

    }

    /**
     * Method: bite()
     */
    @Test
    public void testBite() {
        int bitsLeft = badBeast.getBitesLeft();

        badBeast.bite();

        Assert.assertEquals(bitsLeft - 1, badBeast.getBitesLeft());
    }

    private class MasterSquirrelImpl extends MasterSquirrel {

        MasterSquirrelImpl(XY location, String name) {
            super(location, name);
        }
    }

    private class EntityContextImpl implements EntityContext {
        private boolean moved = false;
        private Object object = null;

        @Override
        public XY getSize() {
            return new XY(15, 15);
        }

        @Override
        public void tryMove(MiniSquirrel miniSquirrel, XY moveDirection) {
            moved = true;
            object = moveDirection;

        }

        @Override
        public void tryMove(GoodBeast goodBeast, XY moveDirection) {
            moved = true;
            object = moveDirection;

        }

        @Override
        public void tryMove(BadBeast badBeast, XY moveDirection) {
            badBeast.move(moveDirection);
            moved = true;
            object = moveDirection;

        }

        @Override
        public void tryMove(MasterSquirrel masterSquirrel, XY moveDirection) {
            moved = true;
            object = moveDirection;

        }

        @Override
        public Player nearestPlayerEntity(XY pos) {
            object = pos;
            MasterSquirrelImpl masterSquirrel = new MasterSquirrelImpl(new XY(15, 8), "master");
            return new MiniSquirrel(200, new XY(3, 5), masterSquirrel);

        }

        @Override
        public void kill(Entity entity) {
            object = entity;

        }

        @Override
        public void killAndReplace(Entity entity) {
            object = entity;

        }

        @Override
        public Entity getEntity(XY xy) {
            object = xy;
            return null;
        }

        @Override
        public EntityType getEntityType(XY xy) {
            object = xy;
            return null;
        }

        @Override
        public int getWaitingTimeBeast() {
            return 7;
        }

        @Override
        public int getPlayerViewDistance() {
            return 6;
        }

        @Override
        public void insertEntity(Entity entity) {
            object = entity;

        }

        @Override
        public int getGameDurationLeft() {
            return 0;
        }

        @Override
        public void setImplosionRadius(int x) {
            object = x;

        }

        @Override
        public void addImplodingMinis(MiniSquirrel mini) {
            object = mini;

        }

        @Override
        public void removeImplodingMinis() {

        }
    }

}
