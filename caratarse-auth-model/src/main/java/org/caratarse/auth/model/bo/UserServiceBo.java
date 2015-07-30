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
import org.caratarse.auth.model.dao.UserServiceDao;
import org.caratarse.auth.model.po.Service;
import org.caratarse.auth.model.po.User;
import org.caratarse.auth.model.po.UserService;
import org.caratarse.auth.model.util.Constants;
import org.caratarse.auth.model.util.CriteriaFilterHelper;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.lambico.dao.generic.Page;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import org.restexpress.common.query.QueryFilter;
import org.restexpress.common.query.QueryOrder;
import org.restexpress.common.query.QueryRange;
import org.springframework.transaction.annotation.Transactional;

/**
 * Method managing user services.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@org.springframework.stereotype.Service
public class UserServiceBo {
    @Resource
    private UserServiceDao userServiceDao;
    @Resource
    private UserBo userBo;
    @Resource
    private ServiceBo serviceBo;
    
    @Transactional
    public List<UserService> findServicesByUser(String userUuid) {
        User user = userBo.getUser(userUuid);
        List<UserService> result = user.getUserServices();
        result.size();
        return result;
    }

    @Transactional
    public UserService addServiceToUser(String userUuid, String serviceName) {
        User user = userBo.getUser(userUuid);
        Service service = serviceBo.getService(serviceName);
        return user.addService(service);
    }
    
    @Transactional
    public Page<UserService> readAllByUser(String userUuid, QueryFilter filter, QueryRange range, QueryOrder order) {
        Page<UserService> result = null;
        DetachedCriteria crit = DetachedCriteria.forClass(UserService.class);
        crit.createAlias("user", "user");
        crit.add(Restrictions.eq("user.uuid", userUuid));
        CriteriaFilterHelper.addQueryFilter(crit, filter);
        CriteriaFilterHelper.addQueryOrder(crit, order);
        ((HibernateGenericDao)userServiceDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        if (range != null && range.isInitialized()) {
            result = ((HibernateGenericDao)userServiceDao).searchPaginatedByCriteria(crit, (int) range.getOffset(), range.getLimit());
        } else {
            result = ((HibernateGenericDao)userServiceDao).searchPaginatedByCriteria(crit, 0, 0);
        }
        ((HibernateGenericDao)userServiceDao).setFilterNames();
        return result;
    }

    @Transactional
    public void delete(String userUuid, String serviceName) {
        ((HibernateGenericDao)userServiceDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        UserService userService
                = userServiceDao.findByUserUuidAndServiceName(userUuid, serviceName);
        userService.delete();
        ((HibernateGenericDao)userServiceDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
    }
    
}
