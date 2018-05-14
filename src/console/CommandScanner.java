package console;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;


public class CommandScanner {

    private CommandTypeInfo[] commandTypeInfo;
    private BufferedReader inputReader;
    private PrintStream outputStream;


    public CommandScanner(CommandTypeInfo[] commandTypeInfo, BufferedReader inputReader, PrintStream outputStream) {
        this.commandTypeInfo = commandTypeInfo;
        this.inputReader = inputReader;
        this.outputStream = outputStream;
    }

    public boolean validateParams(Class<?>[] paramTypes, Object[] params, String[] splitCommand) {

        for(int i=0; i<paramTypes.length; i++) {
            if(paramTypes[i].equals(int.class)) {
                //Versuch als int zu casten
                params[i]=Integer.parseInt(splitCommand[i+1]);
            }
            else if(paramTypes[i].equals(float.class)) {
                //Versuch als float zu casten
                params[i]=Float.parseFloat(splitCommand[i+1]);
            }
            else if(paramTypes[i].equals(String.class)) {
                //Versuch als String zu casten
                params[i]=splitCommand[i+1];
            }
            else {
                //wenn keins der Versuche klappt wird Kommando nicht angenommen
                return false;
            }
        }
        return true;
    }

    public Command next() throws ScanException {

        System.out.println("Enter a command please: ");


        // Get user input
        String input;
        try {
            input = inputReader.readLine();
        } catch (IOException e) {
            throw new ScanException("IOException");
        }


        // Remove trailing and pending whitespace
        input = input.trim();


        // Split input after every whitespace and store it in string array
        String[] splitInput = input.split(" ");


        // Search for matching command type
        CommandTypeInfo command = null;
        for(CommandTypeInfo cti: commandTypeInfo) {
            if(cti.getName().equals(splitInput[0]))
                command = cti;
        }


        // Throw exception if command is not available
        if(command == null)
            throw new ScanException("Error: Command <" + splitInput[0] + "> doesn't exist!");


        // Validate parameters
        Object[] parameters = new Object[splitInput.length];



        if(command.getParamTypes().length == splitInput.length - 1) {
            validateParams(command.getParamTypes(), parameters, splitInput);
        } else
            throw new ScanException("Error: Parameters wrong!");


        return new Command(command, parameters);
    }

}
