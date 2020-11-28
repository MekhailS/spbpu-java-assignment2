package mekhails.executor;

import java.util.logging.Logger;

class Log {

     enum ERROR {
        COMMAND_PROMPT("error with command prompt arguments"),
        CONFIG("error occurred with config file"),
        READER("error while reading/opening input file"),
        WRITER("error while writing/opening output file");

        ERROR(String name_) { name = name_;}

        public final String name;
    }
}
