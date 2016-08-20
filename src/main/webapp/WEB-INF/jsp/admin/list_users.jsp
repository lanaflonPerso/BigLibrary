<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>


<c:url var="controllerUrl" value="controller" />

<c:if test="${param.command == 'listLibrarians'}">
	<c:set var="commandValue" value="listLibrarians" />
	<c:set var="title" value="All librarians" scope="page" />
</c:if>

<c:if test="${param.command == 'listReaders'}">
	<c:set var="commandValue" value="listReaders" />
	<c:set var="title" value="All readers" scope="page" />
</c:if>

<fmt:message key="areYouSureMessage" var="areYouSureMessage"/>
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<table id="main-container">

		<%@ include file="/WEB-INF/jspf/header.jspf"%>

		<tr>
			<td class="content">
				<div id="catalog_list_div">
					<form id="find_by" action="${controllerUrl}">
						<fieldset>
							<legend>
								<fmt:message key="catalog.findBy"/>
							</legend>
							<input type="hidden" name="command" value="${commandValue}" />
							<input name="firstName" placeholder="<fmt:message key="listLibrarians.firstName"/>" />
							<input name="lastName" placeholder="<fmt:message key="listLibrarians.lastName"/>" />
							<input value="<fmt:message key="catalog.find"/>" type="submit" />
						</fieldset>
					</form>
					<form id="reset_find_by" action="${controllerUrl}">
						<input type="hidden" name="command" value="${commandValue}" />
						<input type="hidden" name="firstName" /> <input type="hidden" name="lastName" />
						<input type="submit" value="<fmt:message key="catalog.reset"/>">
					</form>
					<form id="make_order" action="${controllerUrl}">
						<input type="hidden" name="command" value="${commandValue}" />
						<c:if test="${librarianAddIsSuccessful}">
							<div class="alert alert-success">
								<fmt:message key="adminCatalog.librarianAddIsSuccessful"/>
							</div>
						</c:if>
						<c:if test="${librarianDeleteIsSuccessful}">
							<div class="alert alert-success">
								<fmt:message key="adminCatalog.librarianDeleteIsSuccessful"/>
							</div>
						</c:if>
						<c:if test="${readerIsUnblockedSuccessfully}">
							<div class="alert alert-success">
								<fmt:message key="listReaders.readerIsUnblockedSuccessfully"/>
							</div>
						</c:if>
						<c:if test="${readerIsBlockedSuccessfully}">
							<div class="alert alert-success">
								<fmt:message key="listReaders.readerIsBlockedSuccessfully"/>
							</div>
						</c:if>
						<table id="list_catalog_table" class="table">
							<thead>
								<tr>
									<c:if test="${param.command == 'listLibrarians'}">
										<th class="message" colspan=7>
											<fmt:message key="listLibrarians.allLibrarians"/>
										</th>
									</c:if>
									<c:if test="${param.command == 'listReaders'}">
										<th class="message" colspan=7>
											<fmt:message key="listLibrarians.allReaders"/>
										</th>
									</c:if>
								</tr>
								<tr>
									<td>№</td>
									<td>
										<fmt:message key="listLibrarians.firstName"/>
									</td>
									<td>
										<fmt:message key="listLibrarians.lastName"/>
									</td>
									<td>
										<fmt:message key="listLibrarians.login"/>
									</td>
									<td>
										<fmt:message key="listLibrarians.email"/>
									</td>
									<td>
										<fmt:message key="booksHasTaken" />
									</td>
									<td>
									</td>
								</tr>
							</thead>
							<c:if test="${not noMatchesFound}">
								<c:set var="k" value="0" />
								<c:forEach var="user" items="${listUsers}">
									<c:set var="k" value="${k+1}" />
									<tr>
										<td><c:out value="${k}" /></td>
										<td>${user.firstName}</td>
										<td>${user.lastName}</td>
										<td>${user.login}</td>
										<td>${user.email}</td>
										<td>${user.booksHasTaken}</td>
										<c:if test="${not empty user}">
											<td><input type="radio" name="userId" value="${user.id}"></td>
										</c:if>
									</tr>
								</c:forEach>
							</c:if>
							<c:if test="${noMatchesFound}">
								<tr>
									<td colspan=7>
										<span class="message">
											<fmt:message key="listLibrarians.noMatches"/>
										</span>
									</td>
								</tr>
							</c:if>
						</table>
						<br />
						<c:if test="${param.command == 'listLibrarians'}">
							<input value="<fmt:message key="listLibrarians.deleteLibrarian"/>" type="submit"
								onclick="return validate('${areYouSureMessage}');" />
						</c:if>
						<c:if test="${param.command == 'listReaders'}">
							<input value="<fmt:message key="listLibrarians.details"/>" type="submit" />
						</c:if>
					</form>
					<c:if test="${param.command == 'listLibrarians'}">
						<form action="${controllerUrl}">
							<input type="hidden" name="command" value="addLibrarian" />
							<input type="submit" value="<fmt:message key="listLibrarians.addLibrarian"/>" />
						</form>
					</c:if>
				</div> 
			</td>
		</tr>

		<%@ include file="/WEB-INF/jspf/footer.jspf"%>

	</table>
</body>