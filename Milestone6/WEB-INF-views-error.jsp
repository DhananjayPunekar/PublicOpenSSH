<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="common/header.jsp" %>

<div class="card error-card">
  <div class="error-code">${status}</div>
  <h2>${title}</h2>
  <p class="muted">${message}</p>

  <div class="confirm-actions">
    <a href="${ctx}/home" class="btn btn-primary">Go to Home</a>
    <c:if test="${not empty sessionScope.loggedInUser}">
      <c:choose>
        <c:when test="${sessionScope.loggedInUser.role == 'ADMIN'}">
          <a href="${ctx}/admin/dashboard" class="btn btn-outline">Dashboard</a>
        </c:when>
        <c:otherwise>
          <a href="${ctx}/user/dashboard" class="btn btn-outline">Dashboard</a>
        </c:otherwise>
      </c:choose>
    </c:if>
  </div>
</div>

<%@ include file="common/footer.jsp" %>
