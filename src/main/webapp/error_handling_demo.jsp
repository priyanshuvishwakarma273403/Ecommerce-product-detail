<%-- error_handling_demo.jsp--%>
<%@pagecontentType="text/html;charset=UTF-8" %>
<%@pageerrorPage="/error/500.jsp" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<body>
<h1>Error Handling Demo</h1>
<%-- Method 1: Using c:catch (within page)--%>
<h2>1. c:catch — Catch Within Page</h2>
<c:catch var="mathError">
<%int result = 100 / 0; %>
</c:catch>
<c:if test="${not empty mathError}">
<p style="color:red;">Caught: ${mathError.message}</p>
</c:if>
<c:catch var="nullError">
<%
String str = null;
str.length();
%>
</c:catch>
<c:if test="${not empty nullError}">
<p style="color:red;">Caught: ${nullError.class.simpleName}
—${nullError.message}</p>
</c:if>
<%-- Method 2: errorPage directive sends to error page--%>
<h2>2. Trigger Error Page</h2>
<p>
<a href="?triggerError=true">Click to trigger unhandled exception</a>
</p>
<c:if test="${param.triggerError == 'true'}">
<%
// This will forward to /error/500.jsp
throw new RuntimeException("Intentional error for demo!");
%>
</c:if>
</body>
</html>