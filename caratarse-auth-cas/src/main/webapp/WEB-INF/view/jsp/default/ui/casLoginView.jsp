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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />

<div style="padding: 75px;">
    <div class="modal" style="position: relative; top: auto; left: auto; margin: 0 auto; z-index: 1">
        <div class="modal-body">
            <form:form cssClass="form-horizontal" method="post" id="fm1" commandName="${commandName}" htmlEscape="true">
                <fieldset>
                    <legend><spring:message code="application.title"/></legend>

                    <form:errors path="*">
                        <div class="alert alert-error">
                            <c:forEach items="${messages}" var="message">
                                <c:choose>
                                    <c:when test="${message=='error.authentication.credentials.bad.usernameorpassword.username'}">
                                        <div><spring:message code="error.authentication.credentials.bad.username"/></div>
                                    </c:when>
                                    <c:when test="${message=='error.authentication.credentials.blocked'}">
                                        <div><spring:message code="error.authentication.credentials.blocked"/></div>
                                    </c:when>
                                    <c:when test="${message=='error.authentication.credentials.bad.usernameorpassword.password'}">
                                        <div><spring:message code="error.authentication.credentials.bad.password"/></div>
                                    </c:when>
                                    <c:otherwise>    
                                        <div>${message}</div>
                                    </c:otherwise>
                                </c:choose>    
                            </c:forEach>
                        </div>
                    </form:errors>

                    <div class="control-group">
                        <label class="control-label" for="username"><spring:message code="credential.email" /></label>
                        <div class="controls">
                            <c:if test="${not empty sessionScope.openIdLocalId}">
                                <strong>${sessionScope.openIdLocalId}</strong>
                                <input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
                            </c:if>

                            <c:if test="${empty sessionScope.openIdLocalId}">
                                <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                                <form:input cssClass="input-xlarge" id="username" size="30" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" autofocus="autofocus"/>
                            </c:if>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="password"><spring:message code="credential.password" /></label>
                        <div class="controls">
                            <spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
                            <form:password cssClass="input-xlarge" id="password" size="30" tabindex="2" accesskey="${passwordAccessKey}" path="password" autocomplete="off" htmlEscape="true" />
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="controls">
                            <label class="checkbox">
                                <input id="warn" name="warn" value="true" tabindex="3" accesskey="<spring:message code='screen.welcome.label.warn.accesskey' />" type="checkbox" />
                                <spring:message code="screen.welcome.label.warn" />
                            </label>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="controls">
                            <input type="hidden" name="lt" value="${flowExecutionKey}" />
                            <input type="hidden" name="_eventId" value="submit" />
                            <input class="btn btn-primary" name="submit" accesskey="l" value="<spring:message code="screen.welcome.button.login" />" tabindex="4" type="submit" />
                        </div>
                    </div>
                </fieldset>
            </form:form>
        </div>
        <div class="modal-footer" style="text-align: center;">
            <p><spring:message code="screen.footer.label.part.one" /><spring:message code="screen.footer.label.part.two" /></a></p>
        </div>
    </div>
</div>
<jsp:directive.include file="includes/bottom.jsp" />
