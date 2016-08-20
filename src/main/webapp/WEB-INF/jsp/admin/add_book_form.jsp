<%@ page pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>

<c:url var="controllerUrl" value="controller" />

<c:set var="title" value="Personal area" scope="page" />
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
	<table id="main-container">

		<%@ include file="/WEB-INF/jspf/header.jspf"%>

		<tr>
			<td class="content">
				<div id="catalog_list_div">
					<form action="${controllerUrl}" method="post">
						<input type="hidden" name="command" value="addBook" />
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
									<input name="bookTitle" size=40 value="${bookTitle}" />
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
									<input name="author" size=36 value="${author}"/>
									<br/>
									<span class="message">${authorMessage}</span>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="modifyBook.edition"/>
								</td>
								<td>
									<input name="edition" size=32 value="${edition}" />
									<br/>
									<span class="message">${editionMessage}</span>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="modifyBook.publicationYear"/>
								</td>
								<td>
									<input name="publicationYear" size=28 value="${publicationYear}" />
									<br/> 
									<span class="message">${publicationYearMessage}</span>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="modifyBook.instancesNumber"/>
								</td>
								<td>
									<input name="instancesNumber" size=24 value="${instancesNumber}" />
									<br/> 
									<span class="message">${instancesNumberMessage}</span>
								</td>
							</tr>
						</table>
						<br/> 
						<input value="<fmt:message key="modifyBook.addBook"/>" type="submit" />
					</form>
				</div> 
			</td>
		</tr>

		<%@ include file="/WEB-INF/jspf/footer.jspf"%>

	</table>
</body>