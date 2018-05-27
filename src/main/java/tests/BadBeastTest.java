import core.XY;
import entities.BadBeast;
import org.junit.Test;


public class BadBeastTest {

    @Test
    public void entityTypeTest() {
        BadBeast badBeast = new BadBeast(new XY(15, 33));
        System.out.println(badBeast.toString());
    }
}
