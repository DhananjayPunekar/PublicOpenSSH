<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="common/header.jsp" %>

<div class="dashboard">
  <div class="card">
    <h3>&#9993; My Bookings</h3>

    <c:if test="${not empty successMessage}"><p class="success-text">${successMessage}</p></c:if>
    <c:if test="${not empty errorMessage}"><p class="error-text">${errorMessage}</p></c:if>

    <c:choose>
      <c:when test="${empty bookings}">
        <p class="muted">You have no bookings yet.
          <a href="${ctx}/home">Search flights</a> to make one.</p>
      </c:when>
      <c:otherwise>
        <table class="table">
          <tr>
            <th>Booking ID</th><th>Flight Number</th><th>Route</th><th>Journey Date</th>
            <th>Seats</th><th>Total</th><th>Status</th><th>Action</th><th>Cancel</th>
          </tr>
          <c:forEach var="b" items="${bookings}">
            <tr>
              <td>${b.bookingId}</td>
              <td>${b.flightNumber}</td>
              <td>${b.source} &rarr; ${b.destination}</td>
              <td>${b.journeyDate}</td>
              <td>${b.seats}</td>
              <td>&#8377;${b.totalPrice}</td>
              <td>
                <span class="badge ${b.status == 'BOOKED' ? 'badge-success' : 'badge-danger'}">
                  ${b.status}
                </span>
              </td>
              <td>
                <a href="${ctx}/user/bookings/${b.bookingId}/tickets" class="btn btn-sm btn-info">
                  View Tickets
                </a>
              </td>
              <td>
                <c:choose>
                  <c:when test="${b.cancellable}">
                    <form action="${ctx}/user/bookings/${b.bookingId}/cancel" method="post"
                          onsubmit="return confirm('Cancel booking ${b.bookingId}? All its tickets will be cancelled.');">
                      <button type="submit" class="btn btn-sm btn-danger">Cancel</button>
                    </form>
                  </c:when>
                  <c:otherwise>
                    <button class="btn btn-sm btn-disabled" disabled>Cancel</button>
                  </c:otherwise>
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
