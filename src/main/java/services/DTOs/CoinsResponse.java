package services.DTOs;

public class CoinsResponse {
        public int coins; // Public perquè MOXy ho llegeixi ràpid

        public CoinsResponse() {} // Constructor buit obligatori
        public CoinsResponse(int coins) {
            this.coins = coins;
        }
        public int getCoins() { return coins; }
        public void setCoins(int coins) { this.coins = coins; }
}
