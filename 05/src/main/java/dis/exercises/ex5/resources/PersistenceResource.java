package dis.exercises.ex5.resources;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/persistence")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersistenceResource {

    private final String logFileName;
    private final String dataFileName;

    public PersistenceResource(String logFileName, String dataFileName) {
        this.logFileName = logFileName;
        this.dataFileName = dataFileName;
    }

    @Path("/ping")
    @GET
    public Response ping() {
        return Response.ok("pong").build();
    }
}
