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

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 * The defined permissions.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
@Embeddable
public class Permissions implements Serializable {
    private boolean read;
    private boolean write;
    private boolean execute;

    public static final Permissions R = new Permissions(true, false, false);
    public static final Permissions RW = new Permissions(true, true, false);
    public static final Permissions RWX = new Permissions(true, true, true);
    
    public Permissions() {
    }

    public Permissions(boolean read, boolean write, boolean execute) {
        this.read = read;
        this.write = write;
        this.execute = execute;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public boolean isExecute() {
        return execute;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }
    
}
