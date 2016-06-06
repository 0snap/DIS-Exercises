package dis.exercises.ex5.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class RecoveryManager {

    private static final Logger LOG = LoggerFactory.getLogger(RecoveryManager.class);

    PersistentDataManager dataManager;
    PersistentLogManager logManager;
    public RecoveryManager(PersistentLogManager logManager, PersistentDataManager dataManager) {
        this.dataManager = dataManager;
        this.logManager = logManager;
    }
    public void recoverBufferAfterCrash() {
        Map<String, List<String>> uncommittedTransactions = logManager.getLogentriesOfUncommittedTAs();
        for (String uncommittedTransactionId :  uncommittedTransactions.keySet()) {
            for (String logEntry : uncommittedTransactions.get(uncommittedTransactionId)) {
                // struct is lsn, transactionId, pageId, data
                long loggedLsn = Long.parseLong(logEntry.split("\t")[0]);
                int loggedPage = Integer.parseInt(logEntry.split("\t")[2]);
                // struct is pageId, lsn, data
                String pageEntry = dataManager.getPageEntry(loggedPage);
                if(pageEntry == null || Long.parseLong(pageEntry.split("\t")[1]) < loggedLsn) {
                    String loggedData = logEntry.split("\t")[3];
                    LOG.info("Recovering uncommitted TID " + uncommittedTransactionId + " and data '" + loggedData + "' to buffer.");
                    dataManager.write(uncommittedTransactionId, loggedPage, loggedLsn, loggedData);
                }
            }
        }
    }
}
