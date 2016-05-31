package dis.exercises.ex5.resources;


import dis.exercises.ex5.persistence.PersistentDataManager;
import dis.exercises.ex5.persistence.PersistentLogManager;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/persistence")
public class PersistenceResource {


    private PersistentDataManager dataManager;
    private PersistentLogManager logManager;

    public PersistenceResource(PersistentDataManager dataManager, PersistentLogManager logManager) {
        this.dataManager = dataManager;
        this.logManager = logManager;
    }

    @Path("/ping")
    @GET
    public Response ping() {
        return Response.ok("pong").build();
    }

    @Path("/beginTransaction")
    @POST
    public Response beginTransaction() {
        String transactionId = dataManager.beginTransaction();
        logManager.beginTransaction(transactionId);
        return Response.ok(transactionId).build();
    }

    @Path("/commit/{transactionId}")
    @POST
    public Response commit(@PathParam("transactionId") String transactionId) {
        Long lsn = logManager.commit(transactionId);
        dataManager.commit(transactionId, lsn);
        return Response.ok().build();
    }

    @Path("/write")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response write(@NotNull WriteParameter writeParameter) {
        Long lsn = logManager.write(writeParameter.getTransactionId(),
                writeParameter.getPageId(),
                writeParameter.getData());

        dataManager.write(
                writeParameter.getTransactionId(),
                writeParameter.getPageId(),
                lsn,
                writeParameter.getData());
        return Response.ok().build();
    }

}
