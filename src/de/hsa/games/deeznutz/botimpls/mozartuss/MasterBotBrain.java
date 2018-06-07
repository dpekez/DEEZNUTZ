package de.hsa.games.deeznutz.botimpls.mozartuss;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.SpawnException;
import de.hsa.games.deeznutz.core.XY;

import java.util.logging.Logger;

public class MasterBotBrain implements BotController {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private int energyToReachForSpawn = 2000;
    private BotBrain brain = new BotBrain();

    @Override
    public void nextStep(ControllerContext view) {
        XY move = brain.moveToNearestGoodEntity(view);
        try {
            if (view.getEnergy() < energyToReachForSpawn) {
                view.move(move);
            } else {
                XY spawnDirection = checkFreeSpawnField(view);
                int miniSquirrelEnergy = (int) (view.getEnergy() * 0.1);
                view.spawnMiniBot(spawnDirection, miniSquirrelEnergy);
                energyToReachForSpawn = energyToReachForSpawn + miniSquirrelEnergy;
            }
        } catch (SpawnException e) {
            logger.warning("Unable to spawn MiniSquirrel (Brain)");
        }
    }

    private XY checkFreeSpawnField(ControllerContext view) {
        XY[] allMoveVectors = {XY.DOWN, XY.UP, XY.LEFT, XY.LEFT_DOWN, XY.LEFT_UP, XY.RIGHT, XY.RIGHT_UP, XY.RIGHT_DOWN};
        XY finalSpawnDiection = null;
        for (XY spawnDirection : allMoveVectors) {
            if (brain.checkSpawnField(view, view.locate().addVector(spawnDirection))) {
                finalSpawnDiection = spawnDirection;
                break;
            }
        }
        return finalSpawnDiection;
    }
}
