package de.hsa.games.deeznutz.botimpls.test;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.core.XYsupport;

public class TestMaster implements BotController {

    @Override
    public void nextStep(ControllerContext context) {
        context.move(XYsupport.generateRandomMoveVector());
    }

}
