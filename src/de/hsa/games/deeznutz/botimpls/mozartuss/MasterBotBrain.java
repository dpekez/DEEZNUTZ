package de.hsa.games.deeznutz.botimpls.mozartuss;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.SpawnException;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterBotBrain implements BotController {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private int energyToReachForSpawn = 2000;

    @Override
    public void nextStep(ControllerContext view) {
        XY move = BotBrain.moveToNearestGoodEntity(view);
        try {
            if (view.getEnergy() < energyToReachForSpawn) {
                view.move(move);
            } else {
                XY spawnDirection = XYsupport.generateRandomMoveVector();
                if (BotBrain.checkSpawnField(view, view.locate().addVector(spawnDirection))) {
                    view.spawnMiniBot(spawnDirection, 500);
                    energyToReachForSpawn = energyToReachForSpawn + 2000;
                }
            }
        } catch (SpawnException e) {
            logger.warning("Unable to spawn MiniSquirrel (Brain)");
        }
    }
}
