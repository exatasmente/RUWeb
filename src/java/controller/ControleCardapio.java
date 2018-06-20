package controller;

import dao.CardapioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Cardapio;

import org.codehaus.jackson.map.ObjectMapper;

public class ControleCardapio implements Controle<Cardapio> {

    public ControleCardapio() {

    }

    public void remove(int id) throws ClassNotFoundException, SQLException {
        CardapioDAO dao = new CardapioDAO();
        dao.remove(id);

    }

    public void update(Cardapio cardapio) throws ClassNotFoundException, SQLException {
        CardapioDAO dao = new CardapioDAO();

        dao.update(cardapio);

    }

    @Override
    public void add(Cardapio cardapio) throws ClassNotFoundException, SQLException {
        CardapioDAO dao = new CardapioDAO();
        dao.add(cardapio);


    }

    @Override
    public String lista() throws ClassNotFoundException, SQLException {
        CardapioDAO dao = new CardapioDAO();
        ObjectMapper mapperObj = new ObjectMapper();
        ArrayList<Cardapio> lista = new ArrayList<>();
        lista = dao.lista();
        try {
            return mapperObj.writeValueAsString(lista);
        } catch (IOException e) {
            return "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";

        }

    }

    @Override
    public String get(int id) throws ClassNotFoundException, SQLException {
        CardapioDAO dao = new CardapioDAO();
        Cardapio a = dao.get(id);
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
