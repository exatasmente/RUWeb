/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.Dao.DRIVER;
import static dao.Dao.SENHA;
import static dao.Dao.URL;
import static dao.Dao.USER;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Refeicao;
import model.TipoRefeicao;

/**
 *
 * @author root
 */
public class RefeicaoDAO implements Dao<Refeicao> {

    @Override
    public ArrayList lista() throws ClassNotFoundException, SQLException {
        ArrayList<Refeicao> lista = new ArrayList<>();

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);

        PreparedStatement ps = con.prepareStatement("select * from REFEICAO_API");
        ResultSet rs = ps.executeQuery();
        boolean desjejum = false, almoco = false, janta = false;
        while (rs.next()) {
            Refeicao r = new Refeicao();
            r.setId(rs.getInt("id"));
            r.setInfoNutricional(rs.getString("info_nutricional"));
            r.setNome(rs.getString("nome_refeicao"));
            r.setTipo(new TipoRefeicao(rs.getInt("id_tipo"),rs.getString("nome_tipo")));
            ps = con.prepareStatement("SELECT * FROM OBS_REFEICAO  WHERE id_refeicao = ?");
            ps.setInt(1, r.getId());
            ResultSet rs2 = ps.executeQuery();
            ArrayList<String> obs = new ArrayList<>();
            while (rs2.next()) {
                obs.add(rs2.getString("valor"));
            }
            r.setObservacoes(obs.toArray(new String[obs.size()]));
            lista.add(r);
        }
        return lista;
    }

    @Override
    public Refeicao get(int id) throws ClassNotFoundException, SQLException {
        Refeicao r = null;

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);

        PreparedStatement ps = con.prepareStatement("select * from REFEICAO_API");
        ResultSet rs = ps.executeQuery();
        boolean desjejum = false, almoco = false, janta = false;
        if (rs.next()) {
            r = new Refeicao();
            r.setId(rs.getInt("id"));
            r.setInfoNutricional(rs.getString("info_nutricional"));
            r.setNome(rs.getString("nome_refeicao"));
            r.setTipo(new TipoRefeicao(rs.getInt("id_tipo"), rs.getString("nome_tipo")));
            ps = con.prepareStatement("SELECT * FROM OBS_REFEICAO  WHERE id_refeicao = ?");
            ps.setInt(1, r.getId());
            ResultSet rs2 = ps.executeQuery();
            ArrayList<String> obs = new ArrayList<>();
            while (rs2.next()) {
                obs.add(rs2.getString("valor"));
            }
            r.setObservacoes(obs.toArray(new String[obs.size()]));

        }
        return r;
    }

    public ArrayList getByTipo(String tipo) throws ClassNotFoundException, SQLException {
        ArrayList<Refeicao> lista = new ArrayList<>();

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);

        PreparedStatement ps = con.prepareStatement("select * from REFEICAO_API where nome_tipo = ?");
        ps.setString(1, tipo);
        ResultSet rs = ps.executeQuery();
        boolean desjejum = false, almoco = false, janta = false;
        while (rs.next()) {
            Refeicao r = new Refeicao();
            r.setId(rs.getInt("id"));
            r.setInfoNutricional(rs.getString("info_nutricional"));
            r.setNome(rs.getString("nome_refeicao"));
            r.setTipo(new TipoRefeicao(rs.getInt("id_tipo"), rs.getString("nome_tipo")));
            ps = con.prepareStatement("SELECT * FROM OBS_REFEICAO  WHERE id_refeicao = ?");
            ps.setInt(1, r.getId());
            ResultSet rs2 = ps.executeQuery();
            ArrayList<String> obs = new ArrayList<>();
            while (rs2.next()) {
                obs.add(rs2.getString("valor"));
            }
            r.setObservacoes(obs.toArray(new String[obs.size()]));
            lista.add(r);
        }
        return lista;
    }

    @Override
    public void remove(int id) throws ClassNotFoundException, SQLException {

    }

    @Override
    public void update(Refeicao e) throws ClassNotFoundException, SQLException {

    }

    @Override
    public void add(Refeicao e) throws ClassNotFoundException, SQLException {

    }

}
