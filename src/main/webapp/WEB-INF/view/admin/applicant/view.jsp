<%@ include file="/WEB-INF/view/jspf/page.jspf"%>
<%@ include file="/WEB-INF/view/jspf/taglib.jspf"%>
<html>
<%@ include file="/WEB-INF/view/jspf/head.jspf"%>
<body>
	<%@ include file="/WEB-INF/view/jspf/header.jspf"%>

	<div class="view">

		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.first_name" />
			</label>
			<c:out value="${user.firstName}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.last_name" />
			</label>
			<c:out value="${user.lastName}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.email" />
			</label>
			<c:out value="${user.email}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.city" />
			</label>
			<c:out value="${applicant.city}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.district" />
			</label>
			<c:out value="${applicant.district}"></c:out>
		</p>
		<p>
			<label>
				<fmt:message key="profile.view_jsp.label.school" />
			</label>
			<c:out value="${applicant.school}"></c:out>
		</p>

		<p>
			<label>
				<fmt:message key="applicant.view_jsp.label.blocked_status" />
			</label>
			<c:if test="${applicant.blockedStatus == true}">
				<fmt:message key="applicant.view_jsp.label.blocked" />
			</c:if>
			<c:if test="${applicant.blockedStatus == false}">
				<fmt:message key="applicant.view_jsp.label.unblocked" />
			</c:if>
		</p>

		<form action="controller" method="POST">
			<input type="hidden" name="command" value="viewApplicant">
			<input type="hidden" name="id" value="${applicant.id}">
			<input type="submit"
				   value="<fmt:message key="applicant.view_jsp.button.change_blocked_status_msg" />">
		</form>

	</div>

	<div class="form">
		<c:if test="${not empty preliminaryGrades}">
			<h3>
				<fmt:message key="applicant.view_jsp.label.preliminary_subjects"/>
			</h3>
			<c:forEach var="pg" items="${preliminaryGrades}">
				<div class="field">
					<p>
						<label>
							<c:out value="${lang eq 'ru' ? pg.value.nameRu : pg.value.nameEn}: ${pg.key.grade}"></c:out>
						</label>
					</p>
				</div>
			</c:forEach>
			<br>
		</c:if>

		<c:if test="${not empty diplomaGrades}">
			<h3>
				<fmt:message key="applicant.view_jsp.label.diploma_subjects"/>
			</h3>
			<c:forEach var="pg" items="${diplomaGrades}">
				<div class="field">
					<p>
						<label>
							<c:out value="${lang eq 'ru' ? pg.value.nameRu : pg.value.nameEn}: ${pg.key.grade}"></c:out>
						</label>
					</p>
				</div>
			</c:forEach>
			<br>
		</c:if>

		<c:if test="${notConfirmed}">
			<form action="controller" method="POST">
				<input type="hidden" name="command" value="confirmGrades"/>
				<input type="hidden" name="id" value="${applicant.id}"/>
				<input type="hidden" name="userId" value="${user.id}"/>
				<input type="submit" value="<fmt:message key="applicant.view_jsp.button.confirm_grades" />">
			</form>
		</c:if>
	</div>

	<%@ include file="/WEB-INF/view/jspf/message.jspf" %>

</body>
</html>