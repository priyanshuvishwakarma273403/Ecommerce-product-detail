<%-- security_demo.jsp--%>
<%@ pagecontentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<body>
<h1> Security Best Practices</h1>
<h2>1. XSS Prevention</h2>
<c:set var="userInput" value='<script>alert("XSS")</script><img src=x onerror=alert(1)>' />
<%-- ❌ VULNERABLE — Neverdothis--%>
<%-- <p> ${userInput}</p>--%>
<%-- ✅ SAFE — Usec:out(auto-escapes HTML)--%>
<p>Safe output: <c:out value="${userInput}" /></p>
<%-- ✅SAFE—Usefn:escapeXml--%>
<p>Safe output: ${fn:escapeXml(userInput)}</p>
<%-- ✅SAFE—Inattributes--%>
<input type="text" value="${fn:escapeXml(userInput)}" />
<h2>2. CSRF Prevention Token</h2>
<%
// Generate CSRF token
String csrfToken = java.util.UUID.randomUUID().toString();
session.setAttribute("csrfToken", csrfToken);
%>
<form action="/process" method="post">
<input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
<input type="text" name="data" />
<button type="submit">Submit</button>
</form>
<h2>3. SQL Injection Prevention</h2>
<pre>
// ❌ NEVER concatenate user input in SQL:
String sql = "SELECT * FROM users WHERE name = '" + name + "'";
// ✅ ALWAYS usePreparedStatement:
String sql = "SELECT * FROM users WHERE name = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, name);
</pre>
</body>
</html>