package com.mycompany.aprendendoservlet;

import com.mycompany.annotations.Api;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyContextListener implements ServletContextListener {

    public static Map<Class, Map<String, Method>> resources;

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
    
    @Override
    public void contextInitialized(ServletContextEvent event) {

        resources = new HashMap<>();

        ServletContext context = event.getServletContext();
        Dynamic dynamic = context.addServlet(ParentServlet.class.getSimpleName(), ParentServlet.class);
        dynamic.addMapping("/");

        try {
            Class[] classes = Clazz.getClasses("com.mycompany.api");
            for (Class clazz : classes) {
                
                if (clazz.isAnnotationPresent(Api.class)) {
                    Map<String, Method> mapMethods = new HashMap<>();
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(Api.class)) {
                            mapMethods.put(((Api) clazz.getAnnotation(Api.class)).url().concat(method.getAnnotation(Api.class).url()), method);
                        }
                    }
                    resources.put(clazz, mapMethods);
                }
            }

            resources
                    .forEach((clazz, method) -> {
                        method
                                .forEach((uri, value) -> {
                                    dynamic.addMapping(uri);
                                    System.out.println("Registered: " + uri + " -> " + value.getName());
                                });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
