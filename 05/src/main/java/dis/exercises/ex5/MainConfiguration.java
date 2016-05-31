package dis.exercises.ex5;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class MainConfiguration extends Configuration {

    private String logFileName;
    private String dataFileName;

    @JsonProperty
    public String getDataFileName() {
        return dataFileName;
    }

    @JsonProperty
    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    @JsonProperty
    public String getLogFileName() {
        return logFileName;
    }

    @JsonProperty
    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }
}
