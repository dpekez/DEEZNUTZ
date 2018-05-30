package botimpl.potato;

import botapi.BotController;
import botapi.ControllerContext;
import core.XYsupport;

public class PotatoController implements BotController {

    @Override
    public void nextStep(ControllerContext view) {
        view.move(XYsupport.generateRandomMoveVector());
    }

}
