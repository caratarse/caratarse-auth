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
package org.caratarse.auth.services.spring;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * An container for the Spring Application Context.
 * 
 * @author Lucio Benfante
 */
public class ApplicationContextHolder {

    public static final String DEFAULT_BASE_CONFIGURATION_PATH = "config";
    public static final String DEFAULT_ENVIRONMENT = "dev";
    private GenericApplicationContext context;

    public ApplicationContextHolder() {
        this(DEFAULT_ENVIRONMENT);
    }
    
    public ApplicationContextHolder(String environment) {
        if (StringUtils.isBlank(environment)) {
            environment = DEFAULT_ENVIRONMENT;
        }
        String configurationPath = DEFAULT_BASE_CONFIGURATION_PATH+"/"+environment+"/";
        context = new GenericApplicationContext();
        new XmlBeanDefinitionReader(context).loadBeanDefinitions(
                new String[]{
                    "classpath:org/lambico/spring/dao/hibernate/genericDao.xml",
                    "classpath:org/lambico/spring/dao/hibernate/applicationContextBase.xml",
                    "classpath:"+configurationPath+"database.xml",
                    "classpath:"+configurationPath+"applicationContext.xml"
                });
        context.refresh();
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }

    public void autowireBeanProperties(Object o) {
        context.getBeanFactory().autowireBeanProperties(o,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
    }
}
