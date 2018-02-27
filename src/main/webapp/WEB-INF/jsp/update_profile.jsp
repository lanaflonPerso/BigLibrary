<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:url var="contrullerUrl" value="controller" />
<c:set var="title" value="Update profile" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content center">
				<form id="sign_up_form" action="${contrullerUrl}" method=post>
					<input type="hidden" name="command" value="updateProfile" />
					<table>
						<tr>
							<td colspan=2>
								<div class="alert alert-danger">
									<ul>
										<c:if test="${not empty fillInMessage}">
											<li>${fillInMessage}</li>
										</c:if>
										<c:if test="${not empty firstNameMessage}">
											<li>${firstNameMessage}</li>
										</c:if>
										<c:if test="${not empty lastNameMessage}">
											<li>${lastNameMessage}</li>
										</c:if>
										<c:if test="${not empty loginMessage}">
											<li>${loginMessage}</li>
										</c:if>
										<c:if test="${not empty emailMessage}">
											<li>${emailMessage}</li>
										</c:if>
										<c:if test="${not empty passwordMessage}">
											<li>${passwordMessage}</li>
										</c:if>
									</ul>
								</div>
							</td>
						</tr>
						<tr>
							<th colspan=2><fmt:message key="signup.updateProfile" /><br> <sup>*</sup>
								<fmt:message key="signup.requiredFields" />
							</th>
						</tr>
						<tr>
							<td>
								<fieldset>
									<legend>
										<fmt:message key="signup.firstName" /><sup>*</sup>
									</legend>
									<c:if test="${not empty firstName}">
										<input name="firstName" value="${firstName}" />
									</c:if>
									<c:if test="${empty firstName}">
										<input name="firstName" value="${user.firstName}" />
									</c:if>
								</fieldset>
							</td>
							<td>
								<fieldset>
									<legend>
										<fmt:message key="signup.lastName" /><sup>*</sup>
									</legend>
									<c:if test="${not empty lastName}">
										<input name="lastName" value="${lastName}" />
									</c:if>
									<c:if test="${empty lastName}">
										<input name="lastName" value="${user.lastName}" />
									</c:if>
									<br/>
								</fieldset>
							</td>
						</tr>
						<tr>
							<td>
								<fieldset>
									<legend>
										<fmt:message key="signup.login" /><sup>*</sup>
									</legend>
									<c:if test="${not empty login}">
										<input name="login" value="${login}" />
									</c:if>
									<c:if test="${empty login}">
										<input name="login" value="${user.login}" />
									</c:if>
									<br/>
								</fieldset>
							</td>
							<td>
								<fieldset>
									<legend>
										<fmt:message key="signup.email" /><sup>*</sup>
									</legend>
									<c:if test="${not empty email}">
										<input name="email" value="${email}" />
									</c:if>
									<c:if test="${empty email}">
										<input name="email" value="${user.email}" />
									</c:if>
									<br/>
								</fieldset>
							</td>
						</tr>
						<tr>
							<td>
								<fieldset>
									<legend>
										<fmt:message key="signup.newPassword" />
									</legend>
									<input name="newPassword1" type="password" /><br />
								</fieldset>
							</td>
							<td>
								<fieldset>
									<legend>
										<fmt:message key="signup.confirmNewPassword" />
									</legend>
									<input name="newPassword2" type="password" /><br />
								</fieldset>
							</td>
						</tr>
						<tr>
							<td align=center colspan=2><input type="submit"
								value="<fmt:message key="signup.submit" />"></td>
						</tr>
					</table>

				</form>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>