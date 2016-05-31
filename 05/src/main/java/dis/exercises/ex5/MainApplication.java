package dis.exercises.ex5;

import dis.exercises.ex5.persistence.PersistenceManager;
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
        return "Dis-05-Bullshit";
    }

    @Override
    public void initialize(Bootstrap<MainConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
	public void run(MainConfiguration config, Environment env) throws Exception {
		LOG.info("Starting server...");
        PersistenceManager manager = new PersistenceManager(config.getLogFileName(), config.getDataFileName());
		PersistenceResource persistenceResource =new PersistenceResource(manager);
		env.jersey().register(persistenceResource);
		LOG.info("Server startup finished.");
	}
}
