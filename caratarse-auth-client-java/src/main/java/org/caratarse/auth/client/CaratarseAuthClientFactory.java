/**
 * Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>
 *
 * This file is part of Caratarse Auth Client Java.
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
package org.caratarse.auth.client;

import org.caratarse.auth.client.apiversion0100.CaratarseAuthClient0100Impl;
import java.util.Properties;

/**
 * A factory for Caratarse-auth client implementations.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class CaratarseAuthClientFactory {
    public static final String DEFAULT_BASE_URL = "http://localhost:8081";
    public static final String DEFAULT_FORMAT = ".json";
    public static final String DEFAULT_VERSION = "1.0";
    public static final String PROPERTY_API_VERSION = "caratarse.auth.apiVersion";
    public static final String PROPERTY_DEFAULT_FORMAT = "caratarse.auth.defaultFormat";
    public static final String PROPERTY_BASE_URL = "caratarse.auth.baseUrl";

    private CaratarseAuthClientFactory() {
    }
    
    public static CaratarseAuthClient getClient() {
        return CaratarseAuthClientFactory.getClient(null);
    }

    public static CaratarseAuthClient getClient(Properties properties) {
        Properties configuration = CaratarseAuthClientFactory.fixBaseProperties(properties);
        CaratarseAuthClient result = null;
        String version = configuration.getProperty(PROPERTY_API_VERSION);
        switch (version) {
            case "1.0":
                result = new CaratarseAuthClient0100Impl(configuration);
                break;
            default:
                throw new IllegalArgumentException("No available version: " + version);
        }
        return result;
    }

    private static Properties fixBaseProperties(Properties properties) {
        Properties configuration = System.getProperties();
        if (properties != null) {
            configuration.putAll(properties);
        }
        configuration.put(PROPERTY_BASE_URL, configuration.getProperty(PROPERTY_BASE_URL, DEFAULT_BASE_URL));
        configuration.put(PROPERTY_DEFAULT_FORMAT, configuration.getProperty(PROPERTY_DEFAULT_FORMAT, DEFAULT_FORMAT));
        configuration.put(PROPERTY_API_VERSION, configuration.getProperty(PROPERTY_API_VERSION, DEFAULT_VERSION));
//        configuration.putIfAbsent(PROPERTY_BASE_URL, DEFAULT_BASE_URL);
//        configuration.putIfAbsent(PROPERTY_DEFAULT_FORMAT, DEFAULT_FORMAT);
//        configuration.setProperty(PROPERTY_API_VERSION, DEFAULT_VERSION);
        return configuration;
    }

}
