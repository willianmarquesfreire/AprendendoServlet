/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aprendendoservlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author wmfsystem
 */
public class ParentServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("------------- iniciando");

        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        MyContextListener.resources
                .forEach((clazz, value) -> {
                    try {
                        Class.forName(clazz.getCanonicalName());
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ParentServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    value.forEach((uri, method) -> {
                        method.setAccessible(true);

                        if (uri.equals(request.getServletPath())) {
                            try {
                                Object result = method.invoke(clazz.newInstance(), null);
                                String json = parseJson(result);
                                response.getWriter().write(json);
                            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException ex) {
                                Logger.getLogger(ParentServlet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                });
    }

    private String parseJson(Object result) {
        return "{nome:'fd'}";
    }

}
