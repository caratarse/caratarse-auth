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
package org.caratarse.auth.services.controller;

import com.strategicgains.hyperexpress.HyperExpress;
import com.strategicgains.hyperexpress.builder.TokenBinder;
import com.strategicgains.hyperexpress.builder.TokenResolver;
import com.strategicgains.hyperexpress.builder.UrlBuilder;
import com.strategicgains.syntaxe.ValidationException;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.caratarse.auth.model.bo.AuthorizationBo;
import org.caratarse.auth.model.bo.ServiceBo;
import org.caratarse.auth.model.bo.UserAuthorizationBo;
import org.caratarse.auth.model.bo.UserBo;
import org.caratarse.auth.model.dao.UserAuthorizationDao;
import org.caratarse.auth.model.po.Authorization;
import org.caratarse.auth.model.po.Service;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserAuthorization;
import org.caratarse.auth.services.Constants;
import org.caratarse.auth.services.util.HyperExpressBindHelper;
import org.lambico.dao.generic.Page;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.common.query.QueryFilter;
import org.restexpress.common.query.QueryOrder;
import org.restexpress.common.query.QueryRange;
import org.restexpress.query.QueryFilters;
import org.restexpress.query.QueryOrders;
import org.restexpress.query.QueryRanges;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceAuthorizationController {

    private static final UrlBuilder LOCATION_BUILDER = new UrlBuilder();
    @Resource
    private UserBo userBo;
    @Resource
    private ServiceBo serviceBo;
    @Resource
    private AuthorizationBo authorizationBo;
    @Resource
    private UserAuthorizationDao userAuthorizationDao;
    @Resource
    private UserAuthorizationBo userAuthorizationBo;

    public UserServiceAuthorizationController() {
    }

    public UserAuthorization addAuthorizationToUserForService(Request request, Response response) {
        String userUuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        String serviceName = request.getHeader(Constants.Url.SERVICE_NAME,
                "No Service Name supplied");
        String authorizationName = request.getHeader(Constants.Url.AUTHORIZATION_NAME,
                "No Authorization Name supplied");
        UserAuthorization userAuthorizationPermissions = request.getBodyAs(UserAuthorization.class,
                "UserAuthorization with permissions details not provided");

        validateAndThrow(userUuid, serviceName, authorizationName);

        UserAuthorization userAuthorization = userAuthorizationBo.addAuthorizationToUserForService(
                userUuid, serviceName, authorizationName,
                userAuthorizationPermissions.getPermissions());

        // Construct the response for create...
        response.setResponseCreated();

        TokenResolver resolver = HyperExpress.bind(Constants.Url.USER_UUID, userUuid)
                .bind(Constants.Url.SERVICE_NAME, serviceName)
                .bind(Constants.Url.AUTHORIZATION_NAME, authorizationName);

        // Include the Location header...
        String locationPattern = request.getNamedUrl(HttpMethod.GET,
                Constants.Routes.USER_READ_ROUTE);
        response.addLocationHeader(LOCATION_BUILDER.build(locationPattern, resolver));

        // Return the newly-created item...
        return userAuthorization;
    }

    public UserAuthorization read(Request request, Response response) {
        String userUuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        String serviceName = request.getHeader(Constants.Url.SERVICE_NAME,
                "No Service Name supplied");
        String authorizationName = request.getHeader(Constants.Url.AUTHORIZATION_NAME,
                "No Authorization Name supplied");
        UserAuthorization userAuthorization = userAuthorizationBo.findUserAuthorization(userUuid,
                serviceName, authorizationName);

        addTokenBinder();

        return userAuthorization;
    }

    @Transactional
    public List<UserAuthorization> readAll(Request request, Response response) {
        String uuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        String serviceName = request.getHeader(Constants.Url.SERVICE_NAME,
                "No UserService ID supplied");
        QueryFilter filter = QueryFilters.parseFrom(request);
        QueryOrder order = QueryOrders.parseFrom(request);
        QueryRange range = QueryRanges.parseFrom(request, Constants.DEFAULT_RANGE_LIMIT);
        Page<UserAuthorization> entities
                = userAuthorizationBo.readAllByUserAndService(uuid, serviceName, filter, range, order);
        response.setCollectionResponse(range, entities.getList().size(), entities.getRowCount());
        HyperExpressBindHelper.bindPaginationTokens(range, entities.getRowCount());
        addTokenBinder();

        return entities.getList();
    }

    private void addTokenBinder() {
        // Bind the resources in the collection with link URL tokens, etc. here...
        HyperExpress.tokenBinder(new TokenBinder<UserAuthorization>() {
            @Override
            public void bind(UserAuthorization entity, TokenResolver resolver) {
                resolver.bind(Constants.Url.USER_AUTHORIZATION_ID, entity.getId().toString())
                        .bind(Constants.Url.AUTHORIZATION_NAME, entity.getAuthorization().getName())
                        .bind(Constants.Url.SERVICE_NAME, entity.getAuthorization().getService().
                                getName())
                        .bind(Constants.Url.USER_UUID, entity.getUser().getUuid())
                        .bind(Constants.Url.USER_ID, entity.getUser().getId().toString());
            }
        });
    }

    public void update(Request request, Response response) {
        String userUuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        String serviceName = request.getHeader(Constants.Url.SERVICE_NAME,
                "No Service Name supplied");
        String authorizationName = request.getHeader(Constants.Url.AUTHORIZATION_NAME,
                "No Authorization Name supplied");
        UserAuthorization dbUserAuthorization
                = userAuthorizationBo.findUserAuthorization(userUuid, serviceName, authorizationName);
        if (dbUserAuthorization == null) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
            return;
        }

        UserAuthorization userAuthorizationPermissions = request.getBodyAs(UserAuthorization.class,
                "UserAuthorization with permissions details not provided");
        dbUserAuthorization.copy(userAuthorizationPermissions);
        userAuthorizationBo.store(dbUserAuthorization);
        response.setResponseNoContent();
    }

    public void delete(Request request, Response response) {
        String userUuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        String serviceName = request.getHeader(Constants.Url.SERVICE_NAME,
                "No Service Name supplied");
        String authorizationName = request.getHeader(Constants.Url.AUTHORIZATION_NAME,
                "No Authorization Name supplied");
        userAuthorizationBo.delete(userUuid, serviceName, authorizationName);
        response.setResponseNoContent();
    }

    private void validateAndThrow(String userUuid, String serviceName, String authorizationName) {
        List<String> errors = new ArrayList<>();
        User user = userBo.getUser(userUuid);
        if (user == null) {
            errors.add("User with UUID " + userUuid + " not found.");
        }
        Service service = serviceBo.getService(serviceName);
        if (service == null) {
            errors.add("Service with name " + serviceName + " not found.");
        }
        Authorization authorization
                = authorizationBo.findAuthorization(serviceName, authorizationName);
        if (authorization == null) {
            errors.add("Authorization with name " + authorizationName + " for service "
                    + serviceName + " not found.");
        }
        UserAuthorization userAuthorization
                = userAuthorizationBo.
                findUserAuthorization(userUuid, serviceName, authorizationName);
        if (userAuthorization != null) {
            errors.add("Authorization " + authorizationName + " for service " + serviceName
                    + " already attached to user " + userUuid + ".");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
