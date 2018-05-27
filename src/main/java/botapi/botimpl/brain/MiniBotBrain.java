package botapi.botimpl.brain;

import botapi.BotController;
import botapi.ControllerContext;
import core.XY;

public class MiniBotBrain implements BotController {

    @Override
    public void nextStep(ControllerContext view) {
        XY move;

        if (view.getEnergy() > 1000) {
            //TODO bewegung
            move = view.directionOfMaster();
            view.move(move);
        }
    }
}
