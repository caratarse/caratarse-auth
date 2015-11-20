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
import javax.persistence.Lob;
import javax.persistence.Transient;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Type;

/**
 * An attribute with value of type long string (over 255 chars).
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Filters({@Filter(name = "limitByNotDeleted")})
@Entity
public class LongStringAttribute extends Attribute {

    private String longStringValue;

    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    public String getLongStringValue() {
        return longStringValue;
    }

    public void setLongStringValue(String longStringValue) {
        this.longStringValue = longStringValue;
    }

    @Transient
    @Override
    public Object getValue() {
        return getLongStringValue();
    }

    public void setValue(Object value) {
        setLongStringValue((String) value);
    }

}
