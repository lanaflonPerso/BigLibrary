<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:url var="controllerUrl" value="controller" />
<c:set var="title" value="Personal area" scope="page" />
<fmt:message key="areYouSureMessage" var="areYouSureMessage"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<table id="main-container">

		<%@ include file="/WEB-INF/jspf/header.jspf"%>

		<tr>
			<td class="content">
				<div id="catalog_list_div">
					<form id="confirm_request" action="${controllerUrl}" method="post">
						<input type="hidden" name="command" value="blockUnblockUser" /> 
						<input type="hidden" name="userId" value="${user.id}" />
						<table id="modify_book_table" class="table">
						<thead>
							<tr>
								<th class="message" colspan=2>
									<fmt:message key="userDetails.readerInfo"/>
								</th>
							</tr>
							<tr>
								<td>
									<fmt:message key="userDetails.firstName"/>
								</td>
								<td>${user.firstName}</td>
							</tr>
						</thead>
							<tr>
								<td>
									<fmt:message key="userDetails.lastName"/>
								</td>
								<td>${user.lastName}</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="userDetails.login"/>
								</td>
								<td>${user.login}</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="userDetails.email"/>
								</td>
								<td>${user.email}</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="userDetails.status"/>
								</td>
								<c:if test="${!userIdBlocked}">
									<td>
										<fmt:message key="userDetails.unblocked"/>
									</td>
								</c:if>
								<c:if test="${userIdBlocked}">
									<td>
										<span class="message">
											<fmt:message key="userDetails.blocked"/>
										</span>
									</td>
								</c:if>
							</tr>
						</table>
						<br />
						<c:if test="${!userIdBlocked}">
							<input value="<fmt:message key="userDetails.blockReader"/>" type="submit"
								onclick="return validate('${areYouSureMessage}');" />
						</c:if>
						<c:if test="${userIdBlocked}">
							<input value="<fmt:message key="userDetails.unblockReader"/>" type="submit"
								onclick="return validate('${areYouSureMessage}');" />
						</c:if>
					</form>
				</div> 
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>