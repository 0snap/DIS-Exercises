package dis.exercises.ex5.resources;


import dis.exercises.ex5.persistence.PersistenceManager;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/persistence")
public class PersistenceResource {


    private PersistenceManager persistenceManager;

    public PersistenceResource(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    @Path("/ping")
    @GET
    public Response ping() {
        return Response.ok("pong").build();
    }

    @Path("/beginTransaction")
    @POST
    public Response beginTransaction() {
        return Response.ok(persistenceManager.beginTransaction()).build();
    }

    @Path("/commit/{transactionId}")
    @POST
    public Response commit(@PathParam("transactionId") String transactionId) {
        boolean success = persistenceManager.commit(transactionId);
        if(success) {
            Response.ok().build();
        }
        return Response.serverError().build();
    }

    @Path("/write")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response write(@NotNull WriteParameter writeParameter) {
        boolean success = persistenceManager.write(
                writeParameter.getTransactionId(),
                writeParameter.getPageId(),
                writeParameter.getData());
        if(success) {
            Response.ok().build();
        }
        return Response.serverError().build();
    }

}
