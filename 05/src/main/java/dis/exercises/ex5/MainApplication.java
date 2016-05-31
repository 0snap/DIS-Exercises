package dis.exercises.ex5;

import dis.exercises.ex5.persistence.PersistentDataManager;
import dis.exercises.ex5.persistence.PersistentLogManager;
import dis.exercises.ex5.resources.PersistenceResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApplication extends Application<MainConfiguration> {
	
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
}
