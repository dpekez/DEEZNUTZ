package core;

import console.ConsoleUI;
import entities.MasterSquirrelBot;

public class GameImplBotOnly extends Game {
    private MasterSquirrelBot mainMasterSquirrelBot;
    private MasterSquirrelBot secondaryMasterSquirrelBot;
    private String mainMasterSquirrelBotInfo;
    private String secondaryMasterSquirrelBotInfo;

    public GameImplBotOnly(boolean threaded, BoardConfig boardConfig) {
        super(new State(boardConfig));
        super.threaded = threaded;
        ui = new ConsoleUI(state, threaded);

        // create and insert bots
        mainMasterSquirrelBot = state.getBoard().createBot(boardConfig.getMainBotPath());
        secondaryMasterSquirrelBot = state.getBoard().createBot(boardConfig.getSecondaryBotPath());
        state.getBoard().insert(mainMasterSquirrelBot);
        state.getBoard().insert(secondaryMasterSquirrelBot);
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
        return mainMasterSquirrelBotInfo + " Energy: " + mainMasterSquirrelBot.getEnergy() + " VS. " +
                secondaryMasterSquirrelBotInfo + " Energy: " + secondaryMasterSquirrelBot.getEnergy();
    }

}
