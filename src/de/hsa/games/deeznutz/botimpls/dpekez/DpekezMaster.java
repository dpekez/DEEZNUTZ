package de.hsa.games.deeznutz.botimpls.dpekez;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.core.XYsupport;

public class DpekezMaster implements BotController {

    @Override
    public void nextStep(ControllerContext context) {

        context.move(XYsupport.generateRandomMoveVector());

    }

}
