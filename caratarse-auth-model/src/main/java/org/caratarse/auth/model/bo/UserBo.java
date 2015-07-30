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

import java.util.List;
import javax.annotation.Resource;
import org.caratarse.auth.model.dao.AuthorizationDao;
import org.caratarse.auth.model.dao.ServiceDao;
import org.caratarse.auth.model.dao.UserAuthorizationDao;
import org.caratarse.auth.model.dao.UserDao;
import org.caratarse.auth.model.dao.UserServiceDao;
import org.caratarse.auth.model.po.Authorization;
import org.caratarse.auth.model.po.Permissions;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserAuthorization;
import org.caratarse.auth.model.util.Constants;
import org.caratarse.auth.model.util.CriteriaFilterHelper;
import org.hibernate.criterion.DetachedCriteria;
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
public class UserBo {
    @Resource
    private UserDao userDao;
    @Resource
    private UserAuthorizationDao userAuthorizationDao;
    @Resource
    private ServiceDao serviceDao;
    @Resource
    private AuthorizationDao authorizationDao;
    @Resource
    private UserServiceDao userServiceDao;
    
    @Transactional
    public List<UserAuthorization> retrieveUserDirectAuthorizations(String uuid, String serviceName) {
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        List<UserAuthorization> result = userAuthorizationDao.findByUserUuidAndServiceName(uuid, serviceName);
        ((HibernateGenericDao)userAuthorizationDao).setFilterNames();
        return result;
    }
    
    @Transactional
    public Page<User> readAll(QueryFilter filter, QueryRange range, QueryOrder order) {
        Page<User> result = null;
        DetachedCriteria crit = DetachedCriteria.forClass(User.class);
        CriteriaFilterHelper.addQueryFilter(crit, filter);
        CriteriaFilterHelper.addQueryOrder(crit, order);
        ((HibernateGenericDao)userDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        if (range != null && range.isInitialized()) {
            result = ((HibernateGenericDao)userDao).searchPaginatedByCriteria(crit, (int) range.getOffset(), range.getLimit());
        } else {
            result = ((HibernateGenericDao)userDao).searchPaginatedByCriteria(crit, 0, 0);
        }
        ((HibernateGenericDao)userDao).setFilterNames();
        return result;
    }

    @Transactional
    public User getUser(String uuid) {
        ((HibernateGenericDao)userDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        final User user = userDao.findByUuid(uuid);
        ((HibernateGenericDao)userDao).setFilterNames();
        return user;
    }
    
    @Transactional
    public List<User> populate() {
        final User userLucio = new User("lucio", "lucioPwd");
        userLucio.setUuid("a1ab82a6-c8ce-4723-8532-777c4b05d03c");
        userDao.create(userLucio);
        final User userMichele = new User("michele", "michelePwd");
        userDao.create(userMichele);
        final org.caratarse.auth.model.po.Service serviceTest
                = new org.caratarse.auth.model.po.Service("TEST_SERVICE", "A service for tests");
        serviceDao.create(serviceTest);
        final org.caratarse.auth.model.po.Service serviceAnother
                = new org.caratarse.auth.model.po.Service("ANOTHER_SERVICE", "Another service for tests");
        serviceDao.create(serviceAnother);
        final org.caratarse.auth.model.po.Service serviceUnused
                = new org.caratarse.auth.model.po.Service("UNUSED_SERVICE", "A not used service for tests");
        serviceDao.create(serviceUnused);
        userLucio.addService(serviceTest);
        userLucio.addService(serviceAnother);
        userMichele.addService(serviceTest);
        Authorization authorizationAdmin
                = new Authorization("ROLE_ADMIN", "Admin authorization", serviceTest);
        authorizationDao.create(authorizationAdmin);
        userLucio.addAuthorization(authorizationAdmin, Permissions.RWX);
        Authorization authorizationUser
                = new Authorization("ROLE_USER", "User authorization", serviceTest);
        authorizationDao.create(authorizationUser);
        userMichele.addAuthorization(authorizationUser, Permissions.RWX);
        Authorization authorizationPrinter
                = new Authorization("ROLE_PRINTER", "Printer authorization", serviceTest);
        authorizationDao.create(authorizationPrinter);
        userMichele.addAuthorization(authorizationPrinter, Permissions.R);
        userLucio.addAuthorization(authorizationPrinter, Permissions.RWX);
        Authorization authorizationNotUsed
                = new Authorization("ROLE_NOT_USED", "Not used authorization", serviceTest);
        authorizationDao.create(authorizationNotUsed);
        return userDao.findAll();
    }

    @Transactional
    public void deleteUser(String uuid) {
        ((HibernateGenericDao)userDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        User user = userDao.findByUuid(uuid);
        user.delete();
        ((HibernateGenericDao)userDao).setFilterNames();
    }

    @Transactional
    public void store(User user) {
        userDao.store(user);
    }

    @Transactional
    public void cleanAll() {
        userAuthorizationDao.deleteAll();
        userServiceDao.deleteAll();
        authorizationDao.deleteAll();
        serviceDao.deleteAll();
        userDao.deleteAll();
    }
    
}
