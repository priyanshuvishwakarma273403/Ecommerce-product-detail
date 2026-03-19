<%-- dashboard.jsp--%>
<%@ pagecontentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Protect page — redirect to login if not authenticated--%>
<c:if test="${empty sessionScope.user}">
<c:redirect url="/login" />
</c:if>
<!DOCTYPE html>
<html>
<head><title>Dashboard</title></head>
<body>
<h1>Welcome, ${sessionScope.user.name}!</h1>
<h2>Session Information</h2>
<table border="1" cellpadding="8">
<tr>
<td>Username</td>
<td>${sessionScope.user.name}</td>
</tr>
<tr>
<td>Email</td>
<td>${sessionScope.user.email}</td>
</tr>
<tr>
<td>Role</td>
<td>${sessionScope.role}</td>
</tr>
<tr>
<td>Session ID</td>
<td>${pageContext.session.id}</td>
</tr>
<tr>
<td>Login Time</td>
<td>
<jsp:useBean id="loginDate" class="java.util.Date" />
<jsp:setProperty name="loginDate" property="time"
value="${sessionScope.loginTime}" />
<fmt:formatDate value="${loginDate}" pattern="yyyy-MM-dd HH:mm:ss" />
</td>
</tr>
<tr>
<td>Session Timeout</td>
<td>${pageContext.session.maxInactiveInterval / 60} minutes</td>
</tr>
</table>
<%-- Role-based content--%>
<c:if test="${sessionScope.role == 'ADMIN'}">
<h2>Admin Panel</h2>
<ul>
<li><a href="${pageContext.request.contextPath}/products?action=list">
Manage Products</a></li>
<li><a href="${pageContext.request.contextPath}/users?action=list">
Manage Users</a></li>
</ul>
</c:if>
<p><a href="${pageContext.request.contextPath}/logout">🚪 Logout</a></p>
</body>
</html>
