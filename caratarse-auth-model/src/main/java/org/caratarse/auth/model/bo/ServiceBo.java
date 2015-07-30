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
import org.caratarse.auth.model.dao.ServiceDao;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import org.caratarse.auth.model.po.Service;
import org.caratarse.auth.model.util.Constants;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@org.springframework.stereotype.Service
public class ServiceBo {
    @Resource
    private ServiceDao serviceDao;
    
    @Transactional
    public Service getService(String serviceName) {
        ((HibernateGenericDao)serviceDao).setFilterNames(Constants.NOT_DELETED_FILTER_NAME);
        final Service service = serviceDao.findByName(serviceName);
        ((HibernateGenericDao)serviceDao).setFilterNames();
        return service;        
    }
    
}
