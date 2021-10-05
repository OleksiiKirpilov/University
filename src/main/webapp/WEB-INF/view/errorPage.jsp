<%@ include file="/WEB-INF/view/jspf/page.jspf" %>
<%@ page isErrorPage="true"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<c:set var="title" value="Error" scope="page" />

<body>
<%@ include file="/WEB-INF/view/jspf/header.jspf" %>

<div class="header">
	<h4>
		<a href="welcome.jsp">
			<fmt:message key="error_message.general_error" />
		</a>
	</h4>
</div>

</body>
</html>