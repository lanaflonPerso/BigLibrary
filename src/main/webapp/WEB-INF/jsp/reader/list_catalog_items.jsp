<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib uri="/WEB-INF/ExtraTags.tld" prefix="et"%>

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
					<form id="book_status" action="${controllerUrl}">
						<input type="hidden" name="command" value="listPersonalArea" />
						<fieldset>
							<legend>
								<fmt:message key="personalArea.bookStatus"/>
							</legend>
							<select size="1" name="bookStatus">
								<c:set var="selectedNotConfirmed"
									value="${param.bookStatus == 'notConfirmed' ? 'selected' : '' }" />
								<c:set var="selectedLibraryCard"
									value="${param.bookStatus == 'libraryCard' ? 'selected' : '' }" />
								<option value="notConfirmed" ${selectedNotConfirmed}>
									<fmt:message key="personalArea.notConfirmed"/>
								</option>
								<option value="libraryCard" ${selectedLibraryCard}>
									<fmt:message key="personalArea.libraryCard"/>
								</option>
							</select> 
							<input value="<fmt:message key="personalArea.find"/>" type="submit" />
						</fieldset>
					</form>
					<form id="cancel_request" action="${controllerUrl}"
						onsubmit="return validate('${areYouSureMessage}');">
						<input type="hidden" name="command" value="listPersonalArea" />
						<c:if test="${requestIsCanceledSuccessfully}">
								<div class="alert alert-success">
									<fmt:message key="personalArea.requestIsCanceledSuccessfully" />
								</div>
						</c:if>
						<c:if test="${bookIsReturnedSuccessfully}">
								<div class="alert alert-success">
									<fmt:message key="personalArea.bookIsReturnedSuccessfully" />
								</div>
						</c:if>
						<c:if test="${not empty bookStatus}">
							<input type="hidden" name="bookStatus" value="${bookStatus}" />
						</c:if>
						<table id="list_catalog_table" class="table">
							<thead>
								<c:if test="${bookStatus == 'notConfirmed' or empty bookStatus}">
									<tr>
										<th class="message" colspan=4>
											<fmt:message key="personalArea.notConfirmed"/>:
										</th>
									</tr>
								</c:if>
								<c:if test="${bookStatus == 'libraryCard'}">
									<tr>
										<th class="message" colspan=6>
											<fmt:message key="personalArea.libraryCard"/>:
										</th>
									</tr>
								</c:if >
								<tr>
									<td>№</td>
									<td>
										<fmt:message key="personalArea.title"/>
									</td>
									<td>
										<fmt:message key="personalArea.author"/>
									</td>
									<c:if test="${bookStatus == 'libraryCard'}">
										<td>
											<fmt:message key="personalArea.deadline"/>
										</td>
										<td>
											<fmt:message key="personalArea.penaltySize"/>
										</td>
									</c:if>
									<td></td>
								</tr>
							</thead>
							<c:if test="${not noMatchesFound}">
								<c:set var="k" value="0" />
								<c:forEach var="bean" items="${catalogItemBeanList}">
									<c:set var="k" value="${k+1}" />
									<tr>
										<td><c:out value="${k}" /></td>
										<td>${bean.title}</td>
										<td>${bean.author}</td>
										<c:if test="${bookStatus == 'libraryCard'}">
											<td>
												<fmt:formatDate pattern="yyyy-MM-dd" value="${bean.dateTo}" />
           									</td>
										<et:printPenalty penaltyDate="${bean.dateTo}" penaltySize="${bean.penaltySize}" />
										</c:if>
										<c:if test="${not empty user}">
											<td>
												<input type="checkbox" name="beanId" value="${bean.id}" />
											</td>
										</c:if>
									</tr>
								</c:forEach>
							</c:if>
							<c:if test="${noMatchesFound}">
								<tr>
									<td colspan=6>
										<span class="message">
											<fmt:message key="personalArea.noMatches"/>
										</span>
									</td>
								</tr>
							</c:if>
						</table>
						<br />
						<c:if test="${not empty user}">
							<c:choose>
								<c:when
									test="${bookStatus == 'notConfirmed' or empty bookStatus}">
									<input value="<fmt:message key="personalArea.cancelRequest"/>" type="submit" />
								</c:when>
								<c:when test="${bookStatus == 'libraryCard'}">
									<input value="<fmt:message key="personalArea.returnBook"/>" type="submit" />
								</c:when>
							</c:choose>
						</c:if>
					</form>
				</div>
			</td>
		</tr>

		<%@ include file="/WEB-INF/jspf/footer.jspf"%>

	</table>
</body>