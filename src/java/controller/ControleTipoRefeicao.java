package controller;

import dao.TipoRefeicaoDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import model.TipoRefeicao;

import org.codehaus.jackson.map.ObjectMapper;

public class ControleTipoRefeicao implements Controle<TipoRefeicao> {

    public ControleTipoRefeicao() {

    }

    public void remove(int id) throws ClassNotFoundException, SQLException {
        TipoRefeicaoDAO dao = new TipoRefeicaoDAO();
        dao.remove(id);

    }

    public void update(TipoRefeicao refeicao) throws ClassNotFoundException, SQLException {
        TipoRefeicaoDAO dao = new TipoRefeicaoDAO();

        dao.update(refeicao);

    }

    @Override
    public void add(TipoRefeicao refeicao) throws ClassNotFoundException, SQLException {
        TipoRefeicaoDAO dao = new TipoRefeicaoDAO();
        System.out.println(refeicao);
        dao.add(refeicao);

    }

    public String get(String nome) throws ClassNotFoundException, SQLException {
        TipoRefeicaoDAO dao = new TipoRefeicaoDAO();
        ObjectMapper mapperObj = new ObjectMapper();
        ArrayList<TipoRefeicao> lista = new ArrayList<>();
        lista = dao.get(nome);
        try {
            return mapperObj.writeValueAsString(lista);
        } catch (IOException e) {
            return "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";

        }

    }

    @Override
    public String lista() throws ClassNotFoundException, SQLException {
        TipoRefeicaoDAO dao = new TipoRefeicaoDAO();
        ObjectMapper mapperObj = new ObjectMapper();
        ArrayList<TipoRefeicao> lista = new ArrayList<>();
        lista = dao.lista();
        try {
            return mapperObj.writeValueAsString(lista);
        } catch (IOException e) {
            return "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";

        }

    }

    @Override
    public String get(int id) throws ClassNotFoundException, SQLException {
        TipoRefeicaoDAO dao = new TipoRefeicaoDAO();
        TipoRefeicao a = dao.get(id);
        String jsonStr = "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";
        ObjectMapper mapperObj = new ObjectMapper();
        try {
            if (a != null) {
                jsonStr = mapperObj.writeValueAsString(a);
            } else {
                jsonStr = "{\"message\" : \"TipoRefeicao Não Encontrada \", \"Status\" : \"404\"}";
            }
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            return jsonStr;
        }
    }

}
