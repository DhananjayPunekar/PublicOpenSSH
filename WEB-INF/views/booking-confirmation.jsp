<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ include file="common/header.jsp" %>

<div class="card confirm-card">
  <div class="confirm-header">&#10004; Booking Confirmed!</div>

  <div class="confirm-body">
    <div class="confirm-plane">&#9992;</div>
    <p class="confirm-title">Congratulations! Your flight is booked!</p>
    <div class="booking-id">Booking ID: ${bookingId}</div>

    <div class="confirm-features">
      <div><strong>&#128737; Secured</strong><br/><small class="muted">Your booking is protected</small></div>
      <div><strong>&#9993; Confirmed</strong><br/><small class="muted">Details saved to My Bookings</small></div>
      <div><strong>&#128197; Ready</strong><br/><small class="muted">Check-in 24hrs before</small></div>
    </div>

    <div class="next-steps">
      <strong>Next Steps:</strong>
      <ul>
        <li>Save your booking ID for future reference</li>
        <li>View your tickets under My Bookings</li>
        <li>Arrive at the airport 2 hours before departure</li>
        <li>Complete online check-in 24 hours before the flight</li>
      </ul>
    </div>

    <div class="confirm-actions">
      <a href="${ctx}/user/dashboard" class="btn btn-success">Go to Dashboard</a>
      <button type="button" class="btn btn-outline" onclick="window.print()">Print Confirmation</button>
    </div>
  </div>
</div>

<%@ include file="common/footer.jsp" %>
