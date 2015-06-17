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
import static com.strategicgains.repoexpress.adapter.Identifiers.UUID;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.List;
import javax.annotation.Resource;
import org.caratarse.auth.model.bo.UserBo;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.services.Constants;
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

public class UserController {

    @Resource
    private UserBo userBo;

    public UserController() {
    }

    public Object create(Request request, Response response) {
        //TODO: Your 'POST' logic here...
        return null;
    }

    public User read(Request request, Response response) {
        String uuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        User user = userBo.getUser(uuid);
        if (user == null) {
            response.setResponseStatus(HttpResponseStatus.NOT_FOUND);
        }

        addTokenBinder();

        return user;
    }

    @Transactional
    public List<User> readAll(Request request, Response response) {
        QueryFilter filter = QueryFilters.parseFrom(request);
        QueryOrder order = QueryOrders.parseFrom(request);
        QueryRange range = QueryRanges.parseFrom(request, 20);
        Page<User> entities = userBo.readAll(filter, range, order);
        response.setCollectionResponse(range, entities.getList().size(), entities.getRowCount());

        addTokenBinder();

        return entities.getList();
    }

    private void addTokenBinder() {
        // Bind the resources in the collection with link URL tokens, etc. here...
        HyperExpress.tokenBinder(new TokenBinder<User>() {
            @Override
            public void bind(User entity, TokenResolver resolver) {
                resolver.bind(Constants.Url.USER_UUID, entity.getUuid())
                        .bind(Constants.Url.USER_ID, entity.getId().toString());
            }
        });
    }

    public void update(Request request, Response response) {
        //TODO: Your 'PUT' logic here...
        response.setResponseNoContent();
    }

    public void delete(Request request, Response response) {
        String uuid = request.getHeader(Constants.Url.USER_UUID, "No User UUID supplied");
        userBo.deleteUser(uuid);
        response.setResponseNoContent();
    }
}
