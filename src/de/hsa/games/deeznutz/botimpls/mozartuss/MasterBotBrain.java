package de.hsa.games.deeznutz.botimpls.mozartuss;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.SpawnException;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterBotBrain implements BotController {
    private final static Logger logger = Logger.getLogger(MasterBotBrain.class.getName());

    private int energyToReachForSpawn = 400;

    @Override
    public void nextStep(ControllerContext view) {
        XY move = BotBrain.moveToNearestGoodEntity(view);
        try {
            if (view.getEnergy() < energyToReachForSpawn) {
                view.move(move);
            } else {
                XY spawnDirection = XYsupport.generateRandomMoveVector();
                if (BotBrain.checkSpawnField(view, view.locate().addVector(spawnDirection))) {
                    view.spawnMiniBot(spawnDirection, 100);
                    energyToReachForSpawn = energyToReachForSpawn + 1000;
                }
            }
        } catch (SpawnException e) {
            logger.log(Level.WARNING, "Unable zo spawn");
        }
    }
}
