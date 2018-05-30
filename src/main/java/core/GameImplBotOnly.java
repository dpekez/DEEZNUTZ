package core;

import console.ConsoleUI;
import entities.MasterSquirrelBot;

public class GameImplBotOnly extends Game {
    private MasterSquirrelBot mainMasterSquirrelBot;
    private MasterSquirrelBot secondaryMasterSquirrelBot;

    public GameImplBotOnly(boolean threaded, BoardConfig boardConfig) {
        super(new State(boardConfig));
        super.threaded = threaded;
        ui = new ConsoleUI(state, threaded);

        // create and insert bot
        mainMasterSquirrelBot = state.getBoard().createBot(boardConfig.getMainBotPath());
        secondaryMasterSquirrelBot = state.getBoard().createBot(boardConfig.getSecondaryBotPath());
        state.getBoard().insert(mainMasterSquirrelBot);
        state.getBoard().insert(secondaryMasterSquirrelBot);
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
        return "Bot 1 Energy: " + mainMasterSquirrelBot.getEnergy() +
               " Bot 2 Energy: " + secondaryMasterSquirrelBot.getEnergy();
    }

}
