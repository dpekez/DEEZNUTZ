package console;

import java.io.BufferedReader;
import java.io.IOException;


class CommandScanner {

    private CommandTypeInfo[] commandTypeInfo;
    private BufferedReader inputReader;

    CommandScanner(CommandTypeInfo[] commandTypeInfo, BufferedReader inputReader) {
        this.commandTypeInfo = commandTypeInfo;
        this.inputReader = inputReader;
    }

    private boolean validateParams(Class<?>[] paramTypes, Object[] params, String[] splitCommand) {

        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i].equals(int.class)) {
                //Versuch als int zu casten
                params[i] = Integer.parseInt(splitCommand[i + 1]);
            } else if (paramTypes[i].equals(float.class)) {
                //Versuch als float zu casten
                params[i] = Float.parseFloat(splitCommand[i + 1]);
            } else if (paramTypes[i].equals(String.class)) {
                //Versuch als String zu casten
                params[i] = splitCommand[i + 1];
            } else {
                //wenn keins der Versuche klappt wird Kommando nicht angenommen
                return false;
            }
        }
        return true;
    }

    Command next() throws ScanException {

        System.out.println("Bitte Eingabe tätigen: ");
        String input;
        try {
            input = inputReader.readLine();
        } catch (IOException e) {
            throw new ScanException("IOException");
        }
        input = input.trim();
        String[] splitInput = input.split(" ");
        CommandTypeInfo command = null;
        for (CommandTypeInfo cti : commandTypeInfo) {
            if (cti.getName().equals(splitInput[0]))
                command = cti;
        }
        if (command == null)
            throw new ScanException("Error: Command <" + splitInput[0] + "> doesn't exist!");
        // Validate parameters
        Object[] parameters = new Object[splitInput.length];
        if (command.getParamTypes().length == splitInput.length - 1) {
            validateParams(command.getParamTypes(), parameters, splitInput);
        } else
            throw new ScanException("Error: Parameters wrong!");
        return new Command(command, parameters);
    }
}
