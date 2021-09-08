<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>

	<h2 align="center">
		<c:out value="${faculty.name}" />
	</h2>

	<div class="form">
		<form id="edit_faculty" action="controller" method="POST" onsubmit="return validate();">
			<input type="hidden" name="command" value="editFaculty" /> <input
				type="hidden" name="oldName" value="${requestScope.name_en}" />
			<div class="field">
				<label for="name_ru"><fmt:message
						key="faculty.edit_jsp.label.name" /></label> <input type="text"
					name="name_ru" id="name_ru" value="${requestScope.name_ru}"
					required />
			</div>
			<div class="field">
				<label for="name_en">
					<fmt:message key="faculty.edit_jsp.label.name" />
				</label>
				<input type="text" name="name_en" id="name_en" value="${requestScope.name_en}"
				required />
			</div>
			<div class="field">
				<label for="total_places">
					<fmt:message key="faculty.edit_jsp.label.total_places" />
				</label>
				<input type="number" name="total_places" id="total_places"
					   value="${requestScope.total_places}" min="1" max="1000" step="1" required />
			</div>
			<div class="field">
				<label for="budget_places">
					<fmt:message key="faculty.edit_jsp.label.budget_places" />
				</label>
				<input type="number" name="budget_places" id="budget_places"
					   value="${requestScope.budget_places}" min="0" max="200" step="1" required />
			</div>
			<p>
				<a href="controller?command=viewFaculty&name_en=${requestScope.name_en}">
					<fmt:message key="profile.edit_jsp.button.back" />
				</a>
			</p>
			<p>
				<label>
					<fmt:message key="faculty.edit_jsp.label.preliminary_subjects" />
				</label>
			</p>

			<br>
			<c:if test="${not empty facultySubjects}">
				<c:forEach var="oldCheckedSubject" items="${facultySubjects}">
					<input type="hidden" name="oldCheckedSubjects" value="${oldCheckedSubject.id}" />
					<p>
						<input type="checkbox" name="subjects" value="${oldCheckedSubject.id}" checked />
						<c:out
							value="${lang eq 'ru' ? oldCheckedSubject.nameRu : oldCheckedSubject.nameEn}">
						</c:out>
					</p>
				</c:forEach>
			</c:if>

			<c:if test="${not empty otherSubjects}">
				<c:forEach var="oldUncheckedSubject" items="${otherSubjects}">
					<p>
						<input type="checkbox" name="subjects" value="${oldUncheckedSubject.id}" />
						<c:out
							value="${lang eq 'ru' ? oldUncheckedSubject.nameRu : oldUncheckedSubject.nameEn}">
						</c:out>
					</p>
				</c:forEach>
			</c:if>
			<input type="submit" value="<fmt:message key="faculty.edit_jsp.button.submit" />" />
		</form>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

	<script src="script/faculty-validation.js"></script>
</body>
</html>