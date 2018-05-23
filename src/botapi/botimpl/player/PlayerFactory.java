package botapi.botimpl.player;

import botapi.BotController;
import botapi.BotControllerFactory;

public class PlayerFactory implements BotControllerFactory {
    @Override
    public BotController createMiniBotController() {
        return new MiniPlayer();
    }

    @Override
    public BotController createMasterBotController() {
        return new MasterPlayer();
    }
}
