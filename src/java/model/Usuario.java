/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author root
 */
public class Usuario extends Pessoa{
    int tipo;
    boolean ativo;
    Cartao cartao;
    public Usuario(){}
    public Usuario(int id, String nome,String sobrenome, String email, String login, String senha,int tipo, boolean ativo, Cartao cartao) {
        super(id, nome,sobrenome, email, login, senha);
        this.tipo = tipo;
        this.ativo = ativo;
        this.cartao = cartao;
    }

    
    
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }
    
    
}
