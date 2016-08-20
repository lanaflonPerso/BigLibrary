<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<html>

<c:url var="contrullerUrl" value="controller" />
<fmt:message key="about.title" var="aboutTitle"/>
<c:set var="title" value="${aboutTitle}" />

<%@ include file="/WEB-INF/jspf/head.jspf"%>

<body>
  
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<div id="aboutDiv">
					<table class="table">
						<thead>
							<tr>
								<th colspan=2 class="message">
									<fmt:message key="about.title" />
								</th>
							</tr>
							<tr>
								<td>
									<fmt:message key="about.basicFunctionality" />
								</td>
								<td>
									<fmt:message key="about.connectionPool" />
								</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
									<fmt:message key="about.dbTriggers" />
								</td>
								<td>
									<fmt:message key="about.prg" />
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="about.log4j" />
								</td>
								<td>
									<fmt:message key="about.filters" />
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="about.validation" />
								</td>
								<td>
									<fmt:message key="about.customTag" />
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="about.jquery" />
								</td>
								<td>
									<fmt:message key="about.js" />
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="about.rememberMe" />
								</td>
								<td>
									<fmt:message key="about.security" />
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="about.urlRewriting" />
								</td>
								<td>
									<fmt:message key="about.bootstrap" />
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="about.userLocale" />
								</td>
								<td>
									<fmt:message key="about.i18n" />
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="about.captcha" />
								</td>
								<td>
									<fmt:message key="about.javaMail" />
								</td>
							</tr>
							<tr>
								<td colspan=2 class="message strong">
									<fmt:message key="projectAuthor"/>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="container">
    				<div class="back side"><span class="text">Java</span></div>
    				<div class="left side"><span class="text">&lt;khai&gt;</span></div>
    				<div class="right side"><span class="text">Library</span></div>
    				<div class="top side"><span class="text">Alex</span></div>
    				<div class="bottom side"><span class="text"><img src="image/book4.png" class="cubeImg"/></span></div>
    				<div class="front side"><span class="text">Slynko</span></div>
  				</div>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>