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
<!DOCTYPE html>
<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<spring:theme code="mobile.custom.css.file" var="mobileCss" text="" />
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>Caratarse Auth - CAS</title>
        <meta name="description" content="CAS Service for Caratarse Auth"/>
        <meta name="author" content="Caratarse Auth Team">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <link href="assets/css/vendor/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/css/vendor/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
        <link href="assets/css/base.css" rel="stylesheet" type="text/css"/>
        <link href="assets/css/styles.css" rel="stylesheet" type="text/css"/>
        <script src="assets/js/vendor/modernizr-2.6.2.min.js"></script>
    </head>
    <body>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
        <div class="container-fluid">
            <div class="navbar navbar-inverse navbar-fixed-top">
                <div class="navbar-inner" style="<spring:message code="headerStyles" text=""/>">
                    <div class="container-fluid">
                        <a class="brand" href="javascript:void();">Caratarse Auth CAS</a>
                    </div>
                </div>
            </div>  
        </div>

        <div class="container-fluid">
            <div class="content">
