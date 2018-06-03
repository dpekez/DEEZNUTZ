package de.hsa.games.deeznutz.botimpls.potato;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.SpawnException;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

import java.util.logging.Logger;

public class PotatoMaster implements BotController {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    @Override
    public void nextStep(ControllerContext context) {
        context.move(XYsupport.generateRandomMoveVector());

        if (context.getEnergy() >= 1000) {
            try {
                context.spawnMiniBot(new XY(0, 0), 100);
            } catch (SpawnException e) {
                logger.warning("Unable to Spawn MiniSquirrel (Potato)");
                e.printStackTrace();
            }
        }

    }

}
