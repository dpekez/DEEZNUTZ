package botimpls.dpekez;

import botapi.BotController;
import botapi.BotControllerFactory;

public class dpekezFactory implements BotControllerFactory {

    public BotController createMasterBotController() {
        return new dpekezMaster();
    }

    public BotController createMiniBotController() {
        return new dpekezMini();
    }

}
