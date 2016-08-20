<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib uri="/WEB-INF/ExtraTags.tld" prefix="et"%>

<c:if test="${not empty param.locale}">
	<%-- set the locale --%>
	<fmt:setLocale value="${param.locale}" scope="session"/>
	
	<%-- set the user locale in db --%>
	<et:setUserLocale locale="${param.locale}"/>
	
	<%-- load the bundle (by locale) --%>
	<fmt:setBundle basename="resources"/>
	
	<%-- set current locale to session --%>
	<c:set var="currentLocale" value="${param.locale}" scope="session"/>
</c:if>

<%-- goto back to the settings--%>
<c:redirect url="home"/>

