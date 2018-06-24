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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Cardapio;
import model.Refeicao;
import model.TipoRefeicao;

/**
 *
 * @author root
 */
public class CardapioDAO implements Dao<Cardapio> {

    @Override
    public void remove(int id) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);
        PreparedStatement ps = con.prepareStatement("delete from USUARIO where id = ?");
        ps.setInt(1, id);
        ps.execute();
    }

    @Override
    public void update(Cardapio cardapio) throws ClassNotFoundException, SQLException {
        /*Class.forName(DRIVER);
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
        ps.setString(1, cardapio.getNome());
        ps.setString(2, cardapio.getSobrenome());
        ps.setString(3, cardapio.getEmail());
        ps.setString(4, cardapio.getLogin());
        ps.setString(5, cardapio.getSenha());
        ps.setInt(6, cardapio.getTipo());
        ps.setInt(7, (cardapio.isAtivo() ? 1 : 0));
        ps.setInt(8, cardapio.getId());
        ps.execute();
         */

    }

    @Override
    public void add(Cardapio cardapio) throws ClassNotFoundException, SQLException {

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);
        PreparedStatement ps = con.prepareStatement("insert into CARDAPIO (dia_semana,horario)\n"
                + "values  (?,?) returning id ");
        ps.setDate(1, Date.valueOf(cardapio.getData()));
        ps.setInt(2, 2);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            HashMap<Integer, HashMap<String, ArrayList<Refeicao>>> dados = cardapio.getDesjejum();
            for (Integer key : dados.keySet()) {
                for (String t : dados.get(key).keySet()) {
                    for (Refeicao r : dados.get(key).get(t)) {
                        ps = con.prepareStatement("INSERT INTO REFEICAO (id_cardapio,nome_refeicao,informacao_nutricional,tipo) "
                                + "VALUES (?,?,?,?) returning id");
                        ps.setInt(1, rs.getInt("id"));
                        ps.setString(2, r.getNome());
                        ps.setString(3, r.getInfoNutricional());
                        ps.setInt(4, key);
                        ResultSet rs2 = ps.executeQuery();
                        rs2.next();
                        if (r.getObservacoes().length > 0) {
                            for (int i = 0; i < r.getObservacoes().length; i++) {

                                ps = con.prepareStatement("INSERT INTO OBS_REFEICAO (id_refeicao,valor) "
                                        + "VALUES (?,?)");
                                ps.setInt(1, rs2.getInt("id"));
                                ps.setString(2, r.getObservacoes()[i]);
                                ps.execute();
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public ArrayList<Cardapio> lista() throws ClassNotFoundException, SQLException {
        ArrayList<Cardapio> lista = new ArrayList<>();

        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);

        PreparedStatement ps = con.prepareStatement("select * from CARDAPIO_API");
        ResultSet rs = ps.executeQuery();
        boolean desjejum = false, almoco = false, janta = false;
        while (rs.next()) {
            Cardapio cardapio = new Cardapio();
            cardapio.setData(rs.getDate("dia").toString());
            if (lista.contains(cardapio)) {
                cardapio = lista.get(lista.indexOf(cardapio));
                int horario = rs.getInt("horario");
                Refeicao r = new Refeicao();
                r.setId(rs.getInt("id_refeicao"));
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
                switch (horario) {
                    case 1:

                        if (cardapio.getDesjejum().get(horario) != null) {
                            if (cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()) != null) {
                                cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()).add(r);
                            } else {
                                cardapio.getDesjejum().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                                cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()).add(r);
                            }

                        } else {
                            cardapio.getDesjejum().put(horario, new HashMap<String, ArrayList<Refeicao>>());
                            cardapio.getDesjejum().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                            cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()).add(r);
                        }

                        break;
                    case 2:

                        if (cardapio.getAlmoco().get(horario) != null) {
                            if (cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()) != null) {
                                cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()).add(r);
                            } else {
                                cardapio.getAlmoco().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                                cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()).add(r);
                            }

                        } else {
                            cardapio.getAlmoco().put(horario, new HashMap<String, ArrayList<Refeicao>>());
                            cardapio.getAlmoco().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                            cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()).add(r);
                        }
                        break;
                    case 3:

                        if (cardapio.getJanta().get(horario) != null) {
                            if (cardapio.getJanta().get(horario).get(r.getTipo().getValor()) != null) {
                                cardapio.getJanta().get(horario).get(r.getTipo().getValor()).add(r);
                            } else {
                                cardapio.getJanta().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                                cardapio.getJanta().get(horario).get(r.getTipo().getValor()).add(r);
                            }

                        } else {
                            cardapio.getJanta().put(horario, new HashMap<String, ArrayList<Refeicao>>());
                            cardapio.getJanta().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                            cardapio.getJanta().get(horario).get(r.getTipo().getValor()).add(r);
                        }
                        break;
                }

            } else {
                int horario = rs.getInt("horario");
                Refeicao r = new Refeicao();
                r.setId(rs.getInt("id_refeicao"));
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
                switch (horario) {
                    case 1:

                        if (cardapio.getDesjejum().get(horario) != null) {
                            if (cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()) != null) {
                                cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()).add(r);
                            } else {
                                cardapio.getDesjejum().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                                cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()).add(r);
                            }

                        } else {
                            cardapio.getDesjejum().put(horario, new HashMap<String, ArrayList<Refeicao>>());
                            cardapio.getDesjejum().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                            cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()).add(r);
                        }

                        break;
                    case 2:

                        if (cardapio.getAlmoco().get(horario) != null) {
                            if (cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()) != null) {
                                cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()).add(r);
                            } else {
                                cardapio.getAlmoco().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                                cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()).add(r);
                            }

                        } else {
                            cardapio.getAlmoco().put(horario, new HashMap<String, ArrayList<Refeicao>>());
                            cardapio.getAlmoco().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                            cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()).add(r);
                        }
                        break;
                    case 3:

                        if (cardapio.getJanta().get(horario) != null) {
                            if (cardapio.getJanta().get(horario).get(r.getTipo().getValor()) != null) {
                                cardapio.getJanta().get(horario).get(r.getTipo().getValor()).add(r);
                            } else {
                                cardapio.getJanta().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                                cardapio.getJanta().get(horario).get(r.getTipo().getValor()).add(r);
                            }

                        } else {
                            cardapio.getJanta().put(horario, new HashMap<String, ArrayList<Refeicao>>());
                            cardapio.getJanta().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                            cardapio.getJanta().get(horario).get(r.getTipo().getValor()).add(r);
                        }
                        break;
                }

                lista.add(cardapio);
            }

        }

        return lista;

    }

    public Cardapio get(int id) throws ClassNotFoundException, SQLException {
        return null;
    }
    public Cardapio get(String data) throws ClassNotFoundException, SQLException {
        Cardapio cardapio = new Cardapio();;
        Class.forName(DRIVER);
        Connection con = (Connection) DriverManager.getConnection(URL, USER, SENHA);
        System.out.println(data);
        PreparedStatement ps = con.prepareStatement("select * from CARDAPIO_API where dia = ? ");
        ps.setDate(1, Date.valueOf(data));
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            
            cardapio.setData(rs.getDate("dia").toString());
            int horario = rs.getInt("horario");
            Refeicao r = new Refeicao();
            r.setId(rs.getInt("id_refeicao"));
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
            switch (horario) {
                case 1:

                    if (cardapio.getDesjejum().get(horario) != null) {
                        if (cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()) != null) {
                            cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()).add(r);
                        } else {
                            cardapio.getDesjejum().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                            cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()).add(r);
                        }

                    } else {
                        cardapio.getDesjejum().put(horario, new HashMap<String, ArrayList<Refeicao>>());
                        cardapio.getDesjejum().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                        cardapio.getDesjejum().get(horario).get(r.getTipo().getValor()).add(r);
                    }

                    break;
                case 2:

                    if (cardapio.getAlmoco().get(horario) != null) {
                        if (cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()) != null) {
                            cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()).add(r);
                        } else {
                            cardapio.getAlmoco().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                            cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()).add(r);
                        }

                    } else {
                        cardapio.getAlmoco().put(horario, new HashMap<String, ArrayList<Refeicao>>());
                        cardapio.getAlmoco().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                        cardapio.getAlmoco().get(horario).get(r.getTipo().getValor()).add(r);
                    }
                    break;
                case 3:

                    if (cardapio.getJanta().get(horario) != null) {
                        if (cardapio.getJanta().get(horario).get(r.getTipo().getValor()) != null) {
                            cardapio.getJanta().get(horario).get(r.getTipo().getValor()).add(r);
                        } else {
                            cardapio.getJanta().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                            cardapio.getJanta().get(horario).get(r.getTipo().getValor()).add(r);
                        }

                    } else {
                        cardapio.getJanta().put(horario, new HashMap<String, ArrayList<Refeicao>>());
                        cardapio.getJanta().get(horario).put(r.getTipo().getValor(), new ArrayList<Refeicao>());
                        cardapio.getJanta().get(horario).get(r.getTipo().getValor()).add(r);
                    }
                    break;

            }

        }

        return cardapio;

    }

}
