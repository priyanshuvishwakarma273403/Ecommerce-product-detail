<%-- modern_template.jsp — The ideal modern JSP--%>
<%-- Notice: ZERO Java code! Everything through EL + JSTL--%>
<%@pagecontentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${fn:escapeXml(pageTitle)} | ${initParam.appName}</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
</head>
<body>
<%-- Reusable header--%>
<jsp:include page="/WEB-INF/fragments/header.jsp" />
<%-- Flash messages--%>
<c:if test="${not empty sessionScope.flashMessage}">
<t:alert type="${sessionScope.flashType}" dismissible="true">
${fn:escapeXml(sessionScope.flashMessage)}
</t:alert>
<c:remove var="flashMessage" scope="session" />
<c:remove var="flashType" scope="session" />
</c:if>
<%-- Main Content--%>
<main>
<c:choose>
<c:when test="${not empty items}">
<c:forEach items="${items}" var="item" varStatus="loop">
<t:card title="${fn:escapeXml(item.name)}">
<p>${fn:escapeXml(item.description)}</p>
<p>Price: <fmt:formatNumber value="${item.price}"
type="currency" /></p>
</t:card>
</c:forEach>
</c:when>
<c:otherwise>
<p>No items found.</p>
</c:otherwise>
</c:choose>
</main>
<%-- Reusable footer--%>
<jsp:include page="/WEB-INF/fragments/footer.jsp" />
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>