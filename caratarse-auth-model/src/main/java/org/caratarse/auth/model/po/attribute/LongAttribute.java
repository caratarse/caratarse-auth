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

import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

/**
 * An attribute with value of type long integer.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Filters({@Filter(name = "limitByNotDeleted")})
@Entity
public class LongAttribute extends Attribute {

    private Long longValue;

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    @Transient
    @Override
    public Object getValue() {
        return getLongValue();
    }

    public void setValue(Object value) {
        setLongValue((Long) value);
    }

}
