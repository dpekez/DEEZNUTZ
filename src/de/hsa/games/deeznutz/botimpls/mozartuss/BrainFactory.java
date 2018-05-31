package de.hsa.games.deeznutz.botimpls.mozartuss;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.BotControllerFactory;

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
