<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.blog.model.*"%>
<%
String contextPath = request.getContextPath();
List<String> errorMsgs = (List<String>) request.getAttribute("errorMsgs");
BlogVO blogVO = (BlogVO) request.getAttribute("blogVO");
if (blogVO == null) {
	blogVO = new BlogVO();
}

BlogService blogSvc = new BlogService();
List<UserVO> userList = blogSvc.getAllUsers();
List<FarmerVO> farmerList = blogSvc.getAllFarmers();
List<ProductVO> productList = blogSvc.getAllProducts();
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8">
<title>新增農場日記</title>
<style>
body {
	margin: 0;
	font-family: Arial, "Noto Sans TC", sans-serif;
	background: linear-gradient(180deg, #f6f7f2 0%, #eef3ea 100%);
	color: #253025;
	min-height: 100vh;
}

main {
	max-width: 900px;
	margin: 40px auto;
	padding: 0 24px;
}

a {
	color: #37683c;
}

form {
	position: relative;
	overflow: hidden;
	display: grid;
	gap: 16px;
	background: #fff;
	border: 1px solid #d9dfd3;
	border-radius: 8px;
	padding: 28px 24px 24px;
	box-shadow: 0 10px 28px rgba(37, 48, 37, 0.08);
}

form:before {
	content: "";
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	height: 5px;
	background: linear-gradient(90deg, #3f6f44, #9eb58e);
}

.grid {
	display: grid;
	grid-template-columns: repeat(2, minmax(0, 1fr));
	gap: 16px;
}

label {
	display: block;
	margin-bottom: 8px;
	font-weight: 700;
}

input, select, textarea {
	width: 100%;
	box-sizing: border-box;
	padding: 10px 12px;
	border: 1px solid #bfc8b8;
	border-radius: 6px;
	font-size: 16px;
	transition: border-color .18s ease, box-shadow .18s ease;
}

input:focus, select:focus, textarea:focus {
	outline: none;
	border-color: #3f6f44;
	box-shadow: 0 0 0 3px rgba(63, 111, 68, 0.14);
}

textarea {
	min-height: 220px;
	resize: vertical;
}

.btn {
	border: 0;
	border-radius: 6px;
	padding: 10px 16px;
	background: #3f6f44;
	color: #fff;
	cursor: pointer;
	justify-self: start;
	transition: background-color .18s ease, transform .18s ease, box-shadow .18s ease;
	box-shadow: 0 4px 12px rgba(37, 48, 37, 0.12);
}

.btn:hover {
	background: #315936;
	transform: translateY(-1px);
	box-shadow: 0 8px 18px rgba(37, 48, 37, 0.16);
}

.errors {
	margin: 0;
	padding: 12px 16px;
	border-radius: 6px;
	background: #ffe9e7;
	color: #9a2c23;
}

@media ( max-width : 720px) {
	.grid {
		grid-template-columns: 1fr;
	}
}
</style>
</head>
<body>
	<main>
		<p>
			<a href="<%=contextPath%>/farmer/blog/MyBloglist.jsp">回我的農場日誌</a>
		</p>
		<h1>新增農場日誌</h1>

		<form method="post" action="<%=contextPath%>/blog/blog.do"
			enctype="multipart/form-data" onsubmit="return validateBlogOwner(this);">
			<input type="hidden" name="action" value="insert">
			<input type="hidden" name="source" value="farmer">
			<input type="hidden" name="blog_status" value="VISIBLE">
			<input type="hidden" name="blog_type_id" value="1">

			<%
			if (errorMsgs != null && !errorMsgs.isEmpty()) {
			%>
			<ul class="errors">
				<%
				for (String message : errorMsgs) {
				%>
				<li><%=message%></li>
				<%
				}
				%>
			</ul>
			<%
			}
			%>

			<div>
				<label for="blog_title">日誌標題</label>
				<input id="blog_title" name="blog_title"
					value="<%=blogVO.getBlogTitle() == null ? "" : blogVO.getBlogTitle()%>"
					required maxlength="100">
			</div>

			<div class="grid">
				<div>
					<label for="farmer_id">農夫編號</label>
					<select id="farmer_id" name="farmer_id">
						<option value="">-- 請選擇農夫 --</option>
						<%
						for (FarmerVO farmerVO : farmerList) {
							String selected = farmerVO.getFarmerId().equals(blogVO.getFarmerId()) ? "selected" : "";
						%>
						<option value="<%=farmerVO.getFarmerId()%>" <%=selected%>><%=farmerVO.getFarmerId()%> - <%=farmerVO.getFarmName() == null ? "" : farmerVO.getFarmName()%></option>
						<%
						}
						%>
					</select>
				</div>

				<div>
					<label for="product_id">關聯商品編號</label>
					<select id="product_id" name="product_id" required>
						<option value="">-- 請選擇商品 --</option>
						<%
						for (ProductVO productVO : productList) {
							String selected = productVO.getProductId().equals(blogVO.getProductId()) ? "selected" : "";
						%>
						<option value="<%=productVO.getProductId()%>" <%=selected%>><%=productVO.getProductId()%> - <%=productVO.getDescription() == null ? "" : productVO.getDescription()%></option>
						<%
						}
						%>
					</select>
				</div>

				<div>
					<label for="user_id">會員編號</label>
					<select id="user_id" name="user_id">
						<option value="">-- 請選擇會員 --</option>
						<%
						for (UserVO userVO : userList) {
							String selected = userVO.getUserId().equals(blogVO.getUserId()) ? "selected" : "";
						%>
						<option value="<%=userVO.getUserId()%>" <%=selected%>><%=userVO.getUserId()%> - <%=userVO.getUserName() == null ? "" : userVO.getUserName()%></option>
						<%
						}
						%>
					</select>
				</div>
			</div>

			<div>
				<label for="blog_content">日誌內容</label>
				<textarea id="blog_content" name="blog_content" required><%=blogVO.getBlogContent() == null ? "" : blogVO.getBlogContent()%></textarea>
			</div>

			<button class="btn" type="submit">新增日誌</button>
		</form>
	</main>
	<script>
	function validateBlogOwner(form) {
		if (form.user_id.value === "" && form.farmer_id.value === "") {
			alert("\u6703\u54e1\u548c\u8fb2\u592b\u4e0d\u80fd\u540c\u6642\u7a7a\u767d");
			return false;
		}
		return true;
	}
	</script>
</body>
</html>
