<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:url var="controllerUrl" value="controller" />
<fmt:message key="catalog.catalog" var="calalogTitle"/>
<c:set var="title" value="${calalogTitle}" scope="page" />

<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<table id="main-container">

		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		
		<tr>
			<td class="content">
				<div id="catalog_list_div">
					<form id="sort_by" action="${controllerUrl}">
						<input type="hidden" name="command" value="listCatalog" />
						<fieldset>
							<legend>
								<fmt:message key="catalog.sortBy"/>
							</legend>
							<select size="1" name="sortBy">
								<c:set var="selectedTitle"
									value="${param.sortBy == 'title' ? 'selected' : '' }" />
								<c:set var="selectedAuthor"
									value="${param.sortBy == 'author' ? 'selected' : '' }" />
								<c:set var="selectedEdition"
									value="${param.sortBy == 'edition' ? 'selected' : '' }" />
								<c:set var="selectedPublicationYear"
									value="${param.sortBy == 'publicationYear' ? 'selected' : '' }" />
								<option value="title" ${selectedTitle}>
									<fmt:message key="catalog.title"/>
								</option>
								<option value="author" ${selectedAuthor}>
									<fmt:message key="catalog.author"/>
								</option>
								<option value="edition" ${selectedEdition}>
									<fmt:message key="catalog.edition"/>
								</option>
								<option value="publicationYear" ${selectedPublicationYear}>
									<fmt:message key="catalog.publicationYear"/>
								</option>
							</select> <input value="<fmt:message key="catalog.sort"/>"
								type="submit" />
						</fieldset>
					</form>

					<form id="find_by" action="${controllerUrl}">
						<fieldset>
							<legend>
								<fmt:message key="catalog.findBy"></fmt:message>
							</legend>
							<input type="hidden" name="command" value="listCatalog" />
							<input name="title" placeholder="<fmt:message key="catalog.title"/>"/>
							<input name="author" placeholder="<fmt:message key="catalog.author"/>"/>
							<input value="<fmt:message key="catalog.find"/>" type="submit" />
						</fieldset>
					</form>
					<form id="reset_find_by" action="${controllerUrl}">
						<input type="hidden" name="command" value="listCatalog" />
						<input type="hidden" name="title" />
						<input type="hidden" name="author" />
						<input type="submit" value="<fmt:message key="catalog.reset"/>">
					</form>

					<c:if test="${bookRequestIsSent}">
						<div class="alert alert-success">
							<fmt:message key="catalog.yourRequestIsSent"/>
						</div>
					</c:if>
					<form id="make_order" action="${controllerUrl}">
						<input type="hidden" name="command" value="listCatalog" />
						<table id="list_catalog_table" class="table" >
							<thead>
								<tr>
									<th class="message" colspan=7>
										<fmt:message key="catalog.catalog" />
									</th>
								</tr>
								<tr>
									<td>№</td>
									<td><fmt:message key="catalog.title"/></td>
									<td><fmt:message key="catalog.author"/></td>
									<td><fmt:message key="catalog.edition"/></td>
									<td><fmt:message key="catalog.publicationYear"/></td>
									<td><fmt:message key="catalog.instancesNumber"/></td>
									<c:if test="${not empty user}">
										<td></td>
									</c:if>
								</tr>
							</thead>
							<c:if test="${not noMatchesFound}">
								<c:set var="k" value="0" />
								<c:forEach var="item" items="${catalogItems}">
									<c:set var="k" value="${k+1}" />
									<tr>
										<td><c:out value="${k}" /></td>
										<td>${item.title}</td>
										<td>${item.author}</td>
										<td>${item.edition}</td>
										<td>${item.publicationYear}</td>
										<td>${item.instancesNumber}</td>
										<c:if test="${not empty user}">
											<td>
												<input type="checkbox" name="itemId" value="${item.id}"/>
											</td>
										</c:if>
									</tr>
								</c:forEach>
							</c:if>
							<c:if test="${noMatchesFound}">
								<tr>
									<td colspan=7>
										<span class="message">
											<fmt:message key="catalog.noMatches"/>
										</span>
									</td>
								</tr>
							</c:if>
						</table>
						<br />
						<c:if test="${not empty user && not noMatchesFound}">
							<input value="<fmt:message key="catalog.sendRequest"/>" type="submit" />
						</c:if>
					</form>
				</div>
			</td>
		</tr>

		<%@ include file="/WEB-INF/jspf/footer.jspf"%>

	</table>
</body>