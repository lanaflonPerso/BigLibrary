<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib uri="/WEB-INF/ExtraTags.tld" prefix="et"%>

<html>
<c:url var="controllerUrl" value="controller"/>
<c:set var="title" value="Settings" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<table id="main-container">

		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		
		<tr>
			<td class="content">
				<form id="settings_form" action="${controllerUrl}" method="post">
					<c:if test="${profileUpdateIsSuccessful}">
						<div class="alert alert-success">
							<fmt:message key="settings.profileUpdateIsSuccessful"/>
						</div>
					</c:if>
					<input type="hidden" name="command" value="updateProfile" />
					<et:userDetails user="${user}"/>
					<input type="submit" value="<fmt:message key="settings.edit"/>">
					<br/>
				</form>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>

	</table>
</body>
</html>