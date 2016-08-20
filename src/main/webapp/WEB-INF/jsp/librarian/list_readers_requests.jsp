<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:url var="controllerUrl" value="controller" />
<c:set var="title" value="Readers' requests" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<div id="catalog_list_div">
					<form id="confirm_request" action="${controllerUrl}">
						<c:if test="${requestIsConfirmedSuccessfully}">
							<div class="alert alert-success">
								<fmt:message key="listReadersRequests.requestIsConfirmedSuccessfully"/>
							</div>
						</c:if>
						<c:if test="${requestIsDeletedSuccessfully}">
							<div class="alert alert-success">
								<fmt:message key="listReadersRequests.requestIsDeletedSuccessfully"/>
							</div>
						</c:if>
						<input type="hidden" name="command" value="listReadersRequests" />
						<table id="list_catalog_table" class="table">
							<thead>
								<tr>
									<th class="message" colspan=6>
										<fmt:message key="listReadersRequests.requests"/>
									</th>
								</tr>
								<tr>
									<td>№</td>
									<td>
										<fmt:message key="listReadersRequests.title"/>
									</td>
									<td>
										<fmt:message key="listReadersRequests.author"/>
									</td>
									<td>
										<fmt:message key="listReadersRequests.firstName"/>
									</td>
									<td>
										<fmt:message key="listReadersRequests.lastName"/>
									</td>
									<td></td>
								</tr>
							</thead>
							<c:if test="${not noMatchesFound}">
								<c:set var="k" value="0" />
								<c:forEach var="bean" items="${catalogItemRequestsList}">
									<c:set var="k" value="${k+1}" />
									<tr>
										<td><c:out value="${k}" /></td>
										<td>${bean.title}</td>
										<td>${bean.author}</td>
										<td>${bean.firstName}</td>
										<td>${bean.lastName}</td>
										<c:if test="${not empty user}">
											<td><input type="radio" name="beanId" value="${bean.id}"></td>
										</c:if>
									</tr>
								</c:forEach>
							</c:if>
							<c:if test="${noMatchesFound}">
								<tr>
									<td colspan=6>
										<span class="message">
											<fmt:message key="listReadersRequests.noMatches"/>
										</span>
									</td>
								</tr>
							</c:if>
						</table>
						<br /> 
						<input value="<fmt:message key="listReadersRequests.openRequest"/>" type="submit"/>
					</form>
				</div>
			</td>
		</tr>

		<%@ include file="/WEB-INF/jspf/footer.jspf"%>

	</table>
</body>