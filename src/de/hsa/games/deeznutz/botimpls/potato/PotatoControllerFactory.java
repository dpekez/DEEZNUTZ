package de.hsa.games.deeznutz.botimpls.potato;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.BotControllerFactory;

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
