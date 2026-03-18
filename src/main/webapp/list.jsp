<%-- /products/list.jsp--%>
<%@ pagecontentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>


<!DOCTYPE html>
<html>
<head>
<title>Product Management</title>
<style>
* { box-sizing: border-box; }
body { font-family: 'Segoe UI', sans-serif; margin: 0; padding: 20px;
background: #f5f6fa; }
.container { max-width: 1100px; margin: 0 auto; }
h1 { color: #2c3e50; }
table { width: 100%; border-collapse: collapse; background: white;
box-shadow: 0 2px 10px rgba(0,0,0,0.1); border-radius: 8px;
overflow: hidden; }
th { background: #3498db; color: white; padding: 12px 15px;
text-align: left; }
td { padding: 10px 15px; border-bottom: 1px solid #ecf0f1; }
tr:hover { background: #f8f9fa; }
.btn { padding: 6px 14px; border: none; border-radius: 4px;
cursor: pointer; text-decoration: none; color: white;
font-size: 14px; display: inline-block; margin: 2px; }
.btn-primary { background: #3498db; }
.btn-success { background: #27ae60; }
.btn-warning { background: #f39c12; }
.btn-danger { background: #e74c3c; }
.btn:hover { opacity: 0.85; }
.search-bar { margin: 15px 0; display: flex; gap: 10px; }
.search-bar input { padding: 8px 12px; border: 1px solid #ddd;
border-radius: 4px; flex: 1; font-size: 14px; }
.badge { padding: 3px 10px; border-radius: 12px; font-size: 12px;
color: white; }
.badge-success { background: #27ae60; }
.badge-danger { background: #e74c3c; }
.pagination { text-align: center; margin: 20px 0; }
.pagination a, .pagination span {
padding: 8px 14px; margin: 0 3px; border: 1px solid #ddd;
border-radius: 4px; text-decoration: none; color: #333; }
.pagination .active { background: #3498db; color: white; border-color: #3498db; }
.toolbar { display: flex; justify-content: space-between;
align-items: center; margin-bottom: 15px; }
.alert { padding: 12px 16px; border-radius: 5px; margin-bottom: 15px; }
.alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
.alert-error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
</style>
</head>
<body>
<div class="container">
<h1>📦Product Management</h1>
<%-- Flash Messages (stored in session, display once)--%>
<c:if test="${not empty sessionScope.message}">
<div class="alert alert-success">
✅${sessionScope.message}
<c:remove var="message" scope="session" />
</div>
</c:if>
<c:if test="${not empty sessionScope.error}">
<div class="alert alert-error">
❌${sessionScope.error}
<c:remove var="error" scope="session" />
</div>
</c:if>
<%-- Toolbar--%>
<div class="toolbar">
<div>
<a href="${pageContext.request.contextPath}/products?action=new"
class="btn btn-success">+ Add Product</a>
<span style="margin-left:15px; color:#7f8c8d;">
Total: ${totalCount} products
</span>
</div>

<%-- Search--%>
<form action="${pageContext.request.contextPath}/products"
method="get" class="search-bar" style="width:400px;">
<input type="hidden" name="action" value="search" />
<input type="text" name="keyword" placeholder="Search products..."

value="${fn:escapeXml(keyword)}" />
<button type="submit" class="btn btn-primary">🔍 Search</button>
<c:if test="${not empty keyword}">
<a href="${pageContext.request.contextPath}/products?action=list"
class="btn btn-warning">Clear</a>
</c:if>
</form>
</div>

<%-- Product Table--%>
<c:choose>
<c:when test="${not empty products}">
<table>
<thead>
<tr>
<th>#</th>
<th>Name</th>
<th>Category</th>
<th>Price</th>
<th>Quantity</th>
<th>Status</th>
<th>Actions</th>
</tr>
</thead>

<tbody>
<c:forEach items="${products}" var="product" varStatus="status">
<tr>
<td>${status.count + (currentPage- 1) * 5}</td>
<td>
<a
href="${pageContext.request.contextPath}/products?action=view&id=${product.id}">
<strong>${fn:escapeXml(product.name)}</strong>
</a>
<c:if test="${not empty product.description}">
<br/>
<small style="color:#7f8c8d;">
<c:choose>
<c:when test="${fn:length(product.description) > 50}">
${fn:substring(product.description, 0, 50)}...
</c:when>
<c:otherwise>
${fn:escapeXml(product.description)}
</c:otherwise>
</c:choose>
</small>
</c:if>
</td>
<td>
<span style="background:#ecf0f1; padding:3px 8px;
border-radius:3px;">
${fn:escapeXml(product.category)}
</span>
</td>
<td>
<fmt:formatNumber value="${product.price}"
type="currency" currencyCode="USD" />
</td>
<td>${product.quantity}</td>
<td>
<c:choose>
<c:when test="${product.inStock}">
<span class="badge badge-success">In Stock</span>

</c:when>
<c:otherwise>
<span class="badge badge-danger">Out of Stock</span>
</c:otherwise>
</c:choose>
</td>
<td>
<a
href="${pageContext.request.contextPath}/products?action=edit&id=${product.id}"
class="btn btn-warning">✏️Edit</a>
<a
href="${pageContext.request.contextPath}/products?action=delete&id=${product.id}"
class="btn btn-danger"
onclick="return confirm('Delete ${fn:escapeXml(product.name)}?')">
🗑️Delete
</a>
</td>
</tr>
</c:forEach>

</tbody>
</table>
<%-- Pagination--%>
<c:if test="${totalPages > 1}">
<div class="pagination">
<c:if test="${currentPage > 1}">
<a
href="${pageContext.request.contextPath}/products?action=list&page=${currentPage-1}">
&laquo; Prev
</a>
</c:if>

<c:forEach begin="1" end="${totalPages}" var="i">
<c:choose>
<c:when test="${i == currentPage}">
<span class="active">${i}</span>
</c:when>
<c:otherwise>
<a href="${pageContext.request.contextPath}/products?action=list&page=${i}">
${i}
</a>
</c:otherwise>
</c:choose>
</c:forEach>
<c:if test="${currentPage < totalPages}">
<a
href="${pageContext.request.contextPath}/products?action=list&page=${currentPage+1}">
Next &raquo;
</a>
</c:if>
</div>
</c:if>
</c:when>
<c:otherwise>
<div style="text-align:center; padding:40px; background:white;
border-radius:8px; color:#7f8c8d;">
<h2>No products found</h2>
<p>
<a href="${pageContext.request.contextPath}/products?action=new"
class="btn btn-success">Add your first product</a>
</p>
</div>
</c:otherwise>
</c:choose>
</div>
</body>
</html>

