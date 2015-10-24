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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

/**
 * The resource entity.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Filters({@Filter(name = "limitByNotDeleted")})
@Entity
public class Resource extends EntityBase implements UuidIdentified {

    public static final String GENERIC_RESOURCE_TYPE = "generic";
    private String uuid;
    private String type;
    private String name;
    private String description;
    private Date creationDate;
    private Date updatedDate;
//    @JsonIgnore
//    private List<UserService> userServices;
//    @JsonIgnore
//    private List<Authorization> authorizations;

    public Resource() {
        this.uuid = UUID.randomUUID().toString();
        this.type = GENERIC_RESOURCE_TYPE;
    }
    
    public Resource(String type) {
        this.uuid = UUID.randomUUID().toString();
        this.type = type;
    }

    public Resource(String type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    @PrePersist
    protected void onCreate() {
        creationDate = updatedDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }
    
    @Column(length = 36, nullable = false, unique = true, updatable = false)
    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    @OneToMany(mappedBy = "service")
//    @Filters({@Filter(name = "limitByNotDeleted")})
//    public List<UserService> getUserServices() {
//        if (userServices == null) {
//            userServices = new LinkedList<UserService>();
//        }
//        return userServices;
//    }
//
//    public void setUserServices(List<UserService> userServices) {
//        this.userServices = userServices;
//    }
//
//    @OneToMany(mappedBy = "service")
//    @Filters({@Filter(name = "limitByNotDeleted")})
//    public List<Authorization> getAuthorizations() {
//        if (authorizations == null) {
//            authorizations = new LinkedList<Authorization>();
//        }
//        return authorizations;
//    }
//
//    public void setAuthorizations(List<Authorization> authorizations) {
//        this.authorizations = authorizations;
//    }

    @Override
    protected void doRecursiveDelete() {
        super.doRecursiveDelete();
//        final List<UserService> userServices = getUserServices();
//        if (userServices != null && !userServices.isEmpty()) {
//            for (UserService userService : userServices) {
//                userService.delete();
//            }
//        }
//        List<Authorization> authorizations = getAuthorizations();
//        if (authorizations != null && !authorizations.isEmpty()) {
//            for (Authorization authorization : authorizations) {
//                authorization.delete();
//            }
//        }
    }

}
