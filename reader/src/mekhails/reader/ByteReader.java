package mekhails.reader;

import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.RC;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ByteReader extends AConfigurable implements IReader {

    /**
     * Vocabulary of Reader
     */
    private enum LexemeReader implements ILexeme
    {
        BUFFER_SIZE("buffer size", SemanticAnalyzer.Semantic.SIZE);

        LexemeReader(String nameInConfig_, SemanticAnalyzer.Semantic semantic_)
            {nameInConfig = nameInConfig_; semantic = semantic_;}

        @Override
        public SemanticAnalyzer.Semantic getSemantic() { return semantic; }
        public String getNameInConfig() { return nameInConfig; }

        private final String nameInConfig;
        private final SemanticAnalyzer.Semantic semantic;
    }

    @Override
    protected LexemeAndRule[] setOfRulesForVocabulary()
    {
        LexemeAndRule[] setRules =
                {
                        new LexemeAndRule(LexemeReader.BUFFER_SIZE, paramVal -> {
                            bufferSize = (Integer)paramVal;
                            return RC.CODE_SUCCESS;
                        })
                };

        return setRules;
    }

    @Override
    public RC setInputStream(FileInputStream fileInputStream)
    {
        bis = new BufferedInputStream(fileInputStream);
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC execute(byte[] b)
    {
        try
        {
            int bytesRead = 0;
            while (true)
            {
                byte[] buffer = new byte[bufferSize];
                bytesRead = bis.read(buffer, 0, bufferSize);

                if (bytesRead == -1)
                    return RC.CODE_SUCCESS;

                if (bytesRead != buffer.length)
                    buffer = Arrays.copyOfRange(buffer, 0, bytesRead);

                RC code = consumer.execute(buffer);
                if (code != RC.CODE_SUCCESS)
                    return code;
            }
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, Log.ERROR.READER.name);
            return RC.CODE_FAILED_TO_READ;
        }
    }

    @Override
    public RC setConsumer(IExecutable o)
    {
        consumer = o;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setProducer(IExecutable o) { return RC.CODE_SUCCESS; }

    @Override
    public RC setConfig(String s)
    {
        try
        {
            FileInputStream cfgStream = new FileInputStream(s);

            return configure(cfgStream, logger);
        }
        catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, Log.ERROR.CONFIG.name);
            return RC.CODE_INVALID_INPUT_STREAM;
        }
    }


    public ByteReader(Logger logger_)
        {logger = logger_;}

    private int bufferSize;
    private IExecutable consumer;

    private BufferedInputStream bis;

    private final Logger logger;
}
