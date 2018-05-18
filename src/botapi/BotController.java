package botapi;

public interface BotController {
    /**
     * der nächste Schritt, den der Bot macht
     *
     * @param view
     */
    void nextStep(ControllerContext view);
}
