package botapi.botimpl.brain;

import botapi.BotController;
import botapi.ControllerContext;
import botapi.SpawnException;
import core.XY;
import core.XYsupport;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterBotBrain implements BotController {
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private int energyToReachForSpawn = 200;
    private XY lastPosition = XY.ZERO_ZERO;

    @Override
    public void nextStep(ControllerContext view) {
        XY maxSize = view.getViewUpperRight();
        XY move = BotBrain.moveToNearestGoodEntity(view, maxSize);
        try {
            if (view.getEnergy() < energyToReachForSpawn) {
                lastPosition = view.locate().addVector(move);
                view.move(move);
            } else {
                XY spawnDirection = XYsupport.generateRandomMoveVector();
                if (BotBrain.checkSpawnField(view, view.locate().addVector(spawnDirection))) {
                    view.spawnMiniBot(spawnDirection, 1000);
                    energyToReachForSpawn = energyToReachForSpawn + 1000;
                }
            }
        } catch (SpawnException e) {
            logger.log(Level.WARNING, "Nicht genug Energy");
        }
    }
}
