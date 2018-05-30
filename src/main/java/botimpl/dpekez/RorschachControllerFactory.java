package botimpl.dpekez;

import botapi.BotController;
import botapi.BotControllerFactory;

public class RorschachControllerFactory implements BotControllerFactory {

    public BotController createMiniBotController() {
        return new RorschachController();
    }

    public BotController createMasterBotController() {
        return new RorschachController();
    }

}
