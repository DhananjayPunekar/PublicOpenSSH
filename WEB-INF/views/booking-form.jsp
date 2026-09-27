<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="common/header.jsp" %>

<div class="card auth-card">
  <h3>Book Flight</h3>

  <div class="flight-info">
    <strong>Flight Number:</strong> ${flight.flightNumber}
    <span class="muted">&nbsp;|&nbsp; ${flight.source} &rarr; ${flight.destination}
      &nbsp;|&nbsp; Dep ${flight.departureTime}</span>
  </div>

  <c:if test="${not empty bookingError}">
    <p class="error-text">${bookingError}</p>
  </c:if>

  <form:form action="${ctx}/booking/new?flightId=${flight.flightId}"
             method="post" modelAttribute="bookingForm">

    <div class="form-group">
      <form:label path="journeyDate">Journey Date *</form:label>
      <form:input path="journeyDate" type="date" min="${today}" cssClass="form-control"/>
      <form:errors path="journeyDate" cssClass="error-text"/>
    </div>

    <div class="form-group">
      <form:label path="numberOfPassengers">Number of Passengers *</form:label>
      <form:input path="numberOfPassengers" type="number" min="1" max="10" cssClass="form-control"/>
      <small class="muted">Maximum 10 passengers allowed</small>
      <form:errors path="numberOfPassengers" cssClass="error-text"/>
    </div>

    <button type="submit" class="btn btn-primary btn-block">Book Now</button>
  </form:form>

  <a href="${ctx}/user/dashboard" class="btn btn-secondary btn-block" style="margin-top:10px;">
    Back to Dashboard
  </a>
</div>

<%@ include file="common/footer.jsp" %>
