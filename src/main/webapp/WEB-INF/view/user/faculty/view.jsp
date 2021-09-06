<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<h2 align="center">
		<c:out value="${language eq 'ru' ? name_ru : name_en}"></c:out>
	</h2>
	<div class="view">
		<p>
			<label><fmt:message key="faculty.view_jsp.label.name" /></label>
			<c:out value="${language eq 'ru' ? name_ru : name_en}"></c:out>
		</p>
		<p>
			<label><fmt:message key="faculty.view_jsp.label.total_places" /></label>
			<c:out value="${total_places}"></c:out>
		</p>
		<p>
			<label><fmt:message key="faculty.view_jsp.label.budget_places" /></label>
			<c:out value="${budget_places}"></c:out>
		</p>
		<p>
			<label><fmt:message
					key="faculty.view_jsp.label.preliminary_subjects" /></label>
		</p>

		<c:if test="${empty facultySubjects}">
			<fmt:message key="faculty.view_jsp.label.no_subjects_msg" />
		</c:if>

		<br>
		<c:if test="${not empty facultySubjects}">
			<ol>
				<c:forEach var="subject" items="${facultySubjects}">
					<li><c:out
							value="${language eq 'ru' ? subject.nameRu : subject.nameEn}"></c:out></li>
				</c:forEach>
			</ol>
		</c:if>
		<c:if test="${userRole eq 'user' }">
		<p>
			<a href="controller?command=applyFaculty&name_en=${name_en}"><fmt:message
					key="faculty.view_jsp.button.apply" /></a>
		</p></c:if>
	</div>
</body>
</html>