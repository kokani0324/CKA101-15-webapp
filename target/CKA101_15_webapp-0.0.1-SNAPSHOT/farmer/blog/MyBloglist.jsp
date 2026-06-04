<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.blog.model.*"%>
<%
String contextPath = request.getContextPath();
List<BlogVO> blogList = (List<BlogVO>) request.getAttribute("blogList");
if (blogList == null) {
	blogList = new BlogService().getAll();
}

String farmerIdParam = request.getParameter("farmer_id");
String farmerIdValue = farmerIdParam == null ? "" : farmerIdParam.trim();
List<BlogVO> list = new ArrayList<BlogVO>();
for (BlogVO blogVO : blogList) {
	if (farmerIdValue.length() > 0 && !farmerIdValue.equals(String.valueOf(blogVO.getFarmerId()))) {
		continue;
	}
	list.add(blogVO);
}

DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
String pageUrl = contextPath + "/farmer/blog/MyBloglist.jsp";
String pageParamJoin = "?";
if (farmerIdValue.length() > 0) {
	pageUrl += "?farmer_id=" + farmerIdValue;
	pageParamJoin = "&";
}
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8">
<title>我的農場日誌</title>
<style>
body {
	margin: 0;
	font-family: Arial, "Noto Sans TC", sans-serif;
	background: linear-gradient(180deg, #f6f7f2 0%, #eef3ea 100%);
	color: #253025;
	min-height: 100vh;
}

main {
	max-width: 1180px;
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
	align-items: end;
	margin: 20px 0;
}

.toolbar form {
	display: flex;
	gap: 10px;
	align-items: end;
	flex-wrap: wrap;
}

label {
	display: block;
	margin-bottom: 6px;
	font-weight: 700;
}

input {
	padding: 9px 12px;
	border: 1px solid #bfc8b8;
	border-radius: 6px;
	font-size: 15px;
	transition: border-color .18s ease, box-shadow .18s ease;
}

input:focus {
	outline: none;
	border-color: #3f6f44;
	box-shadow: 0 0 0 3px rgba(63, 111, 68, 0.14);
}

.btn {
	border: 0;
	border-radius: 6px;
	padding: 8px 12px;
	background: #3f6f44;
	color: #fff;
	text-decoration: none;
	cursor: pointer;
	font-size: 14px;
	transition: background-color .18s ease, transform .18s ease, box-shadow .18s ease;
	box-shadow: 0 4px 12px rgba(37, 48, 37, 0.12);
}

.btn:hover {
	background: #315936;
	transform: translateY(-1px);
	box-shadow: 0 8px 18px rgba(37, 48, 37, 0.16);
}

.btn.secondary {
	background: #5f6f52;
}

.btn.secondary:hover {
	background: #4e5d43;
}

table {
	width: 100%;
	border-collapse: collapse;
	background: #fff;
	border: 1px solid #d9dfd3;
	box-shadow: 0 8px 22px rgba(37, 48, 37, 0.07);
}

th, td {
	border-bottom: 1px solid #e3e7de;
	padding: 12px;
	text-align: left;
	vertical-align: top;
}

th {
	background: #eef2e9;
	white-space: nowrap;
}

.actions {
	display: flex;
	gap: 8px;
	flex-wrap: wrap;
}

.actions form {
	margin: 0;
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
.btn.danger {
	background: #a33a32;
}

.btn.danger:hover {
	background: #872f29;
}
</style>
</head>
<body>
	<main>
		<p>
			<a href="<%=contextPath%>/frontend/blog/select_page.jsp">回 Blog 管理首頁</a>
		</p>
		<h1>我的農場日誌</h1>

		<div class="toolbar">
			<a class="btn" href="<%=contextPath%>/farmer/blog/addFarmDiary.jsp">新增農場日誌</a>
			<form method="get" action="<%=contextPath%>/farmer/blog/MyBloglist.jsp">
				<div>
					<label for="farmer_id">農夫編號篩選</label>
					<input id="farmer_id" type="number" name="farmer_id"
						value="<%=farmerIdValue%>" min="1">
				</div>
				<button class="btn secondary" type="submit">篩選</button>
			</form>
		</div>

		<%
		if (list.isEmpty()) {
		%>
		<div class="empty">目前沒有符合條件的農場日誌。</div>
		<%
		} else {
		%>
		<%@ include file="/frontend/blog/page1.file"%>
		<table>
			<thead>
				<tr>
					<th>編號</th>
					<th>標題</th>
					<th>會員</th>
					<th>農夫</th>
					<th>商品</th>
					<th>讚數</th>
					<th>時間</th>
					<th>狀態</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<%
				for (int i = pageIndex; i < pageIndex + rowsPerPage && i < list.size(); i++) {
					BlogVO blogVO = list.get(i);
				%>
				<tr>
					<td><%=blogVO.getBlogId()%></td>
					<td><%=blogVO.getBlogTitle()%></td>
					<td><%=blogVO.getUserId() == null ? "" : blogVO.getUserId()%></td>
					<td><%=blogVO.getFarmerId() == null ? "" : blogVO.getFarmerId()%></td>
					<td><%=blogVO.getProductId()%></td>
					<td><%=blogVO.getBlogLike()%></td>
					<td><%=blogVO.getBlogTime() == null ? "" : dateFormat.format(blogVO.getBlogTime())%></td>
					<td><%=blogVO.getBlogStatus()%></td>
					<td>
						<div class="actions">
							<form method="post" action="<%=contextPath%>/blog/blog.do">
								<input type="hidden" name="action" value="getOne_For_Update">
								<input type="hidden" name="blog_id" value="<%=blogVO.getBlogId()%>">
								<button class="btn" type="submit">修改</button>
							</form>
							<form method="post" action="<%=contextPath%>/blog/blog.do">
								<input type="hidden" name="action" value="getOne_For_Display">
								<input type="hidden" name="blog_id" value="<%=blogVO.getBlogId()%>">
								<button class="btn secondary" type="submit">查看</button>
							</form>
							<form method="post" action="<%=contextPath%>/blog/blog.do"
							      onsubmit="return confirm('確定要刪除這篇文章嗎？');">
								<input type="hidden" name="action" value="delete">
								<input type="hidden" name="blog_id" value="<%=blogVO.getBlogId()%>">
								<button class="btn danger" type="submit">刪除</button>
							</form>
						</div>
					</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>

		<div class="pager">
			<%
			if (rowsPerPage < rowNumber) {
				if (pageIndex >= rowsPerPage) {
			%>
			<a href="<%=pageUrl%><%=pageParamJoin%>whichPage=1">至第一頁</a>
			<a href="<%=pageUrl%><%=pageParamJoin%>whichPage=<%=whichPage - 1%>">上一頁</a>
			<%
				}
				if (pageIndex < pageIndexArray[pageNumber - 1]) {
			%>
			<a href="<%=pageUrl%><%=pageParamJoin%>whichPage=<%=whichPage + 1%>">下一頁</a>
			<a href="<%=pageUrl%><%=pageParamJoin%>whichPage=<%=pageNumber%>">至最後一頁</a>
			<%
				}
			}
			if (pageNumber > 1) {
			%>
			<form method="get" action="<%=contextPath%>/farmer/blog/MyBloglist.jsp">
				<%
				if (farmerIdValue.length() > 0) {
				%>
				<input type="hidden" name="farmer_id" value="<%=farmerIdValue%>">
				<%
				}
				%>
				<select size="1" name="whichPage">
					<%
					for (int i = 1; i <= pageNumber; i++) {
					%>
					<option value="<%=i%>" <%=i == whichPage ? "selected" : ""%>>跳至第 <%=i%> 頁</option>
					<%
					}
					%>
				</select>
				<input type="submit" value="送出">
			</form>
			<%
			}
			%>
		</div>
		<%
		}
		%>
	</main>
</body>
</html>
