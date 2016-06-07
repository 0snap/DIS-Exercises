package dis.exercises.ex5.persistence;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PersistentDataManager extends PersistentFileAccessor {

    private static final Logger LOG = LoggerFactory.getLogger(PersistentDataManager.class);

    private static final Integer THRESHOLD = 5;

    /** Relative Path to persistent data file */
    private final Path dataFile;

    /** Buffer in the format {"transactionId": {"pageId": "lsn, data"}, ... } */
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, Pair<Long, String>>> buffer;

    /** Buffer in the format {"transactionId": "lsn"}. if lsn is null, TA is not committed */
    private ConcurrentHashMap<String, Long> committedTransactions;


    public PersistentDataManager(String dataFileName) {
        dataFile = Paths.get("./", dataFileName);
        buffer = new ConcurrentHashMap<>();
        committedTransactions = new ConcurrentHashMap<>();
    }

    public String beginTransaction() {
        return UUID.randomUUID().toString();
    }

    public void write(String transactionId, int pageId, Long logSequenceNumber, String data) {
        storeToBuffer(transactionId, pageId, logSequenceNumber, data);
        flushOnThreshold();
    }

    public void commit(String transactionId, Long logSequenceNumber) {
        committedTransactions.put(transactionId, logSequenceNumber);
        flushOnThreshold();
    }

    public String getPageEntry(int pageId) {
        return readLineFromFile(dataFile, pageId);
    }

    private void storeToBuffer(String transactionId, int pageId, Long logSequenceNumber, String data) {
        ConcurrentHashMap<Integer, Pair<Long, String>> pagesInTransaction = buffer.get(transactionId);
        if(pagesInTransaction == null) {
            pagesInTransaction = new ConcurrentHashMap<>();
        }
        // hard overwrite (create new)
        Pair<Long, String> pageEntry = new Pair<>(logSequenceNumber, data);
        pagesInTransaction.put(pageId, pageEntry);
        buffer.put(transactionId, pagesInTransaction);
    }

    /** Flushes the buffer to db, if threshold is reached */
    private void flushOnThreshold() {
        if(countDirtyPagesInBuffer() > THRESHOLD) {
            for (String committedTransaction: committedTransactions.keySet()) {
                // LSN of the actual commit:
                Long highestLSN = committedTransactions.get(committedTransaction);
                Map<Integer, Pair<Long, String>> pagesInTransaction =
                        buffer.getOrDefault(committedTransaction, new ConcurrentHashMap<>());
                for (Integer pageId : pagesInTransaction.keySet()) {
                    Pair<Long, String> pageEntry = pagesInTransaction.get(pageId);
                    // use pageId as linenumber to override
                    overwriteLineInFile(dataFile, pageId, pageId, highestLSN, pageEntry.getValue());
                    buffer.remove(committedTransaction);
                }
            }
        }
    }

    private int countDirtyPagesInBuffer() {
        if(buffer.keySet().size() > 1) {
            return buffer.keySet().stream().map(key -> buffer.get(key).size()).reduce(0, (a, b) -> a + b);
        }
        return buffer.get(buffer.keys().nextElement()).size();
    }
}
