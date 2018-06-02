package de.hsa.games.deeznutz.botimpls.dpekez;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.BotControllerFactory;

public class DpekezFactory implements BotControllerFactory {

    public BotController createMasterBotController() {
        return new DpekezMaster();
    }

    public BotController createMiniBotController() {
        return new DpekezMini();
    }

}
