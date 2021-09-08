<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<div class="header">
		<fmt:message key="welcome_jsp.label.greeting" />
	</div>
	<br><br>

	<c:choose>
		<c:when test="${empty user}">

			<div class="welcomeform">
				<form id="login_form" action="controller" method="POST">
					<input type="hidden" name="command" value="login" />
					<div class="field">
						<label>
							<fmt:message key="welcome_jsp.label.login" />
						</label>
						<input type="text" name="email" required />
					</div>
					<br>
					<div class="field">
						<label>
							<fmt:message key="welcome_jsp.label.password" />
						</label>
						<input type="password" name="password" required />
					</div>
					<div class="field">
						<input type="submit"
							value="<fmt:message key="welcome_jsp.button.login"/>">
					</div>

					<div class="field">
						<fmt:message key="welcome_jsp.label.not_registered_msg" />
						<a href="controller?command=userRegistration"><fmt:message
								key="welcome_jsp.label.register_here_msg" /></a>
					</div>
				</form>
			</div>

		</c:when>
	</c:choose>

<%@ include file="/WEB-INF/view/jspf/message.jspf" %>
<%@ include file="/WEB-INF/view/jspf/footer.jspf"%>
</body>
</html>