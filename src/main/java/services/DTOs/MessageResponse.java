package services.DTOs;

public class MessageResponse {
        public String message;

        public MessageResponse() {} // Constructor buit obligatori
        public MessageResponse(String message) { this.message = message; }

        public String getMessage() { return message; }

    public void setMessage(String message) {
        this.message = message;
    }
}
