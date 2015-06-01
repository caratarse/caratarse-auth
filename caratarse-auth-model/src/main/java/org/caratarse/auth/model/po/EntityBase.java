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
package org.caratarse.auth.model.po;

import java.util.Date;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import org.caratarse.auth.model.util.DateTimeProviderHolder;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;

/**
 * A superclass for Caratarse-auth entities.
 * 
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@FilterDefs({
    @FilterDef(name = "limitByNotDeleted",
            defaultCondition = "deleted is null")
})
@MappedSuperclass
public abstract class EntityBase extends org.lambico.po.hibernate.EntityBase implements LogicallyDeleted, Linkable {

    /**
     * The deleted timestamp for logically deleted objects.
     */
    protected Date deleted;
    
    public EntityBase() {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkDeleted() {
        return deleted != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDeleted() {
        return deleted;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
    
    /**
     * Logically delete this entity.
     */
    public final void delete() {
        this.deleted = DateTimeProviderHolder.getDateTimeProvider().getCurrent();
        doRecursiveDelete();
    }

    /**
     * Do logical deletion on the relationships of this object.
     * 
     * The default implementation of this object is doing nothing. It should be overridden if
     * necessary.
     */
    protected void doRecursiveDelete() {}
}
