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

import model.TipoRefeicao;

/**
 *
 * @author root
 */
public class TipoRefeicaoDAO implements Dao<TipoRefeicao> {

    @Override
    public ArrayList lista() throws ClassNotFoundException, SQLException {
        ArrayList<TipoRefeicao> lista = new ArrayList<>();

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);

        PreparedStatement ps = con.prepareStatement("select * from TIPO_REFEICAO");
        ResultSet rs = ps.executeQuery();
        boolean desjejum = false, almoco = false, janta = false;
        while (rs.next()) {
            TipoRefeicao r = new TipoRefeicao();
            r.setId(rs.getInt("id"));
            r.setValor(rs.getString("valor"));
            lista.add(r);
        }
        return lista;
    }

    @Override
    public TipoRefeicao get(int id) throws ClassNotFoundException, SQLException {
        TipoRefeicao r = null;

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);

        PreparedStatement ps = con.prepareStatement("select * from TIPO_REFEICAO WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        boolean desjejum = false, almoco = false, janta = false;
        if (rs.next()) {
            r = new TipoRefeicao();
            r.setId(rs.getInt("id"));
            r.setValor(rs.getString("valor"));

        }
        return r;
    }

    public ArrayList get(String nome) throws ClassNotFoundException, SQLException {
        ArrayList<TipoRefeicao> lista = new ArrayList<>();

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);

        PreparedStatement ps = con.prepareStatement("select * from TIPO_REFEICAO where valor = ?");
        ps.setString(1, nome);
        ResultSet rs = ps.executeQuery();
        boolean desjejum = false, almoco = false, janta = false;
        while (rs.next()) {
            TipoRefeicao r = new TipoRefeicao();
            r.setId(rs.getInt("id"));
            r.setValor(rs.getString("valor"));

            lista.add(r);
        }
        return lista;
    }

    @Override
    public void remove(int id) throws ClassNotFoundException, SQLException {

    }

    @Override
    public void update(TipoRefeicao e) throws ClassNotFoundException, SQLException {

    }

    @Override
    public void add(TipoRefeicao e) throws ClassNotFoundException, SQLException {

    }

}
