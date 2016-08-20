<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<html>

<c:url var="loginURL" value="login"/>
<c:url var="controllerURL" value="controller"/>
<c:set var="title" value="Login" />

<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content center">
				<c:if test="${signUpSuccessful}">
					<span class="message">
						<fmt:message key="login.registrationIsSuccessful" />
					</span>
				</c:if>
				<form id="login_form" action="${controllerURL}" method="post">
					<input type="hidden" name="command" value="forgotPassword" />
					<fieldset>
						<legend>
							<fmt:message key="forgotPassword.loginOrEmail" />
						</legend>
						<select size="1" name="forgotPassData">
								<option value="login">
									<fmt:message key="forgotPassword.login"/>
								</option>
								<option value="email">
									<fmt:message key="forgotPassword.email"/>
								</option>
							</select>
						<input name="loginOrEmail" /><br />
					</fieldset>
					<br />
					<input type="submit" value="<fmt:message key="forgotPassword.submit" />">
				</form>
				<form action="${loginURL}">
					<input type="submit" value="<fmt:message key="forgotPassword.back" />">
				</form> 
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>