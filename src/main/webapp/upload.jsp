<%-- upload.jsp--%>
<%@pagecontentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<title>File Upload</title>
<style>
body { font-family: 'Segoe UI', sans-serif; max-width: 600px;
margin: 40px auto; padding: 20px; }
.upload-box { border: 2px dashed #3498db; border-radius: 10px;
padding: 40px; text-align: center; background: #f8f9fa;
margin: 20px 0; }
.upload-box:hover { background: #eef2f7; border-color: #2980b9; }
input[type="file"] { margin: 15px 0; }
.btn { padding: 10px 25px; background: #3498db; color: white;
border: none; border-radius: 5px; cursor: pointer; font-size: 15px; }
.btn:hover { background: #2980b9; }
.result { background: #d4edda; padding: 15px; border-radius: 8px;
margin-top: 20px; }
.error { background: #f8d7da; color: #721c24; padding: 15px;
border-radius: 8px; margin-top: 20px; }
textarea { width: 100%; padding: 8px; border-radius: 5px;
border: 1px solid #ddd; }
</style>
</head>
<body>
<h1>📁File Upload</h1>
<c:if test="${not empty error}">
<div class="error">❌ ${error}</div>
</c:if>

<form action="${pageContext.request.contextPath}/upload"
method="post" enctype="multipart/form-data">
<div class="upload-box">
<h3>📤Chooseafiletoupload</h3>
<input type="file" name="file" required
accept="image/*,.pdf,.doc,.docx,.txt" />
<p style="color:#7f8c8d; font-size:13px;">
Allowed: Images, PDF, Word, Text | Max: 5MB
</p>
</div>

<div style="margin: 15px 0;">
<label for="description">Description:</label>
<textarea name="description" id="description" rows="3"
placeholder="Optional file description"></textarea>
</div>
<button type="submit" class="btn">⬆️Upload File</button>
</form>
<c:if test="${not empty message}">
<div class="result">
<h3>✅${message}</h3>
<table cellpadding="5">
<tr><td><strong>Original Name:</strong></td><td>${fileName}</td></tr>
<tr><td><strong>Saved As:</strong></td><td>${savedName}</td></tr>
<tr><td><strong>Size:</strong></td><td>${fileSize}</td></tr>
<tr><td><strong>Type:</strong></td><td>${contentType}</td></tr>
</table>
<%-- Show image preview if it's an image--%>
<c:if test="${contentType.startsWith('image/')}">
<h4>Preview:</h4>
<img src="${fileUrl}" alt="Uploaded Image"
style="max-width:100%; max-height:300px; border-radius:5px;" />
</c:if>
<p><a href="${fileUrl}" target="_blank">📥 Download File</a></p>
</div>
</c:if>
</body>
</html>