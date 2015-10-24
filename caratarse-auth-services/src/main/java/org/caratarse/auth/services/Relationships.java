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

import java.util.Map;

import org.restexpress.RestExpress;
//import org.restexpress.example.blogging.domain.Blog;
//import org.restexpress.example.blogging.domain.BlogEntry;
//import org.restexpress.example.blogging.domain.Comment;

import com.strategicgains.hyperexpress.HyperExpress;
import com.strategicgains.hyperexpress.RelTypes;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserAuthorization;

public abstract class Relationships {

    public static void define(RestExpress server) {
        Map<String, String> routes = server.getRouteUrlsByName();

        HyperExpress.relationships()
                .forCollectionOf(User.class)
                    .rel(RelTypes.SELF, routes.get(Constants.Routes.USER_COLLECTION_READ_ROUTE))
                        .withQuery("filter={filter}")
                        .withQuery("limit={limit}")
                        .withQuery("offset={offset}")
                    .rel(RelTypes.NEXT, routes.get(Constants.Routes.USER_COLLECTION_READ_ROUTE) + "?offset={nextOffset}")
                        .withQuery("filter={filter}")
                        .withQuery("limit={limit}")
                        .optional()
                    .rel(RelTypes.PREV, routes.get(Constants.Routes.USER_COLLECTION_READ_ROUTE) + "?offset={prevOffset}")
                        .withQuery("filter={filter}")
                        .withQuery("limit={limit}")
                        .optional()
                .forClass(User.class)
                    .rel(RelTypes.SELF, routes.get(Constants.Routes.USER_READ_ROUTE))
                    .rel("userAuthorizations", routes.get(Constants.Routes.USER_AUTHORIZATIONS_ROUTE))
//                .forCollectionOf(UserService.class)
//                    .asRel("userServices")
//                    .rel(RelTypes.SELF, routes.get(Constants.Routes.USER_SERVICES_ROUTE))
//                    .rel(RelTypes.UP, routes.get(Constants.Routes.USER_READ_ROUTE))
//                .forClass(UserService.class)
//                    .rel(RelTypes.SELF, routes.get(Constants.Routes.USER_SERVICE_ROUTE))
//                    .rel("userAuthorizations", routes.get(Constants.Routes.USER_SERVICE_AUTHORIZATIONS_ROUTE))
                .forCollectionOf(UserAuthorization.class)
                    .asRel("userAuthorizations")
                    .rel(RelTypes.SELF, routes.get(Constants.Routes.USER_AUTHORIZATIONS_ROUTE))
                    .rel(RelTypes.UP, routes.get(Constants.Routes.USER_READ_ROUTE))
                .forClass(UserAuthorization.class)
                    .rel(RelTypes.SELF, routes.get(Constants.Routes.USER_AUTHORIZATION_ROUTE));

//		.forClass(User.class)
//			.rel(RelTypes.SELF, routes.get(Constants.Routes.SINGLE_USER))
//			.rel("entries", routes.get(Constants.Routes.BLOG_ENTRIES_READ_ROUTE))
//
//		.forCollectionOf(BlogEntry.class)
//			.asRel("entries")
//			.rel(RelTypes.SELF, routes.get(Constants.Routes.BLOG_ENTRIES_READ_ROUTE))
//				.withQuery("filter={filter}")
//				.withQuery("limit={limit}")
//				.withQuery("offset={offset}")
//			.rel(RelTypes.NEXT, routes.get(Constants.Routes.BLOG_ENTRIES_READ_ROUTE) + "?offset={nextOffset}")
//				.withQuery("filter={filter}")
//				.withQuery("limit={limit}")
//				.optional()
//			.rel(RelTypes.PREV, routes.get(Constants.Routes.BLOG_ENTRIES_READ_ROUTE) + "?offset={prevOffset}")
//				.withQuery("filter={filter}")
//				.withQuery("limit={limit}")
//				.optional()
//			.rel(RelTypes.UP, routes.get(Constants.Routes.BLOG_READ_ROUTE))
//
//		.forClass(BlogEntry.class)
//			.rel(RelTypes.SELF, routes.get(Constants.Routes.BLOG_ENTRY_READ_ROUTE))
//			.rel(RelTypes.UP, routes.get(Constants.Routes.BLOG_ENTRIES_READ_ROUTE))
//			.rel("comments", routes.get(Constants.Routes.COMMENTS_READ_ROUTE))
//
//		.forCollectionOf(Comment.class)
//			.rel(RelTypes.SELF, routes.get(Constants.Routes.COMMENTS_READ_ROUTE))
//				.withQuery("filter={filter}")
//				.withQuery("limit={limit}")
//				.withQuery("offset={offset}")
//			.rel(RelTypes.NEXT, routes.get(Constants.Routes.COMMENTS_READ_ROUTE) + "?offset={nextOffset}")
//				.withQuery("filter={filter}")
//				.withQuery("limit={limit}")
//				.optional()
//			.rel(RelTypes.PREV, routes.get(Constants.Routes.COMMENTS_READ_ROUTE) + "?offset={prevOffset}")
//				.withQuery("filter={filter}")
//				.withQuery("limit={limit}")
//				.optional()
//			.rel(RelTypes.UP, routes.get(Constants.Routes.COMMENT_READ_ROUTE))
//
//		.forClass(Comment.class)
//			.rel(RelTypes.SELF, routes.get(Constants.Routes.COMMENT_READ_ROUTE))
//			.rel(RelTypes.UP, routes.get(Constants.Routes.COMMENTS_READ_ROUTE));
    }
}
