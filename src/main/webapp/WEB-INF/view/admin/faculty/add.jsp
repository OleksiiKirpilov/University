<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>

	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>


	<div class="form">

		<form id="add_faculty" action="controller" method="POST" onsubmit="return validate();">
			<input type="hidden" name="command" value="addFaculty" />
			<div class="field">
				<label for="name_ru">
					<fmt:message key="faculty.add_jsp.label.name" /> (ru)</label>
				<input type="text" name="name_ru" id="name_ru" value="" required />
			</div>
			<div class="field">
				<label for="name_en"><fmt:message
						key="faculty.add_jsp.label.name" /> (en)</label>
				<input type="text" name="name_en" id="name_en" value="" required />
			</div>
			<div class="field">
				<label for="total_places"><fmt:message
						key="faculty.add_jsp.label.total_places" /></label>
				<input type="number" name="total_places" id="total_places"
					   value="" min="1" max="1000" step="1" required />
			</div>
			<div class="field">
				<label for="budget_places"><fmt:message
						key="faculty.add_jsp.label.budget_places" /></label>
				<input type="number" name="budget_places" id="budget_places"
					   value="" min="0" max="200" step="1" required />
			</div>
			<p>
				<label><fmt:message
						key="faculty.add_jsp.label.preliminary_subjects" />
				</label>
			</p>
			<br>
			<c:forEach var="subject" items="${allSubjects}">
				<p>
					<input type="checkbox" name="subjects" value="${subject.id}" />
					<c:out value="${lang eq 'ru' ? subject.nameRu : subject.nameEn}"></c:out>
				</p>
			</c:forEach>
			<p>
				<input type="submit" value="<fmt:message key="faculty.add_jsp.button.submit" />">
			</p>
		</form>

		<a href="controller?command=viewAllFaculties">
			<fmt:message key="faculty.add_jsp.button.back" />
		</a>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

	<script src="script/faculty-validation.js"></script>

</body>
</html>