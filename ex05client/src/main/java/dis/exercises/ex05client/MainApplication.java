package dis.exercises.ex05client;

/**
 * Created by tilly on 06.06.16.
 */

import dis.exercises.ex05client.MainConfiguration;
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
        return "Dis-05-Client";
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
