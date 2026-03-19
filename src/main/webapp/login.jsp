<%-- login.jsp--%>
<%@ pagecontentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- Redirect if already logged in--%>
<c:if test="${not empty sessionScope.user}">
<c:redirect url="/dashboard.jsp" />
</c:if>
<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<style>
body { font-family: 'Segoe UI', sans-serif; background: #2c3e50;
display: flex; justify-content: center; align-items: center;
min-height: 100vh; margin: 0; }
.login-box { background: white; padding: 40px; border-radius: 10px;
box-shadow: 0 15px 35px rgba(0,0,0,0.3); width: 400px; }
h1 { text-align: center; color: #2c3e50; margin-bottom: 30px; }
.form-group { margin-bottom: 20px; }
label { display: block; margin-bottom: 5px; color: #2c3e50;
font-weight: 600; }
input[type="text"], input[type="password"] {
width: 100%; padding: 12px; border: 2px solid #ecf0f1;
border-radius: 5px; font-size: 15px; box-sizing: border-box; }
input:focus { border-color: #3498db; outline: none; }
.btn-login { width: 100%; padding: 12px; background: #3498db;
color: white; border: none; border-radius: 5px;
font-size: 16px; cursor: pointer; }
.btn-login:hover { background: #2980b9; }
.error { background: #f8d7da; color: #721c24; padding: 10px;
border-radius: 5px; margin-bottom: 15px; text-align: center; }
.success { background: #d4edda; color: #155724; padding: 10px;
border-radius: 5px; margin-bottom: 15px; text-align: center; }
.checkbox { display: flex; align-items: center; gap: 8px; }
</style>
</head>
<body>
<div class="login-box">
<h1>🔐Login</h1>

<c:if test="${param.logout == 'true'}">
<div class="success">You have been logged out successfully.</div>
</c:if>
<c:if test="${not empty error}">
<div class="error">❌ ${fn:escapeXml(error)}</div>
</c:if>
<form action="${pageContext.request.contextPath}/login" method="post">
<div class="form-group">
<label for="username">Username</label>
<input type="text" id="username" name="username"
value="${fn:escapeXml(not empty username ? username : rememberedUser)}"
placeholder="Enter username" required autofocus />
</div>
<div class="form-group">
<label for="password">Password</label>
<input type="password" id="password" name="password"
placeholder="Enter password" required />
</div>
<div class="form-group">
<div class="checkbox">
<input type="checkbox" id="rememberMe" name="rememberMe"
${not empty rememberedUser ? 'checked' : ''} />
<label for="rememberMe" style="margin:0; font-weight:normal;">
Remember me
</label>
</div>
</div>
<button type="submit" class="btn-login">Sign In</button>
</form>
</div>
</body>
</html>
