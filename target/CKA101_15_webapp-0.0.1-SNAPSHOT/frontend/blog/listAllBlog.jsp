<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.blog.model.BlogVO"%>
<%
String contextPath = request.getContextPath();
List<String> errorMsgs = (List<String>) request.getAttribute("errorMsgs");
List<BlogVO> blogList = (List<BlogVO>) request.getAttribute("blogList");
List<BlogVO> list = blogList == null ? new ArrayList<BlogVO>() : blogList;
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8">
<title>全部 Blog</title>
<style>
body {
	margin: 0;
	font-family: Arial, "Noto Sans TC", sans-serif;
	background: linear-gradient(180deg, #f6f7f2 0%, #eef3ea 100%);
	color: #253025;
	min-height: 100vh;
}

main {
	max-width: 1080px;
	margin: 40px auto;
	padding: 0 24px;
}

a {
	color: #37683c;
}

.toolbar {
	display: flex;
	gap: 12px;
	flex-wrap: wrap;
	align-items: center;
	margin: 20px 0;
}

.btn {
	border: 0;
	border-radius: 6px;
	padding: 10px 14px;
	background: #3f6f44;
	color: #fff;
	text-decoration: none;
	cursor: pointer;
	font-size: 15px;
	transition: background-color .18s ease, transform .18s ease, box-shadow .18s ease;
	box-shadow: 0 4px 12px rgba(37, 48, 37, 0.12);
}

.btn:hover {
	background: #315936;
	transform: translateY(-1px);
	box-shadow: 0 8px 18px rgba(37, 48, 37, 0.16);
}

.btn.danger {
	background: #a33a32;
}

.btn.danger:hover {
	background: #872f29;
}

.btn.secondary {
	background: #5f6f52;
}

.btn.secondary:hover {
	background: #4e5d43;
}

.list {
	display: grid;
	gap: 14px;
}

article {
	position: relative;
	overflow: hidden;
	background: #fff;
	border: 1px solid #d9dfd3;
	border-radius: 8px;
	padding: 22px 18px 18px;
	box-shadow: 0 8px 22px rgba(37, 48, 37, 0.07);
}

article:before {
	content: "";
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	height: 4px;
	background: linear-gradient(90deg, #3f6f44, #9eb58e);
}

h2 {
	margin: 0 0 10px;
}

.meta {
	color: #5f6f52;
	margin-bottom: 12px;
	line-height: 1.7;
}

.content {
	white-space: pre-wrap;
	line-height: 1.7;
}

.actions {
	margin-top: 14px;
	display: flex;
	gap: 8px;
	flex-wrap: wrap;
}

.actions form {
	margin: 0;
}

.errors {
	margin: 0 0 18px;
	padding: 12px 16px;
	border-radius: 6px;
	background: #ffe9e7;
	color: #9a2c23;
}

.empty {
	background: #fff;
	border: 1px solid #d9dfd3;
	border-radius: 8px;
	padding: 24px;
	box-shadow: 0 8px 22px rgba(37, 48, 37, 0.07);
}

.page-summary {
	display: flex;
	gap: 16px;
	align-items: center;
	margin: 12px 0;
	color: #40513b;
}

.pager {
	display: flex;
	gap: 10px;
	align-items: center;
	flex-wrap: wrap;
	margin: 16px 0;
}

.pager a {
	padding: 6px 10px;
	border: 1px solid #bfc8b8;
	border-radius: 6px;
	background: #fff;
	text-decoration: none;
	transition: border-color .18s ease, box-shadow .18s ease, transform .18s ease;
}

.pager a:hover {
	border-color: #3f6f44;
	box-shadow: 0 6px 14px rgba(37, 48, 37, 0.12);
	transform: translateY(-1px);
}

.pager form {
	display: flex;
	gap: 8px;
	align-items: center;
	margin: 0;
}

.pager select, .pager input {
	padding: 6px 8px;
	border: 1px solid #bfc8b8;
	border-radius: 6px;
}

.pager select:focus, .pager input:focus {
	outline: none;
	border-color: #3f6f44;
	box-shadow: 0 0 0 3px rgba(63, 111, 68, 0.14);
}
.blog-img {
	display: block;
	max-width: 320px;
	max-height: 220px;
	object-fit: cover;
	border-radius: 6px;
	margin: 12px 0;
}
</style>
</head>
<body>
	<main>
		<p>
			<a href="<%=contextPath%>/frontend/blog/select_page.jsp">回 Blog 管理首頁</a>
		</p>
		<h1>全部 Blog</h1>

		<div class="toolbar">
			<a class="btn" href="<%=contextPath%>/frontend/blog/addBlog.jsp">新增 Blog</a>
			<a class="btn secondary" href="<%=contextPath%>/blog/blog.do?action=getAll">重新整理</a>
		</div>

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

		<%
		if (list.isEmpty()) {
		%>
		<div class="empty">目前沒有文章資料。</div>
		<%
		} else {
		%>
		<%@ include file="page1.file"%>
		<div class="list">
			<%
			for (int i = pageIndex; i < pageIndex + rowsPerPage && i < list.size(); i++) {
				BlogVO blogVO = list.get(i);
			%>
			<article>
				<h2><%=blogVO.getBlogTitle()%></h2>
				<div class="meta">
					編號 <%=blogVO.getBlogId()%> |
					會員 <%=blogVO.getUserId() == null ? "" : blogVO.getUserId()%> |
					農夫 <%=blogVO.getFarmerId() == null ? "" : blogVO.getFarmerId()%> |
					分類 <%=blogVO.getBlogTypeId()%> |
					商品 <%=blogVO.getProductId()%> |
					讚數 <%=blogVO.getBlogLike()%> |
					<%=blogVO.getBlogTime() == null ? "" : dateFormat.format(blogVO.getBlogTime())%> |
					<%=blogVO.getBlogStatus()%>
				</div>
				<% if (blogVO.getBlogImg() != null) { %>

				<img class="blog-img"
				     src="<%=contextPath%>/blog/blog.do?action=getImage&blog_id=<%=blogVO.getBlogId()%>"
<%--		contextPath 專案路徑   action=getImage  拿圖片的意思       blog_id=<%=blogVO.getBlogId()%>   是要取VO 裡的BlogId  			 --%>
				     alt="文章圖片">
				<% } %>
				<div class="content"><%=blogVO.getBlogContent()%></div>
				<div class="actions">
					<form method="post" action="<%=contextPath%>/blog/blog.do">
						<input type="hidden" name="action" value="getOne_For_Display">
						<input type="hidden" name="blog_id" value="<%=blogVO.getBlogId()%>">
						<button class="btn secondary" type="submit">查看</button>
					</form>
					<form method="post" action="<%=contextPath%>/blog/blog.do">
						<input type="hidden" name="action" value="getOne_For_Update">
						<input type="hidden" name="blog_id" value="<%=blogVO.getBlogId()%>">
						<button class="btn" type="submit">修改</button>
					</form>
					<form method="post" action="<%=contextPath%>/blog/blog.do"
						onsubmit="return confirm('確定要刪除這篇文章嗎？');">
						<input type="hidden" name="action" value="delete">
						<input type="hidden" name="blog_id" value="<%=blogVO.getBlogId()%>">
						<button class="btn danger" type="submit">刪除</button>
					</form>
				</div>
			</article>
			<%
			}
			%>
		</div>
		<%@ include file="page2.file"%>
		<%
		}
		%>
	</main>
</body>
</html>
