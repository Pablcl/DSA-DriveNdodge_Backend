package db.orm.dao;



import db.orm.FactorySession;
import db.orm.Session;
import db.orm.model.Usuario;

import java.util.HashMap;
import java.util.List;

public class UsuarioDAOImpl implements IUsuarioDAO {

    private static UsuarioDAOImpl instance;

    private UsuarioDAOImpl() {
        // Constructor privat perquè ningú faci "new UsuarioDAOImpl()"
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
            // 1. Preparem el filtre (WHERE gmail = ...)
            HashMap<String, Object> params = new HashMap<>();
            params.put("gmail", email);

            // 2. Cridem a la base de dades
            List<Object> result = session.findAll(Usuario.class, params);

            // 3. Retornem el primer resultat o null si està buida
            // (Operador ternari: Si està buida ? torna null : torna el primer element)
            return result.isEmpty() ? null : (Usuario) result.get(0);

        } catch (Exception e) {
            e.printStackTrace(); //per veure errors per consola
            return null;
        } finally {
            session.close(); // Sempre tanquem la porta en sortir
        }
    }
    @Override
    public Usuario getUsuarioByUsername(String username) {
        Session session = FactorySession.openSession();
        try {
            // 1. Creem el filtre: WHERE username = ?
            java.util.HashMap<String, Object> params = new java.util.HashMap<>();
            params.put("username", username); // La clau ha de ser igual al nom de la variable a Usuario.java

            // 2. Cridem al findAll que acabes de programar
            List<Object> result = session.findAll(Usuario.class, params);

            // 3. Retornem el resultat
            return result.isEmpty() ? null : (Usuario) result.get(0);

        } catch (Exception e) {
            return null;
        } finally {
            session.close();
        }
    }

    public void updateUsuario(Usuario usuario) {
        /*Employee employee  = this.getEmployee(employeeID);
        employee.setName(name);
        employee.setSurname(surname);
        employee.setSalary(salary);

        Session session = null;
        try {
            session = FactorySession.openSession();
            session.update(employee);
        }
        catch (Exception e) {
            // LOG
        }
        finally {
            session.close();
        }*/
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
