package de.hsa.games.deeznutz.console;

public interface CommandTypeInfo {

    String getCommandFxUI();

    String getName();

    String getHelpText();

    Class<?>[] getParamTypes();

}
