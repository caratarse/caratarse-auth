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
package org.caratarse.auth.model.po.attribute;

import com.strategicgains.syntaxe.annotation.StringValidation;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import org.caratarse.auth.model.po.EntityBase;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

/**
 * An attribute for other entities.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Filters({@Filter(name = "limitByNotDeleted")})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Attribute extends EntityBase implements GenericAttribute {

    @StringValidation(minLength = 1)
    private String name;
    private Date creationDate;
    private Date updatedDate;

    @PrePersist
    protected void onCreate() {
        creationDate = updatedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }

    @Column(nullable = false, updatable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @Access(AccessType.FIELD)
    public Date getCreationDate() {
        return creationDate;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Access(AccessType.FIELD)
    public Date getUpdatedDate() {
        return updatedDate;
    }

}
