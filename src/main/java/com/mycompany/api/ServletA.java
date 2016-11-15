package com.mycompany.api;

import com.mycompany.annotations.Api;
import com.mycompany.model.Pessoa;
import java.io.IOException;

import javax.servlet.ServletException;

@Api(url = "/teste")
public class ServletA {

    private static final long serialVersionUID = 1L;

    public ServletA() {

    }

    @Api(url = "/ola")
    public String construa() throws ServletException, IOException {
        return "<h1>ola danilo</h1>";
    }

    @Api(url = "/muitolegal")
    public String muitoLegal() throws ServletException, IOException {
        return "<h1>muito legal</h1>";
    }
    
    @Api(url = "/pessoa")
    public Pessoa getPessoa() throws ServletException, IOException {
        Pessoa p = new Pessoa();
        p.setNome("Willian");
        p.setIdade(20);
        return p;
    }

}