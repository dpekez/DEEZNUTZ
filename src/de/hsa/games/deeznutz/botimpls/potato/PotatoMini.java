package de.hsa.games.deeznutz.botimpls.potato;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.core.XYsupport;

public class PotatoMini implements BotController {

    @Override
    public void nextStep(ControllerContext context) {
        context.move(XYsupport.generateRandomMoveVector());
    }

}
