<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="common/header.jsp" %>

<div class="dashboard">
  <h2>&#127915; My Tickets</h2>
  <p class="muted">
    Booking #${booking.bookingId} &nbsp;|&nbsp; ${flight.flightNumber}
    ${flight.source} &rarr; ${flight.destination} &nbsp;|&nbsp;
    Dep ${flight.departureTime} &nbsp;|&nbsp; Arr ${flight.arrivalTime}
  </p>

  <c:if test="${not empty successMessage}"><p class="success-text">${successMessage}</p></c:if>
  <c:if test="${not empty errorMessage}"><p class="error-text">${errorMessage}</p></c:if>

  <div class="ticket-grid">
    <c:forEach var="t" items="${tickets}">
      <div class="ticket-card ${t.status == 'CANCELLED' ? 'ticket-cancelled' : ''}">
        <div class="ticket-header">
          <span>&#9992; Flight Ticket</span>
          <span class="pnr">PNR: ${t.pnrNo}</span>
        </div>

        <div class="ticket-section">
          <div class="ticket-label">Passenger Information</div>
          <div class="ticket-row">
            <div><small class="muted">Name</small><br/>${t.passengerName}</div>
          </div>
          <div class="ticket-row">
            <div><small class="muted">Gender</small><br/>${t.gender}</div>
            <div><small class="muted">Age</small><br/>${t.age} years</div>
          </div>
        </div>

        <div class="ticket-section">
          <div class="ticket-label">Travel Information</div>
          <div class="ticket-row">
            <div><small class="muted">Journey Date</small><br/><strong>${t.journeyDate}</strong></div>
            <div><small class="muted">Seat Number</small><br/><span class="badge badge-info">${t.seatNo}</span></div>
          </div>
        </div>

        <div class="ticket-section">
          <div class="ticket-label">Booking Details</div>
          <div class="ticket-row">
            <div><small class="muted">Booking ID</small><br/>#${t.bookingId}</div>
            <div><small class="muted">Status</small><br/>
              <span class="badge ${t.status == 'BOOKED' ? 'badge-dark' : 'badge-danger'}">${t.status}</span>
            </div>
          </div>
        </div>

        <c:if test="${bookingCancellable and t.status == 'BOOKED'}">
          <form action="${ctx}/user/tickets/${t.pnrNo}/cancel?bookingId=${booking.bookingId}" method="post"
                onsubmit="return confirm('Cancel ticket for ${t.passengerName} (seat ${t.seatNo})?');">
            <button type="submit" class="btn btn-sm btn-danger btn-block">Cancel this ticket</button>
          </form>
        </c:if>
      </div>
    </c:forEach>
  </div>

  <a href="${ctx}/user/bookings" class="btn btn-secondary" style="margin-top:16px;">&larr; Back to My Bookings</a>
</div>

<%@ include file="common/footer.jsp" %>
