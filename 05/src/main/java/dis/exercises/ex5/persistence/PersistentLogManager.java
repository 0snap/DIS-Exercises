package dis.exercises.ex5.persistence;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

public class PersistentLogManager extends PersistentFileWriter {

    public static final String CHECKPOINT_IDENTIFIER = "CHECKPOINT";

    private static final Logger LOG = LoggerFactory.getLogger(PersistentLogManager.class);
    private final Path logFile;
    private AtomicLong logSequenceNumber;

    public PersistentLogManager(String logFileName) throws IOException {
        logFile = Paths.get("./", logFileName);
        logSequenceNumber = initializeLSN();
    }

    private AtomicLong initializeLSN() throws IOException {
        return new AtomicLong(Files.lines(logFile).count());
    }

    public Long beginTransaction(String transactionId) {
        Long lsn = getLSN();
        persistToLog(lsn, transactionId, TransactionState.TRANSIENT);
        return lsn;
    }

    public Long commit(String transactionId) {
        Long lsn = getLSN();
        persistToLog(lsn, transactionId, TransactionState.COMMITTED);
        return lsn;
    }

    public Long write(String transactionId, int pageId, String data) {
        Long lsn = getLSN();
        persistToLog(lsn, transactionId, pageId, data);
        return lsn;
    }

    public Long markCheckpoint() {
        Long lsn = getLSN();
        persistToLog(lsn, CHECKPOINT_IDENTIFIER);
        return lsn;
    }

    private void persistToLog(Object... toLog) {
        appendToFile(logFile, toLog);
    }


    private Long getLSN() {
        return logSequenceNumber.incrementAndGet();
    }
}
