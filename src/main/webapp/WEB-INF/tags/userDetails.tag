<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ attribute name="user"
	type="ua.khai.slynko.library.db.entity.User"%>

<table id="settings_user_details" class="table">
	<thead>
		<tr>
			<th class="message" colspan=2><fmt:message
					key="userDetails.profile" /></th>
		</tr>
		<tr>
			<td><fmt:message key="userDetails.firstName" /></td>
			<td>${user.firstName}</td>
		</tr>
	</thead>
	<tr>
		<td><fmt:message key="userDetails.lastName" /></td>
		<td>${user.lastName}</td>
	</tr>
	<tr>
		<td><fmt:message key="userDetails.login" /></td>
		<td>${user.login}</td>
	</tr>
	<tr>
		<td><fmt:message key="userDetails.email" /></td>
		<td>${user.email}</td>
	</tr>
	<tr>
		<td><fmt:message key="userDetails.locale" /></td>
		<td>${user.locale}</td>
	</tr>
</table>