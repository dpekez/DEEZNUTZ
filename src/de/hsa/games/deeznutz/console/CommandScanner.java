package de.hsa.games.deeznutz.console;

import de.hsa.games.deeznutz.Launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


class CommandScanner {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private CommandTypeInfo[] commandTypeInfos;
    private BufferedReader inputReader;

    CommandScanner(CommandTypeInfo[] commandTypeInfos, BufferedReader inputReader) {
        this.commandTypeInfos = commandTypeInfos;
        this.inputReader = inputReader;
    }

    Command next() {

        // get user input
        String userInput = null;
        try {
            userInput = inputReader.readLine();
            logger.finest("Input : " + userInput);
        } catch (IOException e) {
            try {
                throw new ScanException("Input reader ERROR");
            } catch (ScanException ex) {
                logger.warning(ex.getMessage());
            }
        }

        // clean and split user input
        assert userInput != null;
        userInput = userInput.trim();
        String[] splittedInput = userInput.split(" ");

        // command entered by user available?
        CommandTypeInfo command = null;
        for (CommandTypeInfo cmdti : commandTypeInfos) {
            if (splittedInput[0].equalsIgnoreCase(cmdti.getName())) {
                command = cmdti;
            }
        }
        if (command == null)
            throw new ScanException("command <" + splittedInput[0] + "> not available");

        // validate parameters
        Object[] parameters = new Object[splittedInput.length - 1];
        if (!(validateParams(command.getParamTypes(), parameters, splittedInput)) && (command.getParamTypes().length == splittedInput.length - 1)) {
            try {
                throw new ScanException("parameters wrong");
            } catch (ScanException ex) {
                logger.warning(ex.getMessage());
            }
        }
        return new Command(command, parameters);
    }

    private boolean validateParams(Class<?>[] paramTypes, Object[] params, String[] splitCommand) {

        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i].equals(int.class)) {
                params[i] = Integer.parseInt(splitCommand[i + 1]);
            } else if (paramTypes[i].equals(float.class)) {
                params[i] = Float.parseFloat(splitCommand[i + 1]);
            } else if (paramTypes[i].equals(String.class)) {
                params[i] = splitCommand[i + 1];
            } else {
                return false;
            }
        }
        return true;
    }
}
