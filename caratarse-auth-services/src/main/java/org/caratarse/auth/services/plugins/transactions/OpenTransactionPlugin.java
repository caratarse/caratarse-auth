/**
 * Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>
 *
 * This file is part of Caratarse Auth Services.
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
package org.caratarse.auth.services.plugins.transactions;

import javax.annotation.Resource;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.RestExpress;
import org.restexpress.pipeline.MessageObserver;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.plugin.Plugin;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class OpenTransactionPlugin extends MessageObserver
        implements Plugin, Preprocessor, Postprocessor {

    private boolean isRegistered = false;
    private ThreadLocal<TransactionStatus> transaction = new ThreadLocal<>();
    @Resource
    private PlatformTransactionManager transactionManager;
    
    @Override
    public Plugin register(RestExpress server) {
        if (isRegistered) {
            return this;
        }

        server.registerPlugin(this);
        this.isRegistered = true;

        server
                .addMessageObserver(this)
                .addPreprocessor(this)
                .addFinallyProcessor(this);

        return this;
    }

    @Override
    public void bind(RestExpress server) {
    }

    @Override
    public void shutdown(RestExpress server) {
    }

    @Override
    public void process(Request request) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        transaction.set(status);
    }

    @Override
    public void process(Request request, Response response) {
        TransactionStatus status = transaction.get();
        if (!status.isRollbackOnly()) {
            if (!status.isCompleted()) {
                transactionManager.commit(status);
            }
        } else {
            if (!status.isCompleted()) {
                transactionManager.rollback(status);
            }
        }
    }

}
