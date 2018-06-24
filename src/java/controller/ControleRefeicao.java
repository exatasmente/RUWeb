package controller;

import dao.RefeicaoDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Refeicao;

import org.codehaus.jackson.map.ObjectMapper;

public class ControleRefeicao implements Controle<Refeicao> {

    public ControleRefeicao() {

    }

    public void remove(int id) throws ClassNotFoundException, SQLException {
        RefeicaoDAO dao = new RefeicaoDAO();
        dao.remove(id);

    }

    public void update(Refeicao refeicao) throws ClassNotFoundException, SQLException {
        RefeicaoDAO dao = new RefeicaoDAO();

        dao.update(refeicao);

    }

    @Override
    public void add(Refeicao refeicao) throws ClassNotFoundException, SQLException {
        RefeicaoDAO dao = new RefeicaoDAO();
        System.out.println(refeicao);
        dao.add(refeicao);

    }

    public String getByTipo(String tipo) throws ClassNotFoundException, SQLException {
        RefeicaoDAO dao = new RefeicaoDAO();
        ObjectMapper mapperObj = new ObjectMapper();
        ArrayList<Refeicao> lista = new ArrayList<>();
        lista = dao.getByTipo(tipo);
        try {
            return mapperObj.writeValueAsString(lista);
        } catch (IOException e) {
            return "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";

        }

    }

    @Override
    public String lista() throws ClassNotFoundException, SQLException {
        RefeicaoDAO dao = new RefeicaoDAO();
        ObjectMapper mapperObj = new ObjectMapper();
        ArrayList<Refeicao> lista = new ArrayList<>();
        lista = dao.lista();
        try {
            return mapperObj.writeValueAsString(lista);
        } catch (IOException e) {
            return "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";

        }

    }

    @Override
    public String get(int id) throws ClassNotFoundException, SQLException {
        RefeicaoDAO dao = new RefeicaoDAO();
        Refeicao a = dao.get(id);
        String jsonStr = "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";
        ObjectMapper mapperObj = new ObjectMapper();
        try {
            if (a != null) {
                jsonStr = mapperObj.writeValueAsString(a);
            } else {
                jsonStr = "{\"message\" : \"Refeicao Não Encontrada \", \"Status\" : \"404\"}";
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            return jsonStr;
        }
    }

}
