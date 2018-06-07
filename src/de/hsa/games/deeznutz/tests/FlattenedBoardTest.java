import de.hsa.games.deeznutz.core.*;
import de.hsa.games.deeznutz.entities.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class FlattenedBoardTest {
    private Board board = new Board(new BoardConfig("test.properties"));
    private EntityContext context = new FlattenedBoard(board);

    private HandOperatedMasterSquirrel handOperatedMasterSquirrel = new HandOperatedMasterSquirrel(new XY(11, 8), "hand1");
    private HandOperatedMasterSquirrel handOperatedMasterSquirrel2 = new HandOperatedMasterSquirrel(new XY(8, 7), "hand2");
    private HandOperatedMasterSquirrel handOperatedMasterSquirrel3 = new HandOperatedMasterSquirrel(new XY(24, 18), "hand3");
    private HandOperatedMasterSquirrel handOperatedMasterSquirrel4 = new HandOperatedMasterSquirrel(new XY(23, 28), "hand4");

    private MiniSquirrel miniSquirrel = new MiniSquirrel(200, new XY(18, 17), handOperatedMasterSquirrel2);

    @Before
    public void initBoard() {
        board.insert(new Wall(new XY(11, 9)));
        board.insertMaster(handOperatedMasterSquirrel);
        board.insertMaster(handOperatedMasterSquirrel2);
        board.insertMaster(handOperatedMasterSquirrel3);
        board.insertMaster(handOperatedMasterSquirrel4);
        board.insert(miniSquirrel);
    }

    @Test
    public void collisionWithWall() {
        context.tryMove(handOperatedMasterSquirrel, XY.ZERO_ZERO);
        //      7  8  9 10 11 12 13 X
        //   6  .  .  .  .  .  .  .
        //   7  .  .  .  .  .  .  .
        //   8  .  .  .  .  M  .  .
        //   9  .  .  .  .  #  .
        //   0  .  .  .  .  .  .  .
        //   Y
        context.tryMove(handOperatedMasterSquirrel, XY.DOWN);
        assertEquals(3, handOperatedMasterSquirrel.stunnedRounds);
        assertEquals(new XY(11, 8), handOperatedMasterSquirrel.getLocation());
    }

    @Test
    public void badBeastBiteMasterSquirrel() {
        BadBeast badBeast = new BadBeast(new XY(8, 9));
        board.insert(badBeast);

        //The BadBeast has to wait 4 Steps
        //      4  5  6  7  8  9  10 X
        //   6  .  .  .  .  .  .  .
        //   7  .  .  .  .  M  .  .
        //   8  .  .  .  .  .  .  .
        //   9  .  .  .  .  B  .  .
        //  10  .  .  .  .  .  .  .
        //   Y
        // The position of the BadBeast and the Master

        context.tryMove(handOperatedMasterSquirrel2, XY.ZERO_ZERO);

        for (int i = 0; i < 4; i++) {
            badBeast.nextStep(context);
        }

        //After waiting 4 steps
        //      4  5  6  7  8  9  10 X
        //   6  .  .  .  .  .  .  .
        //   7  .  .  .  .  M  .  .
        //   8  .  .  .  .  B  .  .
        //   9  .  .  .  .  .  .  .
        //  10  .  .  .  .  .  .  .
        //   Y
        // The position of the BadBeast and the Master

        assertEquals(badBeast.getLocation(), new XY(8, 8));
        assertEquals(1000, handOperatedMasterSquirrel2.getEnergy());

        //BadBeast decrease Energy of Player (1000 - 150)
        //And BadBeast decrease his BiteCounter (7 -> 6)

        for (int i = 0; i < 4; i++) {
            badBeast.nextStep(context);
        }

        assertEquals(850, handOperatedMasterSquirrel2.getEnergy());
        assertEquals(6, badBeast.getBitesLeft());

        for (int i = badBeast.getBitesLeft(); i > 0; i--) {
            for (int j = 0; j < 4; j++) {
                badBeast.nextStep(context);
            }
        }

        //BadBeast die after 7 Bites
        //      4  5  6  7  8  9  10 X
        //   6  .  .  .  .  .  .  .
        //   7  .  .  .  .  M  .  .
        //   8  .  .  .  .  .  .  .
        //   9  .  .  .  .  .  .  .
        //  10  .  .  .  .  .  .  .
        //   Y
        //Player actually has the same coordinate than before

        assertEquals(0, handOperatedMasterSquirrel2.getEnergy());
        assertEquals(new XY(8, 7), handOperatedMasterSquirrel2.getLocation());

        //No the filed in front of the Player is empty so he can move
        //      4  5  6  7  8  9  10 X
        //   6  .  .  .  .  .  .  .
        //   7  .  .  .  .  .  .  .
        //   8  .  .  .  .  M  .  .
        //   9  .  .  .  .  .  .  .
        //  10  .  .  .  .  .  .  .
        //   Y

        context.tryMove(handOperatedMasterSquirrel2, XY.DOWN);

        assertEquals(new XY(8, 8), handOperatedMasterSquirrel2.getLocation());
    }

    @Test
    public void badBeastBiteMiniSquirrel() {
        BadBeast badBeast = new BadBeast(new XY(20, 19));
        board.insert(badBeast);

        //The BadBeast has to wait 4 Steps
        //      14 15 16 17 18 19 20 X
        //   16  .  .  .  .  .  .  .
        //   17  .  .  .  .  m  .  .
        //   18  .  .  .  .  .  .  .
        //   19  .  .  .  .  .  .  B
        //   20  .  .  .  .  .  .  .
        //   Y
        // The position of the BadBeast and the MiniSquirrel

        context.tryMove(miniSquirrel, XY.ZERO_ZERO);

        for (int i = 0; i <= badBeast.getStepCount(); i++) {
            badBeast.nextStep(context);
        }

        //After waiting 4 steps
        //      14 15 16 17 18 19 20 X
        //   16  .  .  .  .  .  .  .
        //   17  .  .  .  .  m  .  .
        //   18  .  .  .  .  .  B  .
        //   19  .  .  .  .  .  .  .
        //   20  .  .  .  .  .  .  .
        //   Y
        // The position of the BadBeast and the MiniSquirrel

        assertEquals(badBeast.getLocation(), new XY(19, 18));
        assertEquals(200, miniSquirrel.getEnergy());

        //BadBeast decrease Energy of MiniSquirrel (200 - 150)
        //And BadBeast decrease his BiteCounter (7 -> 6)
        //      14 15 16 17 18 19 10 X
        //   16  .  .  .  .  m  .  .
        //   17  .  .  .  .  B  .  .
        //   18  .  .  .  .  .  .  .
        //   19  .  .  .  .  .  .  .
        //   20  .  .  .  .  .  .  .
        //   Y

        context.tryMove(miniSquirrel, XY.UP);

        assertEquals(miniSquirrel.getLocation(), new XY(18, 16));

        for (int i = 0; i <= badBeast.getStepCount(); i++) {
            badBeast.nextStep(context);
        }

        assertEquals(badBeast.getLocation(), new XY(18, 17));

        for (int i = 0; i <= badBeast.getStepCount(); i++) {
            badBeast.nextStep(context);
        }

        assertEquals(50, miniSquirrel.getEnergy());
        assertEquals(6, badBeast.getBitesLeft());

        for (int i = 0; i <= badBeast.getStepCount(); i++) {
            badBeast.nextStep(context);
        }

        //The BadBest kill the MiniSquirrel

        assertEquals(EntityType.NOTHING, context.getEntityType(new XY(18, 16)));

        //The BadBeast move to the old position of the dead MiniSquirrel
        //      14 15 16 17 18 19 10 X
        //   16  .  .  .  .  B  .  .
        //   17  .  .  .  .  .  .  .
        //   18  .  .  .  .  .  .  .
        //   19  .  .  .  .  .  .  .
        //   20  .  .  .  .  .  .  .
        //   Y

        context.tryMove(badBeast, XY.UP);

        assertEquals(EntityType.BAD_BEAST, context.getEntityType(new XY(18, 16)));
    }

    @Test
    public void goodEntityTryToFlee() {
        GoodBeast goodBeast = new GoodBeast(new XY(25, 19));
        board.insert(goodBeast);

        //The position of GoodBeast and MasterSquirrel
        //      24 25 26 27 28 29 30 X
        //   17  .  .  .  .  .  .  .
        //   18  M  .  .  .  .  .  .
        //   19  .  G  .  .  .  .  .
        //   20  .  .  .  .  .  .  .
        //   21  .  .  .  .  .  .  .
        //   Y

        context.tryMove(handOperatedMasterSquirrel3, XY.ZERO_ZERO);
        int firstDestination = (int) goodBeast.getLocation().distanceFrom(handOperatedMasterSquirrel3.getLocation());

        for (int i = 0; i < 4; i++) {
            goodBeast.nextStep(context);
        }

        int secondDestination = (int) goodBeast.getLocation().distanceFrom(handOperatedMasterSquirrel3.getLocation());

        //The new position of GoodBeast
        //      24 25 26 27 28 29 30 X
        //   17  .  .  .  .  .  .  .
        //   18  M  .  .  .  .  .  .
        //   19  .  .  .  .  .  .  .
        //   20  .  .  G  .  .  .  .
        //   21  .  .  .  .  .  .  .
        //   Y

        assertTrue(secondDestination > firstDestination);

    }

    @Test
    public void masterSquirrelEatBadBeast() {
        BadBeast badBeast = new BadBeast(new XY(24, 27));
        board.insert(badBeast);

        //The Position of both entities
        //      21 22  23 24 25 26 27 X
        //   26  .  .  .  .  .  .  .
        //   27  .  .  .  B  .  .  .
        //   28  .  .  M  .  .  .  .
        //   29  .  .  .  .  .  .  .
        //   30  .  .  .  .  .  .  .
        //   Y

        context.tryMove(handOperatedMasterSquirrel4, XY.ZERO_ZERO);
        assertEquals(1000, handOperatedMasterSquirrel4.getEnergy());

        //The HandOperatedMasterSquirrel kill the BadBeast.
        //He get +(-150) energy

        context.tryMove(handOperatedMasterSquirrel4, XY.RIGHT_UP);
        assertEquals(850, handOperatedMasterSquirrel4.getEnergy());
        assertSame(EntityType.NOTHING, context.getEntityType(new XY(24, 28)));

        context.tryMove(handOperatedMasterSquirrel4, XY.RIGHT_UP);
        assertEquals(badBeast.getLocation(), handOperatedMasterSquirrel4.getLocation());
    }

    @Test
    public void killAndreplace() {
        BadPlant badPlant = new BadPlant(new XY(13, 18));
        board.insert(badPlant);

        int badPlantCount = 0;

        context.killAndReplace(badPlant);
        assertEquals(context.getEntityType(badPlant.getLocation()),EntityType.NOTHING);

        //Count the quantity of BadPlants on the Board

        for (int x = 0; x < context.getSize().getX(); x++) {
            for (int y = 0; y < context.getSize().getY(); y++) {
                if (context.getEntityType(new XY(x, y)) == EntityType.BAD_PLANT) {
                    badPlantCount++;
                }
            }
        }

        //There is one more BadPlant because of the insert of BadPlant4

        assertEquals(1,badPlantCount);
    }

    @Test
    public void killEntity(){
        int badPlantCount = 0;
        GoodPlant goodPlant = new GoodPlant(new XY(17, 27));
        board.insert(goodPlant);

        context.kill(goodPlant);

        //scanning the whole Board for BadPlants

        for (int x = 0; x < context.getSize().getX(); x++) {
            for (int y = 0; y < context.getSize().getY(); y++) {
                if (context.getEntityType(new XY(x, y)) == EntityType.BAD_PLANT) {
                    badPlantCount++;
                }
            }
        }

        //There is no BadPlant left

        assertSame(0,badPlantCount);

        //There is Nothing at this location because the GoodPlant was killed and removed

        assertSame(context.getEntityType(goodPlant.getLocation()),EntityType.NOTHING);

    }
}
