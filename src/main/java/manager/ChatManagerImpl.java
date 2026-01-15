package manager;

import services.DTOs.ia.IARequest;
import services.DTOs.ia.IAResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

public class ChatManagerImpl implements ChatManager {

    private static ChatManagerImpl instance;
    private Client client;

    private static final String IA_URL = "http://vps:11434/api/generate";
    private static final String IA_MODEL = "llama3.2:1b";

    private ChatManagerImpl() {
        this.client = ClientBuilder.newClient();
    }

    public static synchronized ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManagerImpl();
        }
        return instance;
    }

    @Override
    public String askIA(String userMessage) {
        try {
            IARequest requestBody = new IARequest(IA_MODEL, userMessage, false);
            IAResponse responseObj = client.target(IA_URL).request(MediaType.APPLICATION_JSON).post(Entity.entity(requestBody, MediaType.APPLICATION_JSON), IAResponse.class);

            return responseObj.getResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Sistema no responde.";
        }
    }
}