/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aprendendoservlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
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
//        response.setContentType("application/json");
        MyContextListener.resources
                .forEach((Class clazz, Map<String, Method> value) -> {
                    try {
                        Class.forName(clazz.getCanonicalName());
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ParentServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    value.forEach((String uri, Method method) -> {
                        method.setAccessible(true);

                        boolean uriEquals = request.getServletPath().matches(".*".concat(uri.replaceAll("/", ".")));
                        if (uriEquals) {
                            try {
                                Object result = method.invoke(clazz.newInstance(), null);

                                StringBuilder toReturn = new StringBuilder();

                                if (!result.getClass().getCanonicalName().equals("java.lang.String")
                                        && !result.getClass().getCanonicalName().equals("java.lang.Double")
                                        && !result.getClass().getCanonicalName().equals("java.lang.Float")
                                        && !result.getClass().getCanonicalName().equals("java.lang.Byte")
                                        && !result.getClass().getCanonicalName().equals("java.lang.Boolean")) {
                                    toReturn.append(parseJson(result));
                                } else {
                                    toReturn.append(result);
                                }
                                response.getWriter().write(toReturn.toString());
                            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException ex) {
                                Logger.getLogger(ParentServlet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                });
    }

    private String parseJson(Object obj) {
        StringBuffer json = new StringBuffer("{");
        List<Field> allFields = getAllFields(new LinkedList<>(), obj.getClass());
        try {
            for (Field field : allFields) {
                field.setAccessible(true);
                json.append("\"".concat(field.getName()).concat("\""));
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

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    private void verifyGetMethods(Object obj, Field field, StringBuffer json) {
        try {
            for (Method method : obj.getClass().getMethods()) {
                if (method.getName().startsWith("get")
                        && method.getName().substring(3).equalsIgnoreCase(field.getName())) {
                    method.setAccessible(true);
                    Object invoke = method.invoke(obj);
                    if (method.getReturnType().toString().contains("class")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.String")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.Double")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.Float")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.Byte")
                            && !method.getReturnType().getCanonicalName().equals("java.lang.Boolean")) {

                        if (invoke != null) {
                            json.append(parseJson(method.invoke(obj)));
                        } else {
                            json.append("null");
                        }
                    } else if (invoke != null) {
                        json.append("\"".concat(invoke.toString()).concat("\""));
                    } else {
                        json.append("null");
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ParentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
