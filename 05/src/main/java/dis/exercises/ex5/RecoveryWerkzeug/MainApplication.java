package dis.exercises.ex5;

import dis.exercises.ex5.persistence.PersistentDataManager;
import dis.exercises.ex5.persistence.PersistentLogManager;
import dis.exercises.ex5.resources.PersistenceResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import RecoveryWerkzeug.ARIES;
import RecoveryWerkzeug.DirtyPageTable;
import RecoveryWerkzeug.DiskImage;
import RecoveryWerkzeug.PostponedChanges;
import RecoveryWerkzeug.TransactionTable;


public class MainApplication extends Application<MainConfiguration>
{
	
	private static final Logger LOG = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) throws Exception {
        new MainApplication().run(args);
    }

    @Override
    public String getName() {
        return "Dis-05-Persistence";
    }

    @Override
    public void initialize(Bootstrap<MainConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
	public void run(MainConfiguration config, Environment env) throws Exception {
		LOG.info("Starting server...");
        PersistentDataManager dataManager = new PersistentDataManager(config.getDataFileName());
        PersistentLogManager logManager = new PersistentLogManager(config.getLogFileName());
        PersistenceResource persistenceResource =new PersistenceResource(dataManager, logManager);
		env.jersey().register(persistenceResource);
	}
    
    LogFileParser fileParser = new LogFileParser();

    Log log = fileParser.parseLogFileAndReturnLogObject(filePath);

	PostponedChanges algorithm = new PostponedChanges();
	
	algorithm.analyze(log);
	
	System.out.println(ARIES.dirtyPageTable);
	System.out.println(ARIES.transactionTable);
	
	List<LogRecord> recordList = algorithm.buildRedoRecordList(log);
	
	for (LogRecord logRecord : recordList) {
		System.out.println(logRecord);
	}
	
	DiskImage finalDiskImage = algorithm.redo(recordList);

	System.out.println(finalDiskImage);

	}
}
