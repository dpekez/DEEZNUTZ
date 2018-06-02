package de.hsa.games.deeznutz.botimpls.potato;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.SpawnException;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;

public class PotatoMaster implements BotController {

    @Override
    public void nextStep(ControllerContext context) {
        context.move(XYsupport.generateRandomMoveVector());

        try {
            context.spawnMiniBot(new XY(0, 0), 100);
        } catch (SpawnException e) {
            e.printStackTrace();
        }

    }

}
