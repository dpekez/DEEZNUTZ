package core;

import entities.*;

public interface EntityContext {

    XY getSize();

    void tryMove(MiniSquirrel miniSquirrel, XY moveDirection);

    void tryMove(GoodBeast goodBeast, XY moveDirection);

    void tryMove(BadBeast badBeast, XY moveDirection);

    void tryMove(MasterSquirrel masterSquirrel, XY moveDirection);

    Player nearestPlayerEntity(XY pos);

    void kill(Entity entity);

    void killAndReplace(Entity entity);

    EntityType getEntityType(XY xy);

    int getWaitingTimeBeast();

    int getGoodBeastViewDistance();

    int getBadBeastViewDistance();

    int getPlayerViewDistance();

}