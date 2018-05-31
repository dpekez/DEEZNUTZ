package de.hsa.games.deeznutz.botapi;

public interface BotControllerFactory {

    BotController createMasterBotController();
    BotController createMiniBotController();

}
