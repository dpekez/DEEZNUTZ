package botapi;

public interface BotControllerFactory {
    BotController createMiniBotController();

    BotController createMasterBotController();
}
