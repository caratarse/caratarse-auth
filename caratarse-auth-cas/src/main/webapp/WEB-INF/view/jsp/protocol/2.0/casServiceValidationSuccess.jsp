<%--

    Copyright (C) 2015 Caratarse Auth Team <lucio.benfante@gmail.com>

    This file is part of Caratarse Auth CAS.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@ page session="false" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>
    <cas:authenticationSuccess>
        <cas:user>${fn:escapeXml(assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.id)}</cas:user>
        <cas:attributes>
            <cas:attributes>
                <c:forEach var="auth" items="${assertion.chainedAuthentications}">
                    <c:forEach var="attr" items="${auth.principal.attributes}">
                        <cas:${fn:escapeXml(attr.key)}>${fn:escapeXml(attr.value)}</cas:${fn:escapeXml(attr.key)}>
                        </c:forEach>
                    </c:forEach>
            </cas:attributes>
        </cas:attributes>
        <c:if test="${not empty pgtIou}">
            <cas:proxyGrantingTicket>${pgtIou}</cas:proxyGrantingTicket>
            </c:if>
            <c:if test="${fn:length(assertion.chainedAuthentications) > 1}">
            <cas:proxies>
                <c:forEach var="proxy" items="${assertion.chainedAuthentications}" varStatus="loopStatus" begin="0" end="${fn:length(assertion.chainedAuthentications)-2}" step="1">
                    <cas:proxy>${fn:escapeXml(proxy.principal.id)}</cas:proxy>
                    </c:forEach>
            </cas:proxies>
        </c:if>
        <%-- CAS attributes -- BEGIN -- --%>
        <cas:attributes>
            <c:if
                test="${fn:length(assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.attributes) >= 1}">
                <c:forEach var="attr"
                           items="${assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.attributes}"
                           varStatus="loopStatus" begin="0"
                           end="${fn:length(assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.attributes)-1}"
                           step="1">
                    <cas:${fn:escapeXml(attr.key)}>${fn:escapeXml(attr.value)}</cas:${fn:escapeXml(attr.key)}>
                    </c:forEach>
                </c:if>
        </cas:attributes>
        <%-- CAS attributes -- END -- --%>
    </cas:authenticationSuccess>
</cas:serviceResponse>