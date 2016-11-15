package com.mycompany.model;

/**
 * @author wmfsystem
 */
public class Endereco {

    private int numero;
    private String rua;
    private Cidade cidade;

    public Endereco() {
    }

    public Endereco(int numero, String rua, Cidade cidade) {
        this.numero = numero;
        this.rua = rua;
        this.cidade = cidade;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    @Override
    public String toString() {
        return "Endereco{" + "numero=" + numero + ", rua=" + rua + '}';
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

}
