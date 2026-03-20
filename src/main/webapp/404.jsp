<%-- /error/404.jsp--%>
<%@pagecontentType="text/html;charset=UTF-8" %>
<%@pageisErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
<title>404- Page Not Found</title>
<style>
body { font-family: 'Segoe UI', sans-serif; background: #f5f6fa;
display: flex; justify-content: center; align-items: center;
min-height: 100vh; margin: 0; text-align: center; }
h1 { font-size: 120px; margin: 0; color: #3498db; }
.btn { padding: 10px 25px; background: #3498db; color: white;
border-radius: 5px; text-decoration: none; }
</style>
</head>
<body>
<div>
<h1>404</h1>
<h2>Page Not Found</h2>
<p>The page you're looking for doesn't exist.</p>
<p>Requested: <code>${pageContext.request.requestURI}</code></p>
<br/>
<a href="${pageContext.request.contextPath}/" class="btn">🏠 Go Home</a>
</div>
</body>
</html>
