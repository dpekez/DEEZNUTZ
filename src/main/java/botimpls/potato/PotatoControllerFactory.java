package botimpls.potato;

import botapi.BotController;
import botapi.BotControllerFactory;

public class PotatoControllerFactory implements BotControllerFactory {

    @Override
    public BotController createMasterBotController() {
        return new PotatoController();
    }

    @Override
    public BotController createMiniBotController() {
        return new PotatoController();
    }

}
