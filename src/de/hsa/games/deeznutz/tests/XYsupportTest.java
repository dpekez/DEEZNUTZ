import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;
import de.hsa.games.deeznutz.entities.BadBeast;
import de.hsa.games.deeznutz.entities.Entity;
import org.junit.Test;

import static org.junit.Assert.*;

public class XYsupportTest {

    @Test
    public void testRandomMoveVector() {
        int tries = 1000;
        boolean realRandom = false;
        int proportionOfTries = tries / 10;
        XY[] randomMoveVectores = new XY[tries];
        int counterDown = 0, counterUp = 0, counterRight = 0, counterLeft = 0, counterNull = 0,
                counterLeftUp = 0, counterLeftDown = 0, counterRightUp = 0, counterRightDown = 0;

        //This forLoop genrate tires * radomMoveVectors and save them into an Array

        for (int i = 0; i < tries; i++) {
            randomMoveVectores[i] = XYsupport.generateRandomMoveVector();
        }

        //This checks how often a MoveVector appears in the Array

        for (XY moveVectore : randomMoveVectores) {
            if (moveVectore.equals(new XY(0, 1))) {
                counterDown++;
            } else if (moveVectore.equals(new XY(0, -1))) {
                counterUp++;
            } else if (moveVectore.equals(new XY(1, 0))) {
                counterRight++;
            } else if (moveVectore.equals(new XY(-1, 0))) {
                counterLeft++;
            } else if (moveVectore.equals(new XY(-1, -1))) {
                counterLeftUp++;
            } else if (moveVectore.equals(new XY(-1, 1))) {
                counterLeftDown++;
            } else if (moveVectore.equals(new XY(1, -1))) {
                counterRightUp++;
            } else if (moveVectore.equals(new XY(1, 1))) {
                counterRightDown++;
            } else if (moveVectore.equals(new XY(0, 0))) {
                counterNull++;
            }
        }

        //The RandomMoveVectorGenerator should generate as balanced as possible.
        //So there occur always at least 10 percent per type

        if (counterDown > proportionOfTries &&
                counterUp > proportionOfTries &&
                counterLeft > proportionOfTries &&
                counterLeftDown > proportionOfTries &&
                counterLeftUp > proportionOfTries &&
                counterRight > proportionOfTries &&
                counterRightDown > proportionOfTries &&
                counterRightUp > proportionOfTries) {
            realRandom = true;
        }

        assertTrue(realRandom);

        //The RandomMovevectorGenerator hopefully don't generate a x:0 y:0 (Move)vector.

        assertEquals(0, counterNull);

    }

    @Test
    public void testRandomLocation() {
        Entity[] entities = new Entity[1];
        XY location = XYsupport.generateRandomLocation(new XY(30, 10), entities);
        entities[0] = (new BadBeast(location));

        assertTrue(location.getX() >= 0 && location.getX() <= 30);
        assertTrue(location.getY() >= 0 && location.getY() <= 10);

    }

    @Test
    public void testDecreaseDistance() {
        XY start1 = new XY(3, 5);
        XY start2 = new XY(3, 9);

        XY target = new XY(5, 7);

        XY moveVector1 = XYsupport.decreaseDistance(start1, target);
        XY moveVector2 = XYsupport.decreaseDistance(start2, target);


        assertEquals(moveVector1, new XY(1, 1));
        assertEquals(moveVector2, new XY(1, -1));
    }

    @Test
    public void testIsInRange() {
        XY start = new XY(3, 3);
        XY target1 = new XY(6, 6);
        XY target2 = new XY(4, 5);
        int viewDistance = 6;

        assertTrue(XYsupport.isInRange(start, target2, viewDistance));
        assertFalse(XYsupport.isInRange(start, target1, viewDistance));

    }
}
