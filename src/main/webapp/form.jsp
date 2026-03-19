<%-- /products/form.jsp--%>
<%@pagecontentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
<title>${formTitle}</title>
<style>
body { font-family: 'Segoe UI', sans-serif; margin: 0; padding: 20px;
background: #f5f6fa; }
.form-container { max-width: 600px; margin: 0 auto; background: white;
padding: 30px; border-radius: 8px;
box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
h1 { color: #2c3e50; margin-bottom: 25px; }
.form-group { margin-bottom: 18px; }
label { display: block; margin-bottom: 5px; font-weight: 600;
color: #2c3e50; }
input[type="text"], input[type="number"], textarea, select {
width: 100%; padding: 10px; border: 1px solid #ddd;
border-radius: 5px; font-size: 14px; }

input:focus, textarea:focus, select:focus {
border-color: #3498db; outline: none;
box-shadow: 0 0 3px rgba(52,152,219,0.3); }
textarea { height: 100px; resize: vertical; }
.btn { padding: 10px 24px; border: none; border-radius: 5px;
cursor: pointer; font-size: 15px; color: white; }
.btn-primary { background: #3498db; }
.btn-secondary { background: #95a5a6; text-decoration: none;
display: inline-block; }
.btn:hover { opacity: 0.9; }
.error { background: #f8d7da; color: #721c24; padding: 10px;
border-radius: 5px; margin-bottom: 15px;
border: 1px solid #f5c6cb; }
.checkbox-group { display: flex; align-items: center; gap: 8px; }
</style>
</head>
<body>
<div class="form-container">
<h1>${formTitle}</h1>
<c:if test="${not empty error}">
<div class="error">⚠️${error}</div>
</c:if>

<form action="${pageContext.request.contextPath}/products" method="post">
<input type="hidden" name="action" value="${formAction}" />
<c:if test="${not empty product}">
<input type="hidden" name="id" value="${product.id}" />
</c:if>
<div class="form-group">
<label for="name">Product Name *</label>
<input type="text" id="name" name="name"
value="${fn:escapeXml(product.name)}"
placeholder="Enter product name" required />
</div>
<div class="form-group">
<label for="description">Description</label>
<textarea id="description" name="description"
placeholder="Product description">${fn:escapeXml(product.description)}</textarea>
</div>

<div class="form-group">
<label for="category">Category</label>
<select id="category" name="category">
<option value="">-- Select Category--</option>
<c:forEach items="${['Electronics','Books','Clothing','Sports','Accessories']}"
var="cat">
<option value="${cat}"
${product.category == cat ? 'selected' : ''}>
${cat}
</option>
</c:forEach>
</select>
</div>

<div class="form-group">
<label for="price">Price ($) *</label>
<input type="number" id="price" name="price"
value="${product.price}" step="0.01" min="0"
placeholder="0.00" required />

</div>
<div class="form-group">
<label for="quantity">Quantity</label>
<input type="number" id="quantity" name="quantity"
value="${product.quantity != null ? product.quantity : 0}"
min="0" />
</div>
<div class="form-group">
<div class="checkbox-group">
<input type="checkbox" id="active" name="active"
${product.active || empty product ? 'checked' : ''} />
<label for="active" style="margin-bottom:0;">Active</label>
</div>
</div>

<div style="display:flex; gap:10px; margin-top:20px;">
<button type="submit" class="btn btn-primary">
${formAction == 'create' ? '➕ Create Product' : '💾 Update Product'}
</button>
<a href="${pageContext.request.contextPath}/products?action=list"
class="btn btn-secondary">Cancel</a>
</div>
</form>
</div>
</body>
</html>