/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author root
 */
public interface Controle<T> {
    public void add(T novo)throws ClassNotFoundException,SQLException;
    public String lista() throws ClassNotFoundException,SQLException;
    public String get(int id) throws ClassNotFoundException,SQLException;
    public void update(T e) throws ClassNotFoundException,SQLException;
    public void remove(int id) throws ClassNotFoundException,SQLException;
}
