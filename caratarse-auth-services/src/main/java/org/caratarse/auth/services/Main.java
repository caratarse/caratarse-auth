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
package org.caratarse.auth.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strategicgains.hyperexpress.domain.hal.HalResourceFactory;
import com.strategicgains.hyperexpress.domain.siren.SirenResourceFactory;
import com.strategicgains.restexpress.plugin.cache.CacheControlPlugin;
import com.strategicgains.restexpress.plugin.cors.CorsHeaderPlugin;
import com.strategicgains.restexpress.plugin.route.RoutesMetadataPlugin;
import com.strategicgains.restexpress.plugin.swagger.SwaggerPlugin;
import static io.netty.handler.codec.http.HttpHeaders.Names.ACCEPT;
import static io.netty.handler.codec.http.HttpHeaders.Names.AUTHORIZATION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpHeaders.Names.REFERER;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.caratarse.auth.model.po.Linkable;

import org.restexpress.RestExpress;
import org.restexpress.pipeline.SimpleConsoleLogMessageObserver;
import org.caratarse.auth.services.config.Configuration;
import org.caratarse.auth.services.plugins.transactions.OpenTransactionPlugin;
import org.caratarse.auth.services.serialization.SerializationProvider;
import org.restexpress.ContentType;
import org.restexpress.Flags;
import static org.restexpress.Flags.Auth.PUBLIC_ROUTE;
import org.restexpress.plugin.hyperexpress.HyperExpressPlugin;
import org.restexpress.util.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final String SERVICE_NAME = "caratarse-auth";
    private static final Logger LOG = LoggerFactory.getLogger(SERVICE_NAME);
    private static boolean first = true;
    private static int counter = 0;

    public static void main(String[] args) throws Exception {
        RestExpress server = initializeServer(args);
        server.awaitShutdown();
    }

    public static RestExpress initializeServer(String[] args) throws IOException {
        RestExpress.setSerializationProvider(new SerializationProvider());

        Configuration config = loadEnvironment(args);
        RestExpress server = new RestExpress()
                .setName(SERVICE_NAME)
                .setBaseUrl(config.getBaseUrl())
                .setExecutorThreadCount(config.getExecutorThreadPoolSize())
                .addMessageObserver(new SimpleConsoleLogMessageObserver());
        Routes.define(config, server);
        Relationships.define(server);
        
        
        new RoutesMetadataPlugin()
                .flag(PUBLIC_ROUTE) // optional. Set a flag on the request for this route.
                .register(server);
        
        new SwaggerPlugin()
                .flag(Flags.Auth.PUBLIC_ROUTE)
                .register(server);

        new CacheControlPlugin() // Support caching headers.
                .register(server);
        
        final HyperExpressPlugin hyperExpressPlugin = new HyperExpressPlugin(Linkable.class);
        if (first) {
            HalResourceFactory hal = new HalResourceFactory();
            hal.excludeAnnotations(JsonIgnore.class);
            hyperExpressPlugin.addResourceFactory(hal, ContentType.HAL_JSON);
            hyperExpressPlugin.addResourceFactory(hal, ContentType.JSON);

            SirenResourceFactory siren = new SirenResourceFactory();
            siren.excludeAnnotations(JsonIgnore.class);
            hyperExpressPlugin.addResourceFactory(siren, ContentType.SIREN);
        } else {
            HalResourceFactory hal = new HalResourceFactory();
            hal.excludeAnnotations(JsonIgnore.class);
            hyperExpressPlugin.addResourceFactory(hal, ContentType.JSON+counter);
        }
        hyperExpressPlugin.register(server);
        
        new CorsHeaderPlugin("*")
                .flag(PUBLIC_ROUTE)
                .allowHeaders(CONTENT_TYPE, ACCEPT, AUTHORIZATION, REFERER, LOCATION)
                .exposeHeaders(LOCATION)
                .register(server);
        
        final OpenTransactionPlugin openTransactionPlugin = new OpenTransactionPlugin();
        config.getContextHolder().autowireBeanProperties(openTransactionPlugin);
        openTransactionPlugin.register(server);
        server.bind(config.getPort());
        if (first) {
            first = false;
        }
        counter++;
        return server;
    }

    private static Configuration loadEnvironment(String[] args)
            throws FileNotFoundException, IOException {
        if (args.length > 0) {
            return Environment.from(args[0], Configuration.class);
        }

        return Environment.fromDefault(Configuration.class);
    }
}
