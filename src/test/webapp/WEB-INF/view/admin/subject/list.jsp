<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>

	<div class="header">
		<fmt:message key="subject.list_jsp.label.subjects_list" />
	</div>
	<div class="view">
		<ol>
			<c:forEach var="subject" items="${allSubjects}">
				<li><a
					href="<c:url value="controller?command=viewSubject"> <c:param name="name_en" value="${subject.nameEn}"/></c:url>">
						<c:out value="${lang eq 'ru' ? subject.nameRu : subject.nameEn}"></c:out>
				</a></li>
			</c:forEach>
		</ol>
		<a href="controller?command=addSubject"><fmt:message
				key="subject.list_jsp.button.add" /></a>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>