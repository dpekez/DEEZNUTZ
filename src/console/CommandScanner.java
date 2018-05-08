package console;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class CommandScanner {
    private CommandTypeInfo[] commandTypeInfo;
    private BufferedReader inputReader;
    private PrintStream outputStream;

    public CommandScanner(CommandTypeInfo[] commandTypeInfo, BufferedReader inputReader) {
        this.commandTypeInfo = commandTypeInfo;
        this.inputReader = inputReader;
    }

    public boolean validateParams(Class<?>[] paramTypes, Object[] params, String[] splitCommand) {
        for(int i=0;i<paramTypes.length;i++) {
            if(paramTypes[i].equals(int.class)) {
//				Versuch als int zu casten
                params[i]=Integer.parseInt(splitCommand[i+1]);
            }
            else if(paramTypes[i].equals(float.class)) {
//				Versuch als float zu casten
                params[i]=Float.parseFloat(splitCommand[i+1]);
            }
            else if(paramTypes[i].equals(String.class)) {
//				Versuch als String zu casten
                params[i]=splitCommand[i+1];
            }
            else {
//				wenn keins der Versuche klappt wird Kommando nicht angenommen
                return false;
            }
        }
        return true;
    }

    public Command next() throws ScanException, IOException {
        //Lesen der Eingabe
        System.out.println("Enter a command please: ");
        String command = "";
        //Leerräume nach Eingabe entfernen
        command = (inputReader.readLine().trim());
        //String nach Leerzeichen trennen
        String[] splitCommand = command.split(",");

        for (CommandTypeInfo i : commandTypeInfo) {
            if (i.getName().equals(splitCommand[0])) {
                if (i.getParamTypes().length == splitCommand.length - 1) {
                    Object[] params = new Object[splitCommand.length];
                    if(validateParams(i.getParamTypes(),params,splitCommand)) {
                        return new Command(i,params);
                    }
                }
            }
        }
        throw new ScanException("FEHLER: Kommando existiert nicht");
    }
}



