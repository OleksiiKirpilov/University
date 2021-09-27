<%@ include file="/WEB-INF/view/jspf/page.jspf" %>
<%@ page isErrorPage="true"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<c:set var="title" value="Error" scope="page" />

<body>
<%@ include file="/WEB-INF/view/jspf/header.jspf" %>

<div class="header">
	<h4>
		<fmt:message key="error_message.general_error" />
	</h4>
</div>

</body>
</html>