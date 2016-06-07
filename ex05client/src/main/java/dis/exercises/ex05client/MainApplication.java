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



public class MainApplication{
    private static final Logger LOG = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        String url = "http://localhost:8080/persistence";
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = com.sun.jersey.api.client.Client.create(clientConfig);
        String transactionId = beginTransaction(client, url);
        writePost(client, url, transactionId);
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

    private static void writePost(Client client, String url, String transId){
        String apiUrl = url + "/write";
        try {
            WebResource webResource = client.resource(apiUrl);
            ClientResponse response = webResource.accept("application/json").type("application/json")
                    .post(ClientResponse.class, "{\"transactionId\": \"" + transId +  "\", \"pageId\": 1, \"data\": \"Client 1 schreibt auf page 1\"}");
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
