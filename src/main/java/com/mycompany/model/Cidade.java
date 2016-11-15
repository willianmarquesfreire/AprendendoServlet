package com.mycompany.model;

/**
 * @author wmfsystem
 */
public class Cidade {

    private String name;
    private CEP cep;

    public Cidade(String name, CEP cep) {
        this.name = name;
        this.cep = cep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CEP getCep() {
        return cep;
    }

    public void setCep(CEP cep) {
        this.cep = cep;
    }

}
