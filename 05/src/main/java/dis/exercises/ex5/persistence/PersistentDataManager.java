package dis.exercises.ex5.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class PersistentDataManager {

    private static final Logger LOG = LoggerFactory.getLogger(PersistentDataManager.class);
    private final String dataFileName;

    public PersistentDataManager(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public String beginTransaction() {
        return UUID.randomUUID().toString();
    }

    public boolean write(Long logSequenceNumber, int pageId, String data) {
        return false;
    }

    public boolean commit(String transactionId, Long logSequenceNumber) {
        return false;
    }
}
