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
package org.caratarse.auth.model.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A dates and times provider operating following the common sense (for example: today is today).
 *
 * @author Lucio Benfante
 * (<a href="mailto:lucio@benfante.com">lucio@benfante.com</a>)
 */
public class DefaultDateTimeProvider implements DateTimeProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant getCurrentInstant() {
        return Instant.now();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCurrent() {
        return Date.from(Instant.now());
    }
    
}
