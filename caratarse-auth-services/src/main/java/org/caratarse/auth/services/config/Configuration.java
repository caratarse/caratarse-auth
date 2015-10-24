/**
 * Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>
 *
 * This file is part of Caratarse Auth Services.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.caratarse.auth.services.config;

import java.util.Properties;
import org.caratarse.auth.services.controller.PopulateController;

import org.restexpress.RestExpress;
import org.caratarse.auth.services.controller.UserController;
import org.caratarse.auth.services.controller.UserAuthorizationController;
import org.caratarse.auth.services.spring.ApplicationContextHolder;
import org.restexpress.util.Environment;

public class Configuration
        extends Environment {

    public static final String DEFAULT_ENVIRONMENT = "dev";
    private static final String DEFAULT_EXECUTOR_THREAD_POOL_SIZE = "20";

    private static final String PORT_PROPERTY = "port";
    private static final String BASE_URL_PROPERTY = "base.url";
    private static final String EXECUTOR_THREAD_POOL_SIZE = "executor.threadPool.size";
    private static final String ENVIRONMENT_PROPERTY = "environment";

    private int port;
    private String baseUrl;
    private int executorThreadPoolSize;
    private String environment;
    private ApplicationContextHolder contextHolder;

    private PopulateController populateController;
    private UserController userController;
    private UserAuthorizationController userAuthorizationController;

    @Override
    protected void fillValues(Properties p) {
        this.port = Integer.parseInt(p.getProperty(PORT_PROPERTY, String.valueOf(
                RestExpress.DEFAULT_PORT)));
        this.baseUrl = p.getProperty(BASE_URL_PROPERTY, "http://localhost:" + String.valueOf(port));
        this.executorThreadPoolSize = Integer.parseInt(p.getProperty(EXECUTOR_THREAD_POOL_SIZE,
                DEFAULT_EXECUTOR_THREAD_POOL_SIZE));
        this.environment = p.getProperty(ENVIRONMENT_PROPERTY, DEFAULT_ENVIRONMENT);
        initializeSpringContext(environment);
        initialize();
    }

    private void initialize() {
        populateController = new PopulateController();
        contextHolder.autowireBeanProperties(populateController);
        userController = new UserController();
        contextHolder.autowireBeanProperties(userController);
        userAuthorizationController = new UserAuthorizationController();
        contextHolder.autowireBeanProperties(userAuthorizationController);
    }

    private void initializeSpringContext(String environment) {
        this.contextHolder = new ApplicationContextHolder(environment);
    }

    public int getPort() {
        return port;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getExecutorThreadPoolSize() {
        return executorThreadPoolSize;
    }

    public PopulateController getPopulateController() {
        return populateController;
    }
    
    public UserController getUserController() {
        return userController;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public ApplicationContextHolder getContextHolder() {
        return contextHolder;
    }

    public void setContextHolder(ApplicationContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }

    public UserAuthorizationController getUserAuthorizationController() {
        return userAuthorizationController;
    }
    
}
