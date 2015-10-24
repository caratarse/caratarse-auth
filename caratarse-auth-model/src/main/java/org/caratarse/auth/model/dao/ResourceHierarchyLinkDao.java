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
package org.caratarse.auth.model.dao;

import org.caratarse.auth.model.po.ResourceHierarchyLink;
import org.lambico.dao.generic.Dao;
import org.lambico.dao.generic.GenericDao;

/**
 * Represents Resource DAO.
 *
 * @author Lucio Benfante
 *
 */
@Dao(entity = ResourceHierarchyLink.class)
public interface ResourceHierarchyLinkDao extends GenericDao<ResourceHierarchyLink, Long> {
    
}
