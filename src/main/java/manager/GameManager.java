package manager;

public interface GameManager {
    boolean procesarPartida(String username, int puntos, int monedas);
    void actualizarObjetosJugador(String username, int magnet, int shield, int doubler);
    public boolean actualizarPuntuacion(String username, int puntosNuevos);
    public boolean actualizarMonedas(String username, int monedasTotales);
    }