package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Game;
import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.console.ConsoleUI;
import de.hsa.games.deeznutz.entities.MasterSquirrelBot;

import java.util.logging.Logger;

public class GameImplBotOnly extends Game {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private MasterSquirrelBot mainMasterSquirrelBot;
    private MasterSquirrelBot secondaryMasterSquirrelBot;
    private String mainMasterSquirrelBotInfo;
    private String secondaryMasterSquirrelBotInfo;

    public GameImplBotOnly(boolean threaded, BoardConfig boardConfig) {
        super(new State(boardConfig));
        super.threaded = threaded;
        ui = new ConsoleUI(state, threaded);

        // create and insert bots
        mainMasterSquirrelBot = createBot(boardConfig.getMainBotPath());
        secondaryMasterSquirrelBot = createBot(boardConfig.getSecondaryBotPath());
        state.getBoard().insertMaster(mainMasterSquirrelBot);
        logger.finer("Insert first MasterSquirrelBot");
        state.getBoard().insertMaster(secondaryMasterSquirrelBot);
        logger.finer("Insert second MasterSquirrelBot");
        mainMasterSquirrelBotInfo = boardConfig.getMainBotPath();
        secondaryMasterSquirrelBotInfo = boardConfig.getSecondaryBotPath();
    }

    @Override
    public void processInput() {
        // Bot only implementation, no user interaction
    }

    @Override
    public void render() {
        ui.render(state.flattenedBoard());
    }

    @Override
    public String message() {
        return mainMasterSquirrelBotInfo + " Energy: " + mainMasterSquirrelBot.getEnergy() + "\nVS.\n" +
                secondaryMasterSquirrelBotInfo + " Energy: " + secondaryMasterSquirrelBot.getEnergy();
    }

}
