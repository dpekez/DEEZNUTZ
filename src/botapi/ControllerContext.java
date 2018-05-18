package botapi;

import core.EntityType;
import core.XY;

public interface ControllerContext {

    XY getViewLowerLeft();

    XY getViewUpperRight();

    EntityType getEntityAt(XY xy);

    void move(XY direction);

    void spawnMiniBot(XY direction, int energy);

    int getEnergy();
}
