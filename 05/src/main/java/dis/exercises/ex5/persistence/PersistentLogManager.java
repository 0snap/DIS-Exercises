package dis.exercises.ex5.persistence;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class PersistentLogManager extends PersistentFileAccessor {

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

    private void persistToLog(Object... toLog) {
        appendToFile(logFile, toLog);
    }


    private Long getLSN() {
        return logSequenceNumber.incrementAndGet();
    }

    public Map<String, List<String>> getLogentriesGroupedByTransactionID() {
        Map<String, List<String>> result = new HashMap<>();
        try {
            List<String> logLines = Files.readAllLines(logFile);
            for (String logEntry : logLines) {
                String loggedTID = logEntry.split("\t")[1];
                List<String> actionsInTA = result.getOrDefault(loggedTID, new ArrayList<>());
                actionsInTA.add(logEntry);
                result.put(loggedTID, actionsInTA);
            }
        }
        catch (IOException ex) {
            LOG.error("Unable to read log", ex);
        }
        return result;
    }

    public Map<String, List<String>> getLogentriesOfUncommittedTAs() {
        Map<String, List<String>> allTransactions = getLogentriesGroupedByTransactionID();
        Map<String, List<String>> result = new HashMap<>();
        for(String tId : allTransactions.keySet()) {
            boolean committed = allTransactions.get(tId).stream().anyMatch(entry -> entry.contains(TransactionState.COMMITTED.toString()));
            if( ! committed) {
                result.put(tId, allTransactions.get(tId));
            }
        }
        return result;
    }

}
