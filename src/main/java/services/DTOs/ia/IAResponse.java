package services.DTOs.ia;

public class IAResponse {
    private String response;

    public IAResponse(String response) {
        this.response = response;
    }

    public IAResponse() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
