package db.orm.dao;

import db.orm.FactorySession;
import db.orm.Session;
import db.orm.model.Evento;
import db.orm.model.Usuario;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class EventoDAOImpl implements EventoDAO {

    private static EventoDAOImpl instance;

    private EventoDAOImpl() {
    }

    public static EventoDAOImpl getInstance() {
        if (instance == null) {
            instance = new EventoDAOImpl();
        }
        return instance;
    }

    @Override
    public List<Evento> getEventos() {
        Session session = null;
        List<Evento> eventos = new ArrayList<>();
        try {
            session = FactorySession.openSession();
            eventos = (List<Evento>)(List<?>) session.findAll(Evento.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return eventos;
    }

    @Override
    public Evento getEventoById(int id) {
        Session session = null;
        Evento evento = null;
        try {
            session = FactorySession.openSession();
            evento = (Evento) session.get(Evento.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return evento;
    }

    @Override
    public void inscribirUsuario(int usuarioId, int eventoId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = FactorySession.getConnection();
            String sql = "INSERT INTO InscripcionEvento (usuarioId, eventoId) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, eventoId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    @Override
    public List<Usuario> getUsuariosInscritos(int eventoId) {
        Session session = null;
        List<Usuario> usuarios = null;
        try {
            session = FactorySession.openSession();
            String sqlDirecta = "SELECT u.* FROM Usuario u JOIN InscripcionEvento i ON u.ID = i.usuarioId WHERE i.eventoId = " + eventoId;
            usuarios = (List<Usuario>)(List<?>) session.query(Usuario.class, sqlDirecta, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return usuarios;
    }
}