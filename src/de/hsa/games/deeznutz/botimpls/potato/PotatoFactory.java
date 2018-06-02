package de.hsa.games.deeznutz.botimpls.potato;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.BotControllerFactory;

public class PotatoFactory implements BotControllerFactory {

    @Override
    public BotController createMasterBotController() {
        return new PotatoMaster();
    }

    @Override
    public BotController createMiniBotController() {
        return new PotatoMini();
    }

}
