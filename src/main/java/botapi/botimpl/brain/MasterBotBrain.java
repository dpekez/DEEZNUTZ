package botapi.botimpl.brain;

import botapi.BotController;
import botapi.ControllerContext;
import botapi.SpawnException;
import core.XY;
import core.XYsupport;

public class MasterBotBrain implements BotController {
    private int energyToReachForSpawn = 3000;
    private XY lastPosition = XY.ZERO_ZERO;

    @Override
    public void nextStep(ControllerContext view) {
        XY maxSize = view.getViewUpperRight();
        XY move = BotBrain.moveToNearestGoodEntity(view, maxSize);
        if (view.getEnergy() < energyToReachForSpawn) {
            move = BotBrain.stuck(view, move);
            lastPosition = view.locate().addVector(move);
            view.move(move);
        } else {
            XY spawndirection = XYsupport.generateRandomMoveVector();
            if (BotBrain.freeFieldSpace(view, view.locate().addVector(spawndirection))) {
                try {
                    view.spawnMiniBot(spawndirection, 2000);
                    energyToReachForSpawn = energyToReachForSpawn + 2000;
                } catch (SpawnException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
