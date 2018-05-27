import core.XY;
import core.XYsupport;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XYsupportTest {
    @Test
    public void inRange() {
        XY lowerLeftCorner = new XY(0, 0);
        XY upperRightCorner = new XY(10, 10);
        XY trueMiddle = new XY(5, 5);
        XY falseMiddle = new XY(3, 11);
        assertTrue(XYsupport.isInRange(trueMiddle, lowerLeftCorner, upperRightCorner));
        assertFalse(XYsupport.isInRange(falseMiddle, lowerLeftCorner, upperRightCorner));
    }

}
