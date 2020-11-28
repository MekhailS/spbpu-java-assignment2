package mekhails.pipeline;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main
{
    private final static String LOGGER_FILE = "log_pipeline.log";
    private final static String LOGGER_NAME = "pipeline";
    private final static String LOG_INIT_PROBLEM = "cannot init logger";

    public static void main(String[] Args) {

        Logger logger = Logger.getLogger(LOGGER_NAME);

        FileHandler fh = null;

        try {
            fh = new FileHandler(LOGGER_FILE);
        } catch (IOException e) {
            System.out.println(LOG_INIT_PROBLEM);;
        }

        fh.setFormatter(new SimpleFormatter());

        logger.addHandler(fh);
        logger.setUseParentHandlers(false);

        if (Args == null || Args.length == 0)
        {
            logger.log(Level.SEVERE, Log.ERROR.COMMAND_PROMPT.name);
            return;
        }
        String configFilename = Args[0];

        Manager mng = new Manager(configFilename, logger);
        mng.configureAndConstructPipeline();

        if (mng.isEverythingAvailable())
            mng.run();
    }
}
