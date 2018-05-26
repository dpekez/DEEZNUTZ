package console;

import java.io.BufferedReader;
import java.io.IOException;


class CommandScanner {

    private CommandTypeInfo[] commandTypeInfos;
    private BufferedReader inputReader;

    CommandScanner(CommandTypeInfo[] commandTypeInfos, BufferedReader inputReader) {
        this.commandTypeInfos = commandTypeInfos;
        this.inputReader = inputReader;
    }

    public Command next() {

        // get user input
        String userInput;
        try {
            userInput = inputReader.readLine();
        } catch (IOException e) {
            throw new ScanException("input reader error");
        }

        // clean and split user input
        userInput = userInput.trim();
        String[] splittedInput = userInput.split(" ");

        // command entered by user available?
        CommandTypeInfo command = null;
        for (CommandTypeInfo cmdti: commandTypeInfos) {
            if(splittedInput[0].equalsIgnoreCase( cmdti.getName() )) {
                command = cmdti;
            }
        }
        if (command == null)
            throw new ScanException("command <" + splittedInput[0] + "> not available");

        // validate parameters
        Object[] parameters = new Object[splittedInput.length - 1];
        if ( (validateParams(command.getParamTypes(), parameters, splittedInput)) && (command.getParamTypes().length == splittedInput.length - 1) ) {

        } else
            throw new ScanException("parameters wrong");

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
