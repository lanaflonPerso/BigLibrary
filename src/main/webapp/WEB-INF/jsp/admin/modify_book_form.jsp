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
						<input type="hidden" name="command" value="updateBook" />
						<table id="modify_book_table" class="table">
							<thead>
								<tr>
									<th class="message" colspan=2>
										<fmt:message key="modifyBook.book"/>
									</th>
								</tr>
								<tr>
									<td>
										<fmt:message key="modifyBook.title"/>
									</td>
									<td>
										<input name="title" value="${catalogItem.title}" size=40 />
										<br/>
										<span class="message">${titleMessage}</span>
									</td>
								</tr>
							</thead>
							<tr>
								<td>
									<fmt:message key="modifyBook.author"/>
								</td>
								<td>
									<input name="author" value="${catalogItem.author}" size=36 />
									<br/> 
									<span class="message">${authorMessage}</span>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="modifyBook.edition"/>
								</td>
								<td>
									<input name="edition" value="${catalogItem.edition}" size=32 />
									<br/>
									<span class="message">${editionMessage}</span> 
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="modifyBook.publicationYear"/>
								</td>
								<td>
									<input name="publicationYear" value="${catalogItem.publicationYear}" size=28 />
									<br/> 
									<span class="message">${publicationYearMessage}</span>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="modifyBook.instancesNumber"/>
								</td>
								<td>
									<input name="instancesNumber" value="${catalogItem.instancesNumber}" size=24 />
									<br/> 
									<span class="message">${instancesNumberMessage}</span>
								</td>
							</tr>
							<c:if test="${not empty validationMessage}">
								<tr>
									<td colspan=2><span class="message">${validationMessage}</span></td>
								</tr>
							</c:if>
						</table>
						<br/> 
						<input value="<fmt:message key="modifyBook.updateBook"/>" type="submit" />
					</form>
					<form id="confirm_request" action="${controllerUrl}" method="post"
						onsubmit="return validate('${areYouSureMessage}');">
						<input type="hidden" name="command" value="updateBook" />
						<input type="hidden" name="delete" value="true" />
						<input type="submit" value="<fmt:message key="modifyBook.deleteBook"/>" />
					</form>
				</div>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>

	</table>
</body>