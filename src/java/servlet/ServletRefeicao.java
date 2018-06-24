/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import controller.ControleRefeicao;
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
import model.Refeicao;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author root
 */
public class ServletRefeicao extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(500);
        String jsonStr = "{\"message\" : \"Internal Error \", \"Status\" : \"500\"}";
        PrintWriter out = response.getWriter();
        ControleRefeicao res = new ControleRefeicao();

        try {
            String querry = request.getPathInfo();
            if (querry != null) {
                if (querry.contains("tipo")) {
                    String tipo = querry.subSequence(querry.lastIndexOf("/") + 1, querry.length()).toString();
                    if (tipo.length() > 0) {
                        jsonStr = res.getByTipo(tipo);
                    } else {
                        throw new NumberFormatException();
                    }
                } else {

                    int id = Integer.parseInt(querry.replace("/", ""));
                    jsonStr = res.get(id);

                }

            } else {
                jsonStr = res.lista();
            }

            response.setStatus(200);
        } catch (NumberFormatException e) {
            response.setStatus(404);
            jsonStr = "{\"message\" : \"Id Inválido \", \"Status\" : \"404\"}";
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
        ControleRefeicao res = new ControleRefeicao();
        String jsonStr = "{\"message\" : \"Refeicao Criado Com Sucesso \", \"Status\" : \"200\"}";
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            StringBuffer jb = new StringBuffer();
            String line = null;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);

            }
            System.out.println(jb.toString());
            Refeicao refeicao = mapperObj.readValue(jb.toString(), Refeicao.class);
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
        ControleRefeicao res = new ControleRefeicao();
        String jsonStr = "{\"message\" : \"Refeicao Alterado Com Sucesso \", \"Status\" : \"200\"}";
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            StringBuffer jb = new StringBuffer();
            String line = null;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);

            }
            System.out.println(jb.toString());
            Refeicao refeicao = mapperObj.readValue(jb.toString(), Refeicao.class);
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
        ControleRefeicao res = new ControleRefeicao();
        String jsonStr = "{\"message\" : \"Refeicao Removido Com Sucesso \", \"Status\" : \"200\"}";
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            StringBuffer jb = new StringBuffer();
            String line = null;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);

            }
            System.out.println(jb.toString());
            Refeicao refeicao= mapperObj.readValue(jb.toString(), Refeicao.class);
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
