package botapi.botimpl;

import botapi.BotController;
import botapi.BotControllerFactory;
import botapi.botimpl.brain.MasterBotBrain;
import botapi.botimpl.brain.MiniBotBrain;


public class BrainFactory implements BotControllerFactory {
    @Override
    public BotController createMiniBotController() {
        return new MiniBotBrain();
    }

    @Override
    public BotController createMasterBotController() {
        return new MasterBotBrain();
    }

}


