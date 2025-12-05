package db.orm.dao;



import db.orm.FactorySession;
import db.orm.Session;
import db.orm.model.Item;
import db.orm.model.Usuario;
import db.orm.util.QueryHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UsuarioDAOImpl implements IUsuarioDAO {

    private static UsuarioDAOImpl instance;
    private int mejorPuntuacion;

    private UsuarioDAOImpl() {
    }
    public static UsuarioDAOImpl getInstance() {
        if (instance == null) {
            instance = new UsuarioDAOImpl();
        }
        return instance;
    }
    public int addUsuario(Usuario usuario) {
        Session session = null;
        int ID = 0;
        try {
            session = FactorySession.openSession();
            session.save(usuario);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (session != null) {
                session.close();
            }
        }

        return ID;
    }

    public Usuario getUsuario(int ID) {
       Session session = null;
        Usuario usuario = null;
        try {
            session = FactorySession.openSession();
            usuario = (Usuario) session.get(Usuario.class, ID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return usuario;
    }


    @Override
    public Usuario getUsuarioByEmail(String email) {
        Session session = FactorySession.openSession();
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("gmail", email);
            List<Object> result = session.findAll(Usuario.class, params);
            return result.isEmpty() ? null : (Usuario) result.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    @Override
    public Usuario getUsuarioByUsername(String username) {
        Session session = FactorySession.openSession();
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("username", username);
            List<Object> result = session.findAll(Usuario.class, params);
            return result.isEmpty() ? null : (Usuario) result.get(0);

        } catch (Exception e) {
            return null;
        } finally {
            session.close();
        }
    }
    public void updateUsuario(Usuario usuario) {
        Session session = FactorySession.openSession();
        try {
            session = FactorySession.openSession();
            session.update(usuario);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (session != null) {
                session.close();
            }

        }
    }
    @Override
    public List<Usuario> getUsuariosRanking() {
        Usuario usuario = null;
        Session session = FactorySession.openSession();
        List<Usuario> usersRanking = null;
        try {
            String sql = QueryHelper.ordenateQuery(Usuario.class, "mejorPuntuacion");
            usersRanking = session.query(Usuario.class, sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(session != null){
                session.close();
            }
        }return usersRanking;
    }

    public void deleteUsuario(int ID) {
        /*Employee employee = this.getEmployee(employeeID);
        Session session = null;
        try {
            session = FactorySession.openSession();
            session.delete(employee);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }*/

    }


    public List<Usuario> getUsuarios() {
        /*Session session = null;
        List<Usuario> usuarioList=null;
        try {
            session = FactorySession.openSession();
            employeeList = session.findAll(Employee.class);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }*/
        return null;
    }


    public List<Usuario> getUsuarioByDept(int ID) {
/*
        // SELECT e.name, d.name FROM Employees e, DEpt d WHERE e.deptId = d.ID AND e.edat>87 AND ........

//        Connection c =

        Session session = null;
        List<Usuario> usuarioList=null;
        try {
            session = FactorySession.openSession();


            HashMap<String, Integer> params = new HashMap<String, Integer>();
            params.put("deptID", deptID);

            employeeList = session.findAll(Employee.class, params);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }*/
        return null;
    }

    /*

    public void customQuery(xxxx) {
        Session session = null;
        List<Employee> employeeList=null;
        try {
            session = FactorySession.openSession();
            Connection c = session.getConnection();
            c.createStatement("SELECT * ")

        }
*/

}
