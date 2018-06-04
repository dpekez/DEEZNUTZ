package de.hsa.games.deeznutz.botimpls.test;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.BotControllerFactory;

public class TestFactory implements BotControllerFactory {

    @Override
    public BotController createMasterBotController() {
        return new TestMaster();
    }

    @Override
    public BotController createMiniBotController() {
        return new TestMaster();
    }

}
