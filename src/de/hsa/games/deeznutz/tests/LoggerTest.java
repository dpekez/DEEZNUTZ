import de.hsa.games.deeznutz.Launcher;
import org.junit.Test;

import java.util.logging.Logger;

public class LoggerTest {

    @Test
    public void logLevelTester() {
        Logger.getLogger(Launcher.class.getName()).finest("Finest");
        Logger.getLogger(Launcher.class.getName()).finer("Finer");
        Logger.getLogger(Launcher.class.getName()).fine("Fine");
        Logger.getLogger(Launcher.class.getName()).config("Config");
        Logger.getLogger(Launcher.class.getName()).info("Info");
        Logger.getLogger(Launcher.class.getName()).warning("Warning");
        Logger.getLogger(Launcher.class.getName()).severe("Severe");
    }

}
