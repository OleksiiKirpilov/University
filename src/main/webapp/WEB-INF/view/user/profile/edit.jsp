<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>
	<div class="header">
		<fmt:message key="profile.edit_jsp.header" />
	</div>
	<br>
	<br>

	<div class="form">
		<form id="profile" method="POST" action="controller">
			<input type="hidden" name="command" value="editProfile">
			<input type="hidden" name="oldEmail" value="${requestScope.email}">

			<div class="field">
				<label for="lang">
					<fmt:message key="profile.edit_jsp.label.language" />
				</label>
				<select name="lang" id="lang">
					<c:set var="selected" value="${language == 'ru' ? 'selected' : ''}"/>
					<option value="ru" ${selected}>Russian</option>
					<c:set var="selected" value="${language != 'ru' ? 'selected' : ''}"/>
					<option value="en" ${selected}>English</option>
				</select>
			</div>

			<div class="field">
				<label for="first_name">
					<fmt:message key="profile.edit_jsp.label.first_name" /></label>
				<input name="first_name" id="first_name" type="text"
					   value="${requestScope.first_name}" required />
			</div>
			<div class="field">
				<label for="last_name">
					<fmt:message key="profile.edit_jsp.label.last_name" />
				</label>
				<input name="last_name" type="text" id="last_name"
					   value="${requestScope.last_name}" required />
			</div>
			<div class="field">
				<label for="email">
					<fmt:message key="profile.edit_jsp.label.email" />
				</label>
				<input name="email" id="email" type="text" value="${requestScope.email}" required />
			</div>
			<div class="field">
				<label for="password">
					<fmt:message key="profile.edit_jsp.label.password" />
				</label>
				<input name="password" id="password"
					type="password" value="${requestScope.password}" required />
			</div>
			<div class="field">
				<label for="city">
					<fmt:message key="profile.edit_jsp.label.city" />
				</label>
				<input name="city" id="city" type="text" value="${requestScope.city}" required />
			</div>
			<div class="field">
				<label for="district">
					<fmt:message key="profile.edit_jsp.label.district" />
				</label>
				<input name="district" id="district"
					type="text" value="${requestScope.district}" required />
			</div>
			<div class="field">
				<label for="school">
					<fmt:message key="profile.edit_jsp.label.school" />
				</label>
				<input name="school" id="school"
					type="text" value="${requestScope.school}" required />
			</div>
			<div class="field">
				<input type="submit"
					value="<fmt:message key="profile.edit_jsp.button.update" />" />
			</div>
		</form>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>