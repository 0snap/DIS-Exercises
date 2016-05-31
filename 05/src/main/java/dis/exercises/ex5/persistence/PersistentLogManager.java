package dis.exercises.ex5.persistence;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;

public class PersistentLogManager {

    private static final Logger LOG = LoggerFactory.getLogger(PersistentLogManager.class);
    private final String logFileName;
    private final Path logFile;
    private AtomicLong logSequenceNumber;

    public PersistentLogManager(String logFileName) throws IOException {
        this.logFileName = logFileName;
        logFile = Paths.get("./", logFileName);
        logSequenceNumber = initializeLSN();
    }

    private AtomicLong initializeLSN() throws IOException {
        // this operation is crucial so terminate in error case.
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

    private boolean persistToLog(Object... toPersist) {
        String logLine = buildLine(toPersist);
        try {
            Files.write(logFile, logLine.getBytes(), StandardOpenOption.APPEND);
            return true;
        } catch (IOException ex) {
            LOG.error("Error writing to log file", ex);
            return false;
        }

    }

    private String buildLine(Object... toCompose) {
        StringBuilder composedString = new StringBuilder();
        for(Object obj : toCompose) {
            composedString.append(obj.toString());
            composedString.append("\t");
        }
        return composedString + "\n"; // terminate line
    }

    private Long getLSN() {
        return logSequenceNumber.incrementAndGet();
    }
}
