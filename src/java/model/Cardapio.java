/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author root
 */
public class Cardapio implements Serializable {
    
    private HashMap<Integer, HashMap<String,ArrayList<Refeicao>>> desjejum ;
    private HashMap<Integer, HashMap<String,ArrayList<Refeicao>>>  almoco ;
    private HashMap<Integer, HashMap<String,ArrayList<Refeicao>>>  janta ;
    private String data;

    public Cardapio() {
        this.desjejum = new HashMap<> ();
        this.janta = new HashMap<> ();
        this.almoco = new HashMap<> ();
    }

    public void setDesjejum(HashMap<Integer, HashMap<String,ArrayList<Refeicao>>>  desjejum) {
        this.desjejum = desjejum;
    }

    public void setAlmoco(HashMap<Integer, HashMap<String,ArrayList<Refeicao>>>  almoco) {
        this.almoco = almoco;
    }

    public void setJanta(HashMap<Integer, HashMap<String,ArrayList<Refeicao>>>  janta) {
        this.janta = janta;
    }

    
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    
    public HashMap<Integer, HashMap<String,ArrayList<Refeicao>>>  getDesjejum() {
        return desjejum;
    }

    public HashMap<Integer, HashMap<String,ArrayList<Refeicao>>>  getAlmoco() {
        return almoco;
    }

    public HashMap<Integer, HashMap<String,ArrayList<Refeicao>>>  getJanta() {
        return janta;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cardapio other = (Cardapio) obj;
        
        return this.data.equals(other.getData());
    }
    
    
}
