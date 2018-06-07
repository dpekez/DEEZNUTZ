import de.hsa.games.deeznutz.core.Board;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.entities.BadBeast;
import org.junit.Test;

public class BoardTest {
    private Board board = new Board();

    @Test
    public void testInsert() {
        BadBeast badBeast = new BadBeast(new XY(1, 1));
        board.insert(badBeast);


    }


}


