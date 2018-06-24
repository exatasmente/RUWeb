/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import controller.ControleTipoRefeicao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.TipoRefeicao;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author root
 */
public class ServletTipoRefeicao extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String jsonStr = "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";
        PrintWriter out = response.getWriter();
        ControleTipoRefeicao res = new ControleTipoRefeicao();

        try {
            String querry = request.getPathInfo();
            System.out.println(querry);
            if (querry != null) {
                String data = querry.replaceFirst("/", "");
                if (Integer.getInteger(data) != null) {
                    jsonStr = res.get(Integer.parseInt(data));
                } else {
                    jsonStr = res.get(data);
                }
            } else {
                jsonStr = res.lista();
            }
        } catch (NumberFormatException e) {
            jsonStr = "{\"message\" : \"Id Inválido \", \"Status\" : \"403\"}";
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServletAluno.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServletAluno.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.write(jsonStr);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ControleTipoRefeicao res = new ControleTipoRefeicao();
        String jsonStr = "{\"message\" : \"TipoRefeicao Criado Com Sucesso \", \"Status\" : \"200\"}";
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            StringBuffer jb = new StringBuffer();
            String line = null;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);

            }
            System.out.println(jb.toString());
            TipoRefeicao refeicao = mapperObj.readValue(jb.toString(), TipoRefeicao.class);
            res.add(refeicao);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            jsonStr = "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";
        } finally {
            out.write(jsonStr);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ControleTipoRefeicao res = new ControleTipoRefeicao();
        String jsonStr = "{\"message\" : \"TipoRefeicao Alterado Com Sucesso \", \"Status\" : \"200\"}";
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            StringBuffer jb = new StringBuffer();
            String line = null;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);

            }
            System.out.println(jb.toString());
            TipoRefeicao refeicao = mapperObj.readValue(jb.toString(), TipoRefeicao.class);
            res.update(refeicao);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            jsonStr = "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";
        } finally {
            out.write(jsonStr);
        }

    }

    @Override
    /*
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ControleTipoRefeicao res = new ControleTipoRefeicao();
        String jsonStr = "{\"message\" : \"TipoRefeicao Removido Com Sucesso \", \"Status\" : \"200\"}";
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            StringBuffer jb = new StringBuffer();
            String line = null;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);

            }
            System.out.println(jb.toString());
            TipoRefeicao refeicao= mapperObj.readValue(jb.toString(), TipoRefeicao.class);
            //res.remove(refeicao.getAlmoco());

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            jsonStr = "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";
        } finally {
            out.write(jsonStr);
        }
    }

    @Override
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
