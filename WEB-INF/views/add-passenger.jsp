<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="common/header.jsp" %>

<div class="card auth-card">
  <h3>Add Passenger ${currentNumber}</h3>
  <p class="muted">Remaining passengers: ${remaining}</p>

  <form:form action="${ctx}/booking/passenger" method="post" modelAttribute="passengerForm">

    <div class="form-group">
      <form:label path="passengerName">Passenger Name *</form:label>
      <form:input path="passengerName" placeholder="Enter full name" cssClass="form-control"/>
      <form:errors path="passengerName" cssClass="error-text"/>
    </div>

    <div class="form-group">
      <form:label path="age">Age *</form:label>
      <form:input path="age" type="number" min="1" max="120" cssClass="form-control"/>
      <form:errors path="age" cssClass="error-text"/>
    </div>

    <div class="form-group">
      <form:label path="gender">Gender *</form:label>
      <form:select path="gender" cssClass="form-control">
        <form:option value="" label="Select Gender"/>
        <form:option value="Male" label="Male"/>
        <form:option value="Female" label="Female"/>
        <form:option value="Other" label="Other"/>
      </form:select>
      <form:errors path="gender" cssClass="error-text"/>
    </div>

    <button type="submit" class="btn btn-primary btn-block">
      <c:choose>
        <c:when test="${isLast}">Complete Booking</c:when>
        <c:otherwise>Add Passenger &amp; Continue</c:otherwise>
      </c:choose>
    </button>
  </form:form>

  <div class="progress"><div class="progress-bar" style="width:${progress}%"></div></div>
  <small class="muted">Passenger ${currentNumber} of ${totalPassengers}</small>
</div>

<%@ include file="common/footer.jsp" %>
