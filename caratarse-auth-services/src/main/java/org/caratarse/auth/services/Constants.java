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

public class Constants
{
        public static int DEFAULT_RANGE_LIMIT = 20;
        
	/**
	 * These define the URL parmaeters used in the route definition strings (e.g. '{userId}').
	 */
	public class Url
	{
		//TODO: Your URL parameter names here...
		public static final String SAMPLE_ID = "sampleId";
		public static final String USER_ID = "userId";
		public static final String USER_UUID = "userUuid";
		public static final String POPULATE_ID = "populateId";
                public static final String AUTHORIZATION_NAME = "authorizationName";
                public static final String USER_AUTHORIZATION_ID = "userAuthorizationId";
	}

	/**
	 * These define the route names used in naming each route definitions.  These names are used
	 * to retrieve URL patterns within the controllers by name to create links in responses.
	 */
	public class Routes
	{
		//TODO: Your Route names here...
		public static final String SINGLE_SAMPLE = "sample.single.route";
		public static final String SAMPLE_COLLECTION = "sample.collection.route";
		public static final String USER_READ_ROUTE = "user.read.route";
		public static final String USER_COLLECTION_READ_ROUTE = "user.collection.read.route";
		public static final String SINGLE_POPULATE = "populate.single.route";
		public static final String POPULATE_COLLECTION = "populate.collection.route";
                public static final String USER_AUTHORIZATIONS_ROUTE = "user.authorizations.route";
                public static final String USER_AUTHORIZATION_ROUTE = "user.authorization.route";
	}
}
