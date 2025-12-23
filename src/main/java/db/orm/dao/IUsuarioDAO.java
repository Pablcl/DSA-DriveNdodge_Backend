package db.orm.dao;


import db.orm.model.Item;
import db.orm.model.Usuario;

import java.util.List;


public interface IUsuarioDAO {

    public int addUsuario(Usuario usuario);
    public Usuario getUsuario(int ID);
    public Usuario getUsuarioByEmail(String gmail);
    public void updateUsuario(Usuario usuario);
    public void deleteUsuario(int ID);
    public List<Usuario> getUsuarios();
    public List <Usuario> getUsuarioByDept(int ID);
    public Usuario getUsuarioByUsername(String username);
    List<Usuario> getUsuariosRanking();
    public Usuario getUsuarioByCred(String username, String password);
}
