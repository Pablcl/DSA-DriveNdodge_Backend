package manager;

public interface GameManager {
    boolean procesarPartida(String username, int puntos, int monedas);
    void actualizarObjetosJugador(String username, int magnet, int shield, int doubler);
}