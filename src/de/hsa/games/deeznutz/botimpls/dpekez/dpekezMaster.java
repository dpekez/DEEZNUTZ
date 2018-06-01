package de.hsa.games.deeznutz.botimpls.dpekez;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.botapi.SpawnException;
import de.hsa.games.deeznutz.core.XY;

public class dpekezMaster implements BotController {

    @Override
    public void nextStep(ControllerContext view) {
        try {
            view.spawnMiniBot(new XY(-1, 0), 200);
        } catch (SpawnException e) {
            e.printStackTrace();
        }
    }

}
