/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Usuario;
import model.Cartao;

/**
 *
 * @author root
 */
public class UsuarioDAO implements Dao<Usuario> {

    @Override
    public void remove(int id) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);
        PreparedStatement ps = con.prepareStatement("delete from USUARIO where id = ?");
        ps.setInt(1,id);
        ps.execute();
    }

    
   

    @Override
    public void update(Usuario usuario) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);
        PreparedStatement ps = con.prepareStatement("update  "
                + "USUARIO set nome =?, "
                + "sobrenome=?, "
                + "email=?, "
                + "login=?,"
                + "senha=?,"
                + "tipo=?,"
                + "ativo=?\n"
                + "where id = ?");
        ps.setString(1, usuario.getNome());
        ps.setString(2, usuario.getSobrenome());
        ps.setString(3, usuario.getEmail());
        ps.setString(4, usuario.getLogin());
        ps.setString(5, usuario.getSenha());
        ps.setInt(6, usuario.getTipo());
        ps.setInt(7, (usuario.isAtivo() ? 1 : 0));
        ps.setInt(8,usuario.getId());
        ps.execute();
       
    }

    @Override
    public void add(Usuario usuario) throws ClassNotFoundException, SQLException {

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);
        PreparedStatement ps = con.prepareStatement("insert into USUARIO (nome,sobrenome,email,login,senha,tipo,ativo)\n"
                + "values  (?,?,?,?,?,?,?) returning id ");
        ps.setString(1, usuario.getNome());
        ps.setString(2, usuario.getSobrenome());
        ps.setString(3, usuario.getEmail());
        ps.setString(4, usuario.getLogin());
        ps.setString(5, usuario.getSenha());
        ps.setInt(6, usuario.getTipo());
        ps.setInt(7, 1);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println(rs.getInt("id"));
            ps = con.prepareStatement("INSERT INTO CARTAO (id_usuario,saldo) VALUES (?,0)");
            ps.setInt(1, rs.getInt("id"));
            ps.execute();
        }

    }

    @Override
    public ArrayList<Usuario> lista() throws ClassNotFoundException, SQLException {
        ArrayList<Usuario> lista = new ArrayList<>();

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);

        PreparedStatement ps = con.prepareStatement("select * from ALUNO");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Usuario usuario = new Usuario(rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("sobrenome"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("senha"),
                    rs.getInt("tipo"),
                    rs.getBoolean("ativo"),
                    new Cartao()
            );
            ps = con.prepareStatement("select * from CARTAO where id_usuario = ?");
            ps.setInt(1, usuario.getId());
            ResultSet rs2 = ps.executeQuery();
            if (rs2.next()) {
                usuario.getCartao().setNumero(rs2.getInt("id"));
                usuario.getCartao().setSaldo(rs2.getFloat("saldo"));

            }
            lista.add(usuario);

        }

        return lista;

    }

    @Override
    public Usuario get(int id) throws ClassNotFoundException, SQLException {
        Usuario usuario = null;

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);
        PreparedStatement ps = con.prepareStatement("select * from usuario where id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            usuario = new Usuario(rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("sobrenome"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("senha"),
                    rs.getInt("tipo"),
                    rs.getBoolean("ativo"),
                    new Cartao()
            );
            ps = con.prepareStatement("select * from cartao where id_usuario = ?");
            ps.setInt(1, usuario.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                usuario.getCartao().setNumero(rs.getInt("id"));
                usuario.getCartao().setSaldo(rs.getFloat("saldo"));
                System.out.println(usuario.getCartao().getSaldo());
            }
        }

        return usuario;

    }

    public Usuario getByLogin(String login) throws ClassNotFoundException, SQLException {
        Usuario usuario = null;

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);
        PreparedStatement ps = con.prepareStatement("select * from usuario where login = ?");
        ps.setString(1, login);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            usuario = new Usuario(rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("sobrenome"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("senha"),
                    rs.getInt("tipo"),
                    rs.getBoolean("ativo"),
                    new Cartao()
            );
            ps = con.prepareStatement("select * from cartao where id = ?");
            ps.setInt(1, rs.getInt("id_cartao"));
            rs = ps.executeQuery();
            if (rs.next()) {
                usuario.getCartao().setNumero(rs.getInt("id"));
                usuario.getCartao().setSaldo(rs.getFloat("saldo"));
                System.out.println(usuario.getCartao().getSaldo());
            }
        }

        return usuario;

    }
}
