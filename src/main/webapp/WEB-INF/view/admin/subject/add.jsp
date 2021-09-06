<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<div class="form">
		<form id="add_subject" action="controller" method="POST">
			<input type="hidden" name="command" value="addSubject" />
			<div class="field">
				<label for="name_ru"><fmt:message
						key="subject.add_jsp.label.name" /> (ru)</label> <input type="text"
					name="name_ru" id="name_ru" value="" required />
			</div>

			<div class="field">
				<label for="name_en"><fmt:message
						key="subject.add_jsp.label.name" /> (en)</label> <input type="text"
					name="name_en" id="name_en" value="" required />
			</div>
			<p>
				<input type="submit"
					value="<fmt:message key="subject.add_jsp.button.submit" />">
			</p>
		</form>
	</div>
	<script src="<c:url value="/script/subject-validation.js"/>"></script>
</body>
</html>