package de.hsa.games.deeznutz.console;

public interface CommandTypeInfo {

    String getName();

    String getHelpText();

    Class<?>[] getParamTypes();

}
