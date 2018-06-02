import de.hsa.games.deeznutz.botimpls.mozartuss.BrainFactory;
import de.hsa.games.deeznutz.botimpls.mozartuss.BotBrain;
import de.hsa.games.deeznutz.core.Board;
import de.hsa.games.deeznutz.core.BoardConfig;
import de.hsa.games.deeznutz.core.EntityType;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.entities.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class KiMasterBot {
    private MasterSquirrelBot.ControllerContextImpl controllerContext;

    @Before
    public void setup() {
        Board board = new Board(new BoardConfig("default.properties"));
        MasterSquirrelBot masterSquirrelBot = new MasterSquirrelBot(new XY(15, 25), new BrainFactory());
        GoodBeast goodbeastOne = new GoodBeast(new XY(15, 15));
        GoodBeast goodbeastTwo = new GoodBeast(new XY(5, 5));
        BadBeast badBeastOne = new BadBeast(new XY(16, 16));
        BadBeast badBeastTwo = new BadBeast(new XY(16, 23));
        BadPlant badPlantOne = new BadPlant(new XY(7, 9));
        Wall wallOne = new Wall(new XY(11, 9));

        board.insert(goodbeastOne);
        board.insert(goodbeastTwo);
        board.insert(badBeastOne);
        board.insert(badBeastTwo);
        board.insert(badPlantOne);
        board.insert(wallOne);
        board.insert(masterSquirrelBot);

        controllerContext = masterSquirrelBot.new ControllerContextImpl(board.flatten());
    }

    @Test
    public void moveToEntities() {
        XY nearestBB = BotBrain.nearestEntity(controllerContext, EntityType.BAD_BEAST);
        XY nearestGB = BotBrain.nearestEntity(controllerContext, EntityType.GOOD_BEAST);

        assertTrue(controllerContext.locate().distanceFrom(nearestBB) < 5);
        assertTrue(controllerContext.locate().distanceFrom(nearestGB) < 31);
    }


}
