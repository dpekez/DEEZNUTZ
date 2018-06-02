import de.hsa.games.deeznutz.botapi.BotControllerFactory;
import de.hsa.games.deeznutz.botimpls.mozartuss.BrainFactory;
import de.hsa.games.deeznutz.core.Board;
import de.hsa.games.deeznutz.core.BoardConfig;
import de.hsa.games.deeznutz.core.EntityContext;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.entities.MasterSquirrel;
import de.hsa.games.deeznutz.entities.MasterSquirrelBot;
import de.hsa.games.deeznutz.entities.MiniSquirrelBot;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ControllerContextTest {

    MasterSquirrelBot masterBot;
    MasterSquirrel master;
    MiniSquirrelBot miniBot;
    EntityContext context;
    XY spawnPositionMasterBot = new XY(40, 30);
    XY spawnPositionMiniBot = new XY(42, 35);
    Board board;
    MasterSquirrelBot.ControllerContextImpl viewMaster;
    BotControllerFactory botControllerFactory;
    BrainFactory factory;
    BoardConfig boardConfig;

    @Before
    public void init() {
        masterBot = new MasterSquirrelBot(spawnPositionMasterBot, factory);

        board.createBot(boardConfig.getMainBotPath());
    }

    public void tearDown() {
        viewMaster = null;
    }

    @Test
    public void locate() {
        viewMaster = masterBot.new ControllerContextImpl(context);

        assertEquals(spawnPositionMasterBot, viewMaster.locate());
    }


}
