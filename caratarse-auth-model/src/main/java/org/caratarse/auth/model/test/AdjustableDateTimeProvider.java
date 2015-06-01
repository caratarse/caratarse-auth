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
package org.caratarse.auth.model.test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.caratarse.auth.model.util.DateTimeProvider;

/**
 * A dates and times provider that can be manipulated, for example fixing the day that must be considered as today.
 *
 * @author Lucio Benfante
 * (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class AdjustableDateTimeProvider implements DateTimeProvider {

    private Instant now;

    private Instant getNow() {
        if (now != null) {
            return now;
        } else {
            return Instant.now();
        }
    }

    public void setNow(Instant now) {
        this.now = now;
    }

    public void setNow(LocalDate now) {
        this.now = now.atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    public void setNow(LocalDateTime now) {
        this.now = now.toInstant(ZoneOffset.UTC);
    }
    
    public void setNow(Date now) {
        if (now == null) {
            this.now = null;
        } else {
            this.now = now.toInstant();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getCurrentDate() {
        return getNow().atOffset(ZoneOffset.UTC).toLocalDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getCurrentDateTime() {
        return getNow().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getCurrentInstant() {
        return getNow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCurrent() {
        return Date.from(getNow());
    }
    
}
