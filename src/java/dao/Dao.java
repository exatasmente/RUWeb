/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.SQLException;
import java.util.ArrayList;



/**
 *
 * @author root
 * @param <T>
 */
public interface Dao<T> {
    final String DRIVER = "org.postgresql.Driver";
    final String USER = "postgres";
    final String SENHA = "383842";
    final String URL = "jdbc:postgresql://localhost:5432/ruweb";
    public ArrayList<T> lista() throws ClassNotFoundException,SQLException;
    public T get(int id) throws ClassNotFoundException,SQLException;
    public void remove(int id) throws ClassNotFoundException,SQLException;
    public void update(T e) throws ClassNotFoundException,SQLException;
    public void add(T e) throws ClassNotFoundException,SQLException;
}
