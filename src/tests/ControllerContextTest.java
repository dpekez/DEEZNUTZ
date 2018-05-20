package tests;

import botapi.BotControllerFactory;
import core.Board;
import core.EntityContext;
import core.XY;
import entities.MasterSquirrel;
import entities.MasterSquirrelBot;
import entities.MiniSquirrelBot;
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
    MiniSquirrelBot.ControllerContextImpl viewMini;
    BotControllerFactory botControllerFactory;

    @Before
    public void init() {
        miniBot = new MiniSquirrelBot(0, spawnPositionMiniBot, master);

        board.insertMiniSquirrel(0, spawnPositionMiniBot, master);
    }

    public void tearDown() {
        viewMini = null;
    }

    @Test
    public void locate() {
        viewMini = new MiniSquirrelBot.ControllerContextImpl(context, miniBot);
        assertEquals(spawnPositionMiniBot, viewMini.locate());
    }


}
