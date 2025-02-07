/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.spring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import com.vaadin.flow.server.Constants;

import org.atmosphere.cpr.ApplicationConfig;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Abstract Vaadin Spring MVC {@link WebApplicationInitializer}.
 * <p>
 * Extend this class in your Spring MVC application and provide your
 * configuration classes via the {@link #getConfigurationClasses()} method.
 *
 * @author Vaadin Ltd
 *
 */
public abstract class VaadinMVCWebAppInitializer
        implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(servletContext);
        registerConfiguration(context);
        servletContext.addListener(new ContextLoaderListener(context));
        context.refresh();
        Environment env = context.getBean(Environment.class);
        String mapping = RootMappedCondition.getUrlMapping(env);
        if (mapping == null) {
            mapping = "/*";
        }
        String pushRegistrationPath;

        boolean rootMapping = RootMappedCondition.isRootMapping(mapping);
        Dynamic registration = servletContext.addServlet(
                ClassUtils.getShortNameAsProperty(SpringServlet.class),
                new SpringServlet(context, rootMapping));
        Map<String, String> initParameters = new HashMap<>();
        if (rootMapping) {
            Dynamic dispatcherRegistration = servletContext
                    .addServlet("dispatcher", new DispatcherServlet(context));
            dispatcherRegistration.addMapping("/*");
            mapping = VaadinServletConfiguration.VAADIN_SERVLET_MAPPING;
            pushRegistrationPath = "";
        } else {
            pushRegistrationPath = mapping.replace("/*", "");
        }
        registration.addMapping(mapping);

        /*
         * Tell Atmosphere which servlet to use for the push endpoint. Servlet
         * mappings are returned as a Set from at least Tomcat so even if
         * Atmosphere always picks the first, it might end up using /VAADIN/*
         * and websockets will fail.
         */
        initParameters.put(ApplicationConfig.JSR356_MAPPING_PATH,
                pushRegistrationPath);
        initParameters.put(ApplicationConfig.JSR356_PATH_MAPPING_LENGTH, "0");

        registration.setInitParameters(initParameters);

        registration.setAsyncSupported(
                Boolean.TRUE.toString().equals(env.getProperty(
                        "vaadin.asyncSupported", Boolean.TRUE.toString())));
    }

    static String makeContextRelative(String url) {
        // / -> context://
        // foo -> context://foo
        // /foo -> context://foo
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        return "context://" + url;
    }

    /**
     * Registers application configuration classes.
     * <p>
     * Uses developer defined configuration classes via the
     * {@link #getConfigurationClasses()} method. Also register Vaadin
     * configuration from the add-on.
     * <p>
     * Override this method if you want to register configuration classes in a
     * totally different way or just provide implementation for
     * {@link #getConfigurationClasses()} method.
     *
     * @see #getConfigurationClasses()
     *
     * @param context
     *            web application context, not {@code null}
     */
    protected void registerConfiguration(
            AnnotationConfigWebApplicationContext context) {
        Stream<Class<? extends Object>> configs = Stream.concat(
                Stream.of(VaadinScopesConfig.class,
                        VaadinServletConfiguration.class,
                        VaadinApplicationConfiguration.class),
                getConfigurationClasses().stream());
        context.register(configs.toArray(Class<?>[]::new));
    }

    /**
     * Gets the application configuration classes.
     *
     * @return a collection of configuration classes
     */
    protected abstract Collection<Class<?>> getConfigurationClasses();

}
