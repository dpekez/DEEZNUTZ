package botapi.botimpl.brain;

import botapi.BotController;
import botapi.ControllerContext;
import core.XY;

public class MiniBotBrain implements BotController {
    private XY lastposition = XY.ZERO_ZERO;

    @Override
    public void nextStep(ControllerContext view) {
        XY maxSize = view.getViewUpperRight();
        XY move;

        if (view.getEnergy() > 4000) {
            move = view.directionOfMaster();
            lastposition = view.locate().addVector(move);
            view.move(move);
        }
        move = BotBrain.moveToNearestGoodEntity(view, lastposition);
        lastposition = view.locate().addVector(move);
        view.move(move);
    }
}
