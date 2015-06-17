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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.restexpress.common.query.FilterCallback;
import org.restexpress.common.query.FilterComponent;
import org.restexpress.common.query.OrderCallback;
import org.restexpress.common.query.OrderComponent;
import org.restexpress.common.query.QueryFilter;
import org.restexpress.common.query.QueryOrder;

/**
 * Helper methods for translating rest filters to Criteria.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class CriteriaFilterHelper {

    public static void addQueryFilter(final DetachedCriteria crit, QueryFilter filter) {
        if (filter == null) {
            return;
        }

        filter.iterate(new FilterCallback() {
            @Override
            public void filterOn(FilterComponent c) {
                switch (c.getOperator()) {
                    case CONTAINS:		// String-related
                        crit.add(Restrictions.ilike(c.getField(), c.getValue().toString(),
                                MatchMode.ANYWHERE));
                        break;
                    case STARTS_WITH:	// String-related
                        crit.add(Restrictions.ilike(c.getField(), c.getValue().toString(),
                                MatchMode.START));
                        break;
                    case GREATER_THAN:
                        crit.add(Restrictions.gt(c.getField(), c.getValue()));
                        break;
                    case GREATER_THAN_OR_EQUAL_TO:
                        crit.add(Restrictions.ge(c.getField(), c.getValue()));
                        break;
                    case LESS_THAN:
                        crit.add(Restrictions.lt(c.getField(), c.getValue()));
                        break;
                    case LESS_THAN_OR_EQUAL_TO:
                        crit.add(Restrictions.le(c.getField(), c.getValue()));
                        break;
                    case NOT_EQUALS:
                        crit.add(Restrictions.ne(c.getField(), c.getValue()));
                        break;
                    case EQUALS:
                    default:
                        crit.add(Restrictions.eq(c.getField(), c.getValue()));
                        break;
                }
            }
        });
    }

    public static void addQueryOrder(final DetachedCriteria crit, QueryOrder order) {
        if (order == null) {
            return;
        }

        if (order.isSorted()) {
            order.iterate(new OrderCallback() {

                @Override
                public void orderBy(OrderComponent component) {
                    if (component.isAscending()) {
                        crit.addOrder(Order.asc(component.getFieldName()));
                    } else {
                        crit.addOrder(Order.desc(component.getFieldName()));
                    }
                }
            });
        }
    }

}
