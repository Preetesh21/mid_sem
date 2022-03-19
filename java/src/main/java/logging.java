import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class logging {

    public static void writeLog(FileHandler fh, String info) {
        Logger logger = Logger.getLogger("MyLog");
        try {
            // This block configures the logger with handler and formatter
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            // the following statement is used to log any messages
            logger.info(info);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
