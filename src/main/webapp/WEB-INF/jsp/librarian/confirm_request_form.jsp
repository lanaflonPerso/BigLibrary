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
					<form id="confirm_request" action="${controllerUrl}" method="post"
						onsubmit="return validate('${areYouSureMessage}');">
						<input type="hidden" name="command" value="confirmRequestCommand" />
						<table id="list_catalog_table" class="table">
						<thead>
							<tr>
								<th class="message" colspan=2>
									<fmt:message key="confirmRequest.request"/>
								</th>
							</tr>
							<tr>
								<td>
									<fmt:message key="confirmRequest.title"/>
								</td>
								<td>${catalogItemRequestBean.title}</td>
							</tr>
						</thead>
							<tr>
								<td>
									<fmt:message key="confirmRequest.author"/>
								</td>
								<td>${catalogItemRequestBean.author}</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="confirmRequest.firstName"/>
								</td>
								<td>${catalogItemRequestBean.firstName}</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="confirmRequest.lastName"/>
								</td>
								<td>${catalogItemRequestBean.lastName}</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="confirmRequest.status"/>
								</td>
								<td><select size="1" name="bookStatus" >
										<c:set var="selectedLibraryCard"
											value="${param.bookStatus == 'libraryCard' ? 'selected' : '' }" />
										<c:set var="selectedReadingRoom"
											value="${param.bookStatus == 'readingRoom' ? 'selected' : '' }" />
										<option value="libraryCard" ${selectedLibraryCard}>
											<fmt:message key="confirmRequest.libraryCard"/>
										</option>
										<option value="readingRoom" ${selectedReadingRoom}>
											<fmt:message key="confirmRequest.readingRoom"/>
										</option>
								</select></td>
							</tr>
							<tr>
								<td>
									<fmt:message key="confirmRequest.dateFrom"/>
								</td>
								<jsp:useBean id="now" class="java.util.Date" />
								<c:set var="dateFrom" value="${now}" scope="request" />
								<td id="dateFromTd">
									<fmt:formatDate pattern="yyyy-MM-dd" value="${now}"/>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="confirmRequest.dateTo"/>
								</td>
								<td id="dateToTd">
									<input name="dateTo" placeholder="yyyy-MM-dd" value="${dateTo}" type="text"  id="datepicker" class="datepicker-here" data-language='en'/>
									<br/> 
									<span class="message">${dateToMessage}</span>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="confirmRequest.penaltySize"/>:
								</td>
								<td>
									<input name="penaltySize" placeholder="<fmt:message key="confirmRequest.penaltySize"/>" value="${penaltySize}" />
									<br/> 
									<span class="message">${penaltySizeMessage}</span>
								</td>
							</tr>
							<c:if test="${not empty validationMessage}">
								<tr>
									<td colspan=2><span class="message">${validationMessage}</span></td>
								</tr>
							</c:if>
						</table>
						<br /> 
						<input value="<fmt:message key="confirmRequest.confirmRequest"/>" type="submit" />
					</form>
					<form id="confirm_request" action="${controllerUrl}" method="post"
						onsubmit="return validate('${areYouSureMessage}');">
						<input type="hidden" name="command" value="confirmRequestCommand" />
						<input type="hidden" name="deleteRequest" value="true" />
						<input value="<fmt:message key="confirmRequest.deleteRequest"/>" type="submit" />
					</form>
				</div>
			</td>
		</tr>

		<%@ include file="/WEB-INF/jspf/footer.jspf"%>

	</table>
</body>