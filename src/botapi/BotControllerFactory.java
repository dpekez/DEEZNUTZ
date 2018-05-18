package botapi;

public interface BotControllerFactory {
    /**
     * BotController für ein MiniSquirrel
     *
     * @return
     */
    BotController createMiniBotController();

    /**
     * BotController für ein MasterSquirrel
     *
     * @return
     */
    BotController createMasterBotController();
}
