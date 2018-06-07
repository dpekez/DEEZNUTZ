package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.entities.*;

public interface EntityContext {

    XY getSize();

    void tryMove(MiniSquirrel miniSquirrel, XY moveDirection);

    void tryMove(GoodBeast goodBeast, XY moveDirection);

    void tryMove(BadBeast badBeast, XY moveDirection);

    void tryMove(MasterSquirrel masterSquirrel, XY moveDirection);

    Player nearestPlayerEntity(XY pos);

    void kill(Entity entity);

    void killAndReplace(Entity entity);

    Entity getEntity(XY xy);

    EntityType getEntityType(XY xy);

    int getWaitingTimeBeast();

    int getPlayerViewDistance();

    void insertEntity(Entity entity);

    int getGameDurationLeft();

    void setImplosionRadius(int x);

    void addImplodingMinis(MiniSquirrel mini);

    void removeImplodingMinis();

}
