package controller;

import dao.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Usuario;

import org.codehaus.jackson.map.ObjectMapper;

public class ControleUsuario implements Controle<Usuario> {

    public ControleUsuario() {

    }

    public void remove(int id) throws ClassNotFoundException, SQLException {
        UsuarioDAO dao = new UsuarioDAO();
        dao.remove(id);

    }

    public void update(Usuario usuario) throws ClassNotFoundException, SQLException {
        UsuarioDAO dao = new UsuarioDAO();

        dao.update(usuario);

    }

    @Override
    public void add(Usuario usuario) throws ClassNotFoundException, SQLException {
        UsuarioDAO dao = new UsuarioDAO();
        dao.add(usuario);


    }

    @Override
    public String lista() throws ClassNotFoundException, SQLException {
        UsuarioDAO dao = new UsuarioDAO();
        ObjectMapper mapperObj = new ObjectMapper();
        ArrayList<Usuario> lista = new ArrayList<>();
        lista = dao.lista();
        try {
            return mapperObj.writeValueAsString(lista);
        } catch (IOException e) {
            return "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";

        }

    }

    @Override
    public String get(int id) throws ClassNotFoundException, SQLException {
        UsuarioDAO dao = new UsuarioDAO();
        Usuario a = dao.get(id);
        String jsonStr = "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";
        ObjectMapper mapperObj = new ObjectMapper();
        try {
            if (a != null) {
                jsonStr = mapperObj.writeValueAsString(a);
            } else {
                jsonStr = "{\"message\" : \"Aluno Não Encontrado \", \"Status\" : \"404\"}";
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            return jsonStr;
        }
    }

}
