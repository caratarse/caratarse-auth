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

import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.RestExpress;
import org.caratarse.auth.services.config.Configuration;

public abstract class Routes
{
	public static void define(Configuration config, RestExpress server)
    {
		//TODO: Your routes here...

		server.uri("/populates.{format}", config.getPopulateController())
			.action("readAll", HttpMethod.GET)
			.action("deleteAll", HttpMethod.DELETE)
			.name(Constants.Routes.POPULATE_COLLECTION);

                server.uri("/users/{userUuid}.{format}", config.getUserController())
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
			.name(Constants.Routes.USER_READ_ROUTE);
		server.uri("/users.{format}", config.getUserController())
			.action("readAll", HttpMethod.GET)
			.method(HttpMethod.POST)
			.name(Constants.Routes.USER_COLLECTION_READ_ROUTE);
                server.uri("/users/{userUuid}/services.{format}", config.getUserServiceController())
                        .action("readAll", HttpMethod.GET)
                        .name(Constants.Routes.USER_SERVICES_ROUTE);
                server.uri("/users/{userUuid}/services/{serviceName}.{format}", config.getUserServiceController())
			.method(HttpMethod.GET, HttpMethod.DELETE)
                        .action("addServiceToUser", HttpMethod.POST)
                        .name(Constants.Routes.USER_SERVICE_ROUTE);
                server.uri("/users/{userUuid}/services/{serviceName}/authorizations.{format}", config.getUserServiceAuthorizationController())
                        .action("readAll", HttpMethod.GET)
                        .name(Constants.Routes.USER_SERVICE_AUTHORIZATIONS_ROUTE);
                server.uri("/users/{userUuid}/services/{serviceName}/authorizations/{authorizationName}.{format}", config.getUserServiceAuthorizationController())
			.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
                        .action("addAuthorizationToUserForService", HttpMethod.POST)
                        .name(Constants.Routes.USER_SERVICE_AUTHORIZATION_ROUTE);
//// or...
//		server.regex("/some.regex", config.getRouteController());
    }
}
