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
<jsp:directive.include file="includes/top.jsp" />

<div id="msg" class="alert alert-block alert-info">
    <h3  class="alert-heading"><spring:message code="screen.logout.header" /></h3>

    <p class="lead"><spring:message code="screen.logout.success" /></p>
    <p class="lead"><spring:message code="screen.logout.security" /></p>
</div>
<jsp:directive.include file="includes/bottom.jsp" />