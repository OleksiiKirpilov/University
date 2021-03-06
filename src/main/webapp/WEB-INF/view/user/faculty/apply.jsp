<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>

	<h2 align="center">
		<c:out value="${lang eq 'ru' ? name_ru : name_en}"></c:out>
	</h2>

	<br>
	<br>

	<div class="view">
		<p>
			<label><fmt:message key="faculty.apply_jsp.label.name" /></label>

			<c:out value="${lang eq 'ru' ? name_ru : name_en}"></c:out>
		</p>
		<p>
			<label> <fmt:message
					key="faculty.apply_jsp.label.total_places" />
			</label>

			<c:out value="${total_places}"></c:out>
		</p>
		<p>
			<label><fmt:message
					key="faculty.apply_jsp.label.budget_places" /> </label>

			<c:out value="${budget_places}"></c:out>
		</p>
	</div>

	<div class="form">
		<form action="controller" method="POST">
			<input type="hidden" name="command" value="applyFaculty" /> <input
				type="hidden" name="id" value="${id}" />

			<h2>
				<fmt:message key="faculty.apply_jsp.label.preliminary_subjects" />
			</h2>


			<c:if test="${empty facultySubjects}">
				<fmt:message
					key="faculty.apply_jsp.label.no_preliminary_subjects_msg" />
			</c:if>

			<c:forEach var="facultySubject" items="${facultySubjects}">
				<div class="field">
					<p>
						<label><c:out
								value="${lang eq 'ru' ? facultySubject.nameRu : facultySubject.nameEn}"></c:out>

						</label>


						<grades:insert subjectId="${facultySubject.id}"
							examType="preliminary" />
					</p>
				</div>

			</c:forEach>

			<h2>
				<fmt:message key="faculty.apply_jsp.label.diploma_subjects" />
			</h2>


			<c:forEach var="subject" items="${allSubjects}">
				<div class="field">
					<p>
						<label><c:out
								value="${lang eq 'ru' ? subject.nameRu : subject.nameEn}">
							</c:out></label>
					<p>

						<grades:insert subjectId="${subject.id}" examType="diploma" />
				</div>
			</c:forEach>

			<p>
				<input type="submit"
					value="<fmt:message key="faculty.apply_jsp.button.submit" />">
			</p>
		</form>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>