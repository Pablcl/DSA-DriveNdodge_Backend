package services.DTOs.ia;

public class ChatIARequest {
    private String prompt;

    public ChatIARequest() {
    }

    public ChatIARequest(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
