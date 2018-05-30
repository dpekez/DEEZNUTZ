package botimpl.mozartuss;

import botapi.BotController;
import botapi.BotControllerFactory;
import botimpl.mozartuss.MasterBotBrain;
import botimpl.mozartuss.MiniBotBrain;

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
