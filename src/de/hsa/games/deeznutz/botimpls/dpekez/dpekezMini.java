package de.hsa.games.deeznutz.botimpls.dpekez;

import de.hsa.games.deeznutz.botapi.BotController;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import de.hsa.games.deeznutz.core.XYsupport;

public class dpekezMini implements BotController {

    @Override
    public void nextStep(ControllerContext view) {
        view.move(XYsupport.generateRandomMoveVector());
    }

}
