package botimpls.mozartuss;

import botapi.BotController;
import botapi.BotControllerFactory;

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
