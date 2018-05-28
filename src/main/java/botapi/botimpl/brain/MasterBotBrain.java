package botapi.botimpl.brain;

import botapi.BotController;
import botapi.ControllerContext;
import core.XY;

public class MasterBotBrain implements BotController {
    private int energyToReachForSpawn = 3000;
    private XY lastPosition = XY.ZERO_ZERO;

    @Override
    public void nextStep(ControllerContext view) {
        XY maxSize = view.getViewUpperRight();
        XY move = BotBrain.moveToNearestGoodEntity(view, maxSize);
        if (view.getEnergy() < energyToReachForSpawn) {
            lastPosition = view.locate().addVector(move);
            view.move(move);
        }
    }
}
