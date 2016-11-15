package com.mycompany.aprendendoservlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class FilterApi implements Filter {

    public void doFilter(ServletRequest request,
            ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        tempoExecucao(chain, request, response);

    }

    private void tempoExecucao(FilterChain chain, ServletRequest request, ServletResponse response) throws IOException, ServletException {
        long tempoInicial = System.currentTimeMillis();
        
        chain.doFilter(request, response);
        
        long tempoFinal = System.currentTimeMillis();
        String uri = ((HttpServletRequest) request).getRequestURI();
        String parametros = ((HttpServletRequest) request)
                .getParameter("logica");
        System.out.println("Tempo da requisicao de " + uri
                + "?logica="
                + parametros + " demorou (ms): "
                + (tempoFinal - tempoInicial));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("inicio");
    }

    @Override
    public void destroy() {
        System.out.println("fim");
    }
}
