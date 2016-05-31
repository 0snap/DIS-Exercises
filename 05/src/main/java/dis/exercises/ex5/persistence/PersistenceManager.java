package dis.exercises.ex5.persistence;



import java.util.UUID;

public class PersistenceManager {
    private final String logFileName;
    private final String dataFileName;

    public PersistenceManager(String logFileName, String dataFileName) {
        this.logFileName = logFileName;
        this.dataFileName = dataFileName;
    }

    public String beginTransaction() {
        return UUID.randomUUID().toString();
    }

    public boolean commit(String transactionId) {
        return false;
    }

    public boolean write(String transactionId, int pageId, String data) {
        return false;
    }
}
