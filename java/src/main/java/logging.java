import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class logging {

    public static void writeLog(FileHandler fh, String info) {
        Logger logger = Logger.getLogger("MyLog");
//        FileHandler fh;

        try {
            // This block configures the logger with handler and formatter
            //fh = new FileHandler("/home/hp/Desktop/mid_sem_java/java/src/main/java/LogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info(info);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        FileHandler fh;
        fh = new FileHandler("/home/hp/Desktop/mid_sem_java/java/src/main/java/LogFile.log");
        writeLog(fh,"Hello World 1");
        writeLog(fh,"Hello World 2");
    }
}
