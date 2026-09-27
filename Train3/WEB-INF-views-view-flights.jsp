<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="common/header.jsp" %>

<div class="dashboard">
  <div class="page-head">
    <h2>&#9992; Flight Journey Status</h2>
    <a href="${ctx}/admin/dashboard" class="btn btn-secondary btn-sm">&larr; Back to Dashboard</a>
  </div>

  <div class="flight-info"><strong>Current Date:</strong> ${today}</div>

  <div class="tabs">
    <a href="${ctx}/admin/flights?tab=upcoming"
       class="tab ${activeTab == 'upcoming' ? 'tab-active' : ''}">
      Upcoming Journeys <span class="badge badge-info">${upcoming.size()}</span>
    </a>
    <a href="${ctx}/admin/flights?tab=completed"
       class="tab ${activeTab == 'completed' ? 'tab-active' : ''}">
      Completed Journeys <span class="badge badge-success">${completed.size()}</span>
    </a>
  </div>

  <c:set var="rows" value="${activeTab == 'completed' ? completed : upcoming}"/>

  <div class="card">
    <c:choose>
      <c:when test="${empty rows}">
        <p class="muted">
          No ${activeTab == 'completed' ? 'completed' : 'upcoming'} journeys with bookings.
        </p>
      </c:when>
      <c:otherwise>
        <table class="table">
          <tr>
            <th>Flight Number</th><th>Route</th><th>Journey Date</th>
            <th>Total Passengers</th><th>Total Bookings</th><th>Total Revenue</th>
            <th>${activeTab == 'completed' ? 'Completed' : 'Days Until Journey'}</th>
          </tr>
          <c:forEach var="j" items="${rows}">
            <tr>
              <td><span class="badge badge-info">${j.flightNumber}</span></td>
              <td>${j.source} &rarr; ${j.destination}<br/><small class="muted">Dep ${j.departureTime}</small></td>
              <td><strong>${j.journeyDate}</strong></td>
              <td>&#128101; ${j.totalPassengers}</td>
              <td>${j.totalBookings}</td>
              <td>&#8377;${j.totalRevenue}</td>
              <td>
                <c:choose>
                  <c:when test="${j.daysUntil == 0}">Today</c:when>
                  <c:when test="${j.daysUntil > 0}">${j.daysUntil} days</c:when>
                  <c:otherwise>${-j.daysUntil} days ago</c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
        </table>
      </c:otherwise>
    </c:choose>
  </div>
</div>

<%@ include file="common/footer.jsp" %>
