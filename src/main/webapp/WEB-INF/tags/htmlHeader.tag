<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <spring:url value="/resources/images/favicon.png" var="favicon"/>
    <link rel="shortcut icon" type="image/x-icon" href="${favicon}">
    
    <title>PetClinic :: a Spring Framework demonstration</title>
    
    <spring:url value="/resources/css/petclinic.css" var="petclinicCss"/>
    <link rel="stylesheet" href="${petclinicCss}"/>
    
    <!-- Only datepicker is used -->
    <spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.min.css" var="jQueryUiCss"/>
    <link href="${jQueryUiCss}" rel="stylesheet"/>
    <spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.theme.min.css" var="jQueryUiThemeCss"/>
    <link href="${jQueryUiThemeCss}" rel="stylesheet"/>
</head>