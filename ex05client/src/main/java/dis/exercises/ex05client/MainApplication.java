package dis.exercises.ex05client;

/**
 * Created by tilly on 06.06.16.
 */

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class MainApplication{
    public static void main(final String[] args) {
        int numberOfSimultaneousExecutions = 6;
        int numberOfRuns = 1;
        for (int j = 0; j < numberOfRuns; j++){
            java.util.concurrent.Executor executor = java.util.concurrent.Executors.newFixedThreadPool(numberOfSimultaneousExecutions);
            for (int i = 0; i < numberOfSimultaneousExecutions; i++) {
                final int thread_id = i;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Run.run(thread_id);
                    }
                });
            }
        }

    }
}



class Run{
    private static final Logger LOG = LoggerFactory.getLogger(MainApplication.class);

    public static void run(int threadId) {
        String url = "http://localhost:8080/persistence";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = com.sun.jersey.api.client.Client.create(clientConfig);

        String[] data = {"Corny mag huebsche Frauen", "das ist eine Datensatz", "pingpong"};

        String transactionId = beginTransaction(client, url);
        writePost(client, url, transactionId,threadId , data);
        commit(client, url, transactionId);
    }

    private static String beginTransaction(Client client, String url){
        String apiUrl = url + "/beginTransaction";
        try {
            WebResource webResource = client.resource(apiUrl);
            ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, "penis");
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed on begin: HTTP error code : " + response.getStatus());
            }
            String output = response.getEntity(String.class);
            System.out.println("Server response ....");
            System.out.println(output);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "shit fucked up";
    }

    private static void writePost(Client client, String url, String transId,int clientId, String[] dataArray){
        String apiUrl = url + "/write";
        Random rand = new Random();
        int pageId = rand.nextInt(10) +1 +  10*clientId;
        int random = rand.nextInt(15) +1;
        String data = dataArray[(pageId+random)%dataArray.length];

        try {
            WebResource webResource = client.resource(apiUrl);
            ClientResponse response = webResource.accept("application/json").type("application/json")
                    .post(ClientResponse.class, "{\"transactionId\": \"" + transId +  "\", \"pageId\":"+ pageId +", \"data\": \"" + data + "\"}");
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed on write: HTTP error code : "
                        + response.getStatus()+ " transID: " + transId
                        + ", pageId: " + Integer.toString(pageId)
                        + ", clientId: " + Integer.toString(clientId)
                        );
            }
            System.out.println("Write done ; transId: " + transId
                    + " pageId: " + Integer.toString(pageId)
                    + " clientId:" + Integer.toString(clientId));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void commit(Client client, String url, String transId){
        String apiUrl = url + "/commit/" + transId;
        try {
            WebResource webResource = client.resource(apiUrl);
            ClientResponse response = webResource.accept("application/json").type("application/json")
                    .post(ClientResponse.class, "penis");
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed on commit: HTTP error code : " + response.getStatus()
                + " transId: " + transId);
            }
            String output = response.getEntity(String.class);
            System.out.println("Commit done; transId: " + transId);
            System.out.println(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
