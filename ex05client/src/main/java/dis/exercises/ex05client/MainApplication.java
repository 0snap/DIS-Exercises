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
        int numberOfSimultaneousExecutions = 2;
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



class Run{
    private static final Logger LOG = LoggerFactory.getLogger(MainApplication.class);

    public static void run(int threadId) {
        String url = "http://localhost:8080/persistence";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = com.sun.jersey.api.client.Client.create(clientConfig);

        String data = "hier kommt der Data shit hin";

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
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            String output = response.getEntity(String.class);
            System.out.println("Server response .... \n");
            System.out.println(output);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "shit fucked up";
    }

    private static void writePost(Client client, String url, String transId,int userId, String data){
        String apiUrl = url + "/write";
        Random rand = new Random();
        int pageId = rand.nextInt(10) + 10*userId;


        try {
            WebResource webResource = client.resource(apiUrl);
            ClientResponse response = webResource.accept("application/json").type("application/json")
                    .post(ClientResponse.class, "{\"transactionId\": \"" + transId +  "\", \"pageId\":"+ pageId +", \"data\": \"" + data + "\"}");
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            System.out.println("Write done \n");

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
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            String output = response.getEntity(String.class);
            System.out.println("Commit done \n");
            System.out.println(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
