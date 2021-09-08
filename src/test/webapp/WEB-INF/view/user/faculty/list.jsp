<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>

	<div class="header">
		<fmt:message key="faculty.list_jsp.label.faculties_list" />
	</div>

	<table id="facultiesTable" class="display">
		<thead>
			<tr>
				<td><fmt:message key="faculty.list_jsp.label.name" /></td>
				<td><fmt:message key="faculty.list_jsp.label.total_places" /></td>
				<td><fmt:message key="faculty.list_jsp.label.budget_places" /></td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="faculty" items="${faculties}">
				<tr>
					<td><a
						href="<c:url value="controller?command=viewFaculty"> <c:param name="name_en" value="${faculty.nameEn}"/></c:url>">
							<c:out
								value="${language eq 'ru' ? faculty.nameRu : faculty.nameEn}"></c:out>
					</a></td>
					<td><c:out value="${faculty.totalPlaces}"></c:out></td>
					<td><c:out value="${faculty.budgetPlaces}"></c:out></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

	<script type="text/javascript">
		var language = "${language}";
		$(document).ready(function() {
			$('#facultiesTable').dataTable({
				"language" : {
					"url" : (language == 'ru') ? "script/russian.lang" : "",
				}
			});
		});
	</script>
</body>
</html>