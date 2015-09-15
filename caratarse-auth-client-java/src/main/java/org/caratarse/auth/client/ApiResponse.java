/**
 * Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>
 *
 * This file is part of Caratarse Auth Client Java.
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
package org.caratarse.auth.client;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * The response from the API.
 * 
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class ApiResponse {
    private String code;
    private String description;
    private InputStream content;
    private String asText;

    public ApiResponse(String code, String description, InputStream content) {
        this.code = code;
        this.description = description;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public InputStream getContent() {
        return content;
    }
    
    /**
     * Return the content of the response as text.
     * 
     * @return The content of the response.
     */
    public String asText() throws IOException {
        if (this.asText == null) {
            this.asText = IOUtils.toString(this.getContent());
        }
        return asText;
    }
}
