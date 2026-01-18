package services.DTOs;

public class InventarioRequest {
    private String username;
    private int magnet;
    private int shield;
    private int doubler;

    public InventarioRequest() {}

    public InventarioRequest(String username, int magnet, int shield, int doubler) {
        this.username = username;
        this.magnet = magnet;
        this.shield = shield;
        this.doubler = doubler;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getMagnet() { return magnet; }
    public void setMagnet(int magnet) { this.magnet = magnet; }
    public int getShield() { return shield; }
    public void setShield(int shield) { this.shield = shield; }
    public int getDoubler() { return doubler; }
    public void setDoubler(int doubler) { this.doubler = doubler; }
}