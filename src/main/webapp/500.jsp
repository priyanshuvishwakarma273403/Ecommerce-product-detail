<%-- /error/500.jsp--%>
<%@ pagecontentType="text/html;charset=UTF-8" %>
<%@ pageisErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
<title>Error- 500</title>
<style>
body { font-family: 'Segoe UI', sans-serif; background: #f5f6fa;
display: flex; justify-content: center; align-items: center;
min-height: 100vh; margin: 0; }
.error-box { background: white; padding: 40px; border-radius: 10px;
box-shadow: 0 5px 15px rgba(0,0,0,0.1); max-width: 600px;
text-align: center; }
h1 { color: #e74c3c; font-size: 72px; margin: 0; }
h2 { color: #2c3e50; }

.details { background: #f8f9fa; padding: 15px; border-radius: 5px;
text-align: left; font-family: monospace; font-size: 13px;
margin: 15px 0; max-height: 200px; overflow-y: auto; }
.btn { padding: 10px 25px; background: #3498db; color: white;
border: none; border-radius: 5px; cursor: pointer;
text-decoration: none; display: inline-block; margin: 5px; }
</style>
</head>
<body>
<div class="error-box">
<h1>500</h1>

<h2>Internal Server Error</h2>
<p>Something went wrong on our end. Please try again later.</p>
<%-- Display error details (only in development!)--%>
<c:if test="${not empty pageContext.exception}">
<div class="details">
<strong>Exception:</strong> ${pageContext.exception.class.name}<br/>
<strong>Message:</strong> ${pageContext.exception.message}<br/>
<c:if test="${initParam.env == 'development'}">
<br/><strong>Stack Trace:</strong><br/>
<c:forEach items="${pageContext.exception.stackTrace}" var="trace">
${trace}<br/>
</c:forEach>
</c:if>
</div>
</c:if>

<%-- Custom error attribute from servlet--%>
<c:if test="${not empty error}">
<div class="details">
<strong>Error:</strong> ${error}
</div>
</c:if>
<div>
<a href="${pageContext.request.contextPath}/" class="btn">🏠 Home</a>
<a href="javascript:history.back()" class="btn">⬅️Go Back</a>
</div>
</div>
</body>
</html>