<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<html>

<c:url var="contrullerUrl" value="controller" />
<c:url var="forgotPasswordURL" value="forgotPassword.jsp"/>
<fmt:message key="login.logIn" var="titleLogin"/>
<c:set var="title" value="${titleLogin}" />

<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content center">
			<div id="login_container" >
				<c:if test="${signUpSuccessful}">
					<div class="alert alert-success">
						<fmt:message key="login.registrationIsSuccessful" />
					</div>
				</c:if>
				<c:if test="${passwordRestorationIsSuccessful}">
					<div class="alert alert-success">
						<fmt:message key="login.passwordRestorationIsSuccessful" />
					</div>
				</c:if>
				<c:if test="${passwordRestorationIsSuccessful eq false}">
					<div class="alert alert-danger">
						<fmt:message key="login.passwordRestorationIsNotSuccessful" />
					</div>
				</c:if>
				<c:if test="${passwordRestorationUserIsNotFound}">
					<div class="alert alert-danger">
						<fmt:message key="login.passwordRestorationUserIsNotFound" />
					</div>
				</c:if>
				<form id="login_form" action="controller" method="post">
					<input type="hidden" name="command" value="login" />
					<fieldset>
						<legend>
							<fmt:message key="login.login" />
						</legend>
						<input name="login" /><br />
					</fieldset>
					<br />
					<fieldset>
						<legend>
							<fmt:message key="login.password" />
						</legend>
						<input type="password" name="password" />
					</fieldset>
					<br /> 
					<input value="true" name="rememberMe" type="checkbox">
					<label for="rememberMe"><fmt:message key="login.rememberMe" /></label>
					<br/>
					<input type="submit" value="<fmt:message key="login.logIn" />">
					<a href="${forgotPasswordURL}" id="forgotPassword">
						<fmt:message key="login.forgotPassword"/>
					</a>
				</form>
				</div>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>