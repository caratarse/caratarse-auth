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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

/**
 * The user.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Filters({@Filter(name = "limitByNotDeleted")})
@NamedQueries({
    @NamedQuery(name = "User.findAllActive", query = "from User where disabled is null")
})
@Entity
public class User extends EntityBase implements UuidIdentified {

    private String uuid;
    private String username;
    private String password;
    private Date lastLogin;
    private String lastIp;
    private Date creationDate;
    private Date updatedDate;
    private Date disabled;
    @JsonManagedReference(value = "user-service")
    private List<UserService> userServices;
    @JsonManagedReference(value = "user-authorization")
    private List<UserAuthorization> userAuthorizations;

    public User() {
        this.uuid = UUID.randomUUID().toString();
    }

    public User(String username, String password) {
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
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
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
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

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDisabled() {
        return disabled;
    }

    public void setDisabled(Date disabled) {
        this.disabled = disabled;
    }

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user")
    @Filters({@Filter(name = "limitByNotDeleted")})
    public List<UserService> getUserServices() {
        if (userServices == null) {
            userServices = new LinkedList<UserService>();
        }
        return userServices;
    }

    public void setUserServices(List<UserService> userServices) {
        this.userServices = userServices;
    }

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "user")
    @Filters({@Filter(name = "limitByNotDeleted")})
    public List<UserAuthorization> getUserAuthorizations() {
        if (userAuthorizations == null) {
            userAuthorizations = new LinkedList<UserAuthorization>();
        }
        return userAuthorizations;
    }

    public void setUserAuthorizations(List<UserAuthorization> userAuthorizations) {
        this.userAuthorizations = userAuthorizations;
    }

    @Override
    protected void doRecursiveDelete() {
        super.doRecursiveDelete();
        final List<UserService> services = getUserServices();
        if (services != null && !services.isEmpty()) {
            for (UserService userService : services) {
                userService.delete();
            }
        }
        List<UserAuthorization> userAuthorizations = getUserAuthorizations();
        if (userAuthorizations != null && !userAuthorizations.isEmpty()) {
            for (UserAuthorization userAuthorization : userAuthorizations) {
                userAuthorization.delete();
            }
        }
    }

    public boolean hasAuthorization(Authorization authorization) {
        boolean result = false;
        final List<UserAuthorization> userAuthorizations = getUserAuthorizations();
        if (userAuthorizations != null && !userAuthorizations.isEmpty()) {
            for (UserAuthorization userAuthorization : userAuthorizations) {
                if (authorization.equals(userAuthorization.getAuthorization())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
    public boolean hasService(Service service) {
        boolean result = false;
        List<UserService> userServices = getUserServices();
        if (userServices != null && !userServices.isEmpty()) {
            for (UserService userService : userServices) {
                if (service.equals(userService.getService())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    
    public void addAuthorization(Authorization authorization, Permissions permissions) {
        if (!hasAuthorization(authorization)) {
            if (hasService(authorization.getService())) {
                UserAuthorization userAuthorization
                        = new UserAuthorization(this, authorization, permissions);
                this.getUserAuthorizations().add(userAuthorization);
                authorization.getUserAuthorizations().add(userAuthorization);
            } else {
                throw new IllegalStateException("User doesn't have service");
            }
        }
    }
    
    public void addService(Service service) {
        if (!hasService(service)) {
            UserService userService = new UserService(this, service);
            this.getUserServices().add(userService);
            service.getUserServices().add(userService);
        }
    }
}
