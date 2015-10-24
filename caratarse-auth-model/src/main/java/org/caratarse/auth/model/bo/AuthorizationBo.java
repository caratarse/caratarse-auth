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
import org.caratarse.auth.model.dao.AuthorizationDao;
import org.caratarse.auth.model.po.Authorization;
import org.caratarse.auth.model.util.Constants;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Service
public class AuthorizationBo {
    @Resource
    private AuthorizationDao authorizationDao;
    
    @Transactional
    public Authorization findAuthorization(String authorizationName) {
        ((HibernateGenericDao)authorizationDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        Authorization result
                = authorizationDao.findByName(authorizationName);
        ((HibernateGenericDao)authorizationDao).setFilterNames();
        return result;
    }
    
}
