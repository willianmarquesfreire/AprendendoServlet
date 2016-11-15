/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aprendendoservlet;

import com.mycompany.model.Endereco;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        response.setContentType("application/json");
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

    private String parseJson(Object obj) {
        System.out.println("ee ---- " + obj.getClass().getFields().length);
        StringBuffer json = new StringBuffer("{");
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                json.append("\"" + field.getName() + "\"");
                json.append(":");
                verifyGetMethods(obj, field, json);
                json.append(",");
            }
            json.replace(json.length() - 1, json.length(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.append("}");
        return json.toString();
    }

    private void verifyGetMethods(Object obj, Field field, StringBuffer json) {
        try {
            for (Method method : obj.getClass().getDeclaredMethods()) {
                if (method.getName().startsWith("get")
                        && method.getName().substring(3).equalsIgnoreCase(field.getName())) {
                    method.setAccessible(true);
                    if (method.getReturnType().toString().contains("class")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.String")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.Double")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.Float")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.Byte")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.Boolean")) {

                        json.append(parseJson(method.invoke(obj)));

                    } else {
                        json.append("\"" + method.invoke(obj) + "\"");

                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ParentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
