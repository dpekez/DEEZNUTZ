package de.hsa.games.deeznutz.botimpls.dpekez;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.BotControllerFactory;

public class dpekezFactory implements BotControllerFactory {

    public BotController createMasterBotController() {
        return new dpekezMaster();
    }

    public BotController createMiniBotController() {
        return new dpekezMini();
    }

}
