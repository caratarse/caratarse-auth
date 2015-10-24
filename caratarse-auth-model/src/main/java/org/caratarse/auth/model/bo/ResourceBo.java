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
import org.caratarse.auth.model.dao.ResourceDao;
import org.caratarse.auth.model.dao.ResourceHierarchyLinkDao;
import org.caratarse.auth.model.po.ResourceHierarchyLink;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.lambico.dao.spring.hibernate.HibernateGenericDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Methods for managing resources and their hierarchy.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Service
public class ResourceBo {

    @Resource
    private ResourceDao resourceDao;
    @Resource
    private ResourceHierarchyLinkDao resourceHierarchyLinkDao;

    @Transactional
    public int insertInHierarchy(org.caratarse.auth.model.po.Resource parent,
            org.caratarse.auth.model.po.Resource child) {
        int result = 1;
        ResourceHierarchyLink self = new ResourceHierarchyLink();
        self.setParent(child);
        self.setChild(child);
        self.setDepth(0);
        resourceHierarchyLinkDao.store(self);
        if (parent != null) {
            Session session = ((HibernateGenericDao) resourceHierarchyLinkDao).currentCustomizedSession();
            SQLQuery query
                    = session.createSQLQuery("INSERT INTO resourcehierarchylink(parent_id, child_id, depth)"
                            + " SELECT l.parent_id, " + child.getId()+", l.depth+1"
                            + " FROM resourcehierarchylink l"
                            + " WHERE l.child_id = ?");
            query.setParameter(0, parent.getId());
            result += query.executeUpdate();
        }
        return result;
    }

}
