/**
 * Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>
 *
 * This file is part of Caratarse Auth Model.
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
package org.caratarse.auth.model.bo;

import javax.annotation.Resource;
import org.caratarse.auth.model.dao.UserAuthorizationDao;
import org.caratarse.auth.model.po.Authorization;
import org.caratarse.auth.model.po.Permissions;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserAuthorization;
import org.caratarse.auth.model.util.Constants;
import org.caratarse.auth.model.util.CriteriaFilterHelper;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.lambico.dao.generic.Page;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import org.restexpress.common.query.QueryFilter;
import org.restexpress.common.query.QueryOrder;
import org.restexpress.common.query.QueryRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Service
public class UserAuthorizationBo {
    @Resource
    private UserAuthorizationDao userAuthorizationDao;
    @Resource
    private UserBo userBo;
    @Resource
    private AuthorizationBo authorizationBo;

    @Transactional
    public UserAuthorization findUserAuthorization(String userUuid, String authorizationName) {
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        final UserAuthorization result
                = userAuthorizationDao.findByUserUuidAndAuthorizationName(userUuid, authorizationName);
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames();
        return result;
    }

    @Transactional
    public UserAuthorization addAuthorizationToUser(String userUuid, String authorizationName, Permissions permissions) {
        User user = userBo.getUser(userUuid);
        Authorization authorization
                = authorizationBo.findAuthorization(authorizationName);
        return user.addAuthorization(authorization, permissions);
    }
    
    @Transactional
    public Page<UserAuthorization> readAllByUserAndService(String userUuid, QueryFilter filter, QueryRange range, QueryOrder order) {
        Page<UserAuthorization> result = null;
        DetachedCriteria crit = DetachedCriteria.forClass(UserAuthorization.class, "ua");
        crit.createAlias("ua.user", "user");
        crit.add(Restrictions.eq("user.uuid", userUuid));
        crit.createAlias("ua.authorization", "authorization");
        
        CriteriaFilterHelper.addQueryFilter(crit, filter);
        CriteriaFilterHelper.addQueryOrder(crit, order);
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        if (range != null && range.isInitialized()) {
            result = ((HibernateGenericDao)userAuthorizationDao).searchPaginatedByCriteria(crit, (int) range.getOffset(), range.getLimit());
        } else {
            result = ((HibernateGenericDao)userAuthorizationDao).searchPaginatedByCriteria(crit, 0, 0);
        }
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames();
        return result;
    }

    @Transactional
    public void delete(String userUuid, String authorizationName) {
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        UserAuthorization userAuthorization
                = userAuthorizationDao.findByUserUuidAndAuthorizationName(userUuid, authorizationName);
        userAuthorization.delete();
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
    }

    @Transactional
    public void store(UserAuthorization userAuthorization) {
        userAuthorizationDao.store(userAuthorization);
    }
    
}
