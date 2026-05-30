<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.blog.model.BlogVO" %>
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
        body { margin: 0; font-family: Arial, "Noto Sans TC", sans-serif; background: #f6f7f2; color: #253025; }
        main { max-width: 1180px; margin: 40px auto; padding: 0 24px; }
        a { color: #37683c; }
        .toolbar { display: flex; gap: 12px; flex-wrap: wrap; align-items: center; margin: 20px 0; }
        .btn { border: 0; border-radius: 6px; padding: 8px 12px; background: #3f6f44; color: #fff; text-decoration: none; cursor: pointer; font-size: 14px; }
        .btn.danger { background: #a33a32; }
        .btn.secondary { background: #5f6f52; }
        table { width: 100%; border-collapse: collapse; background: #fff; border: 1px solid #d9dfd3; }
        th, td { border-bottom: 1px solid #e3e7de; padding: 12px; text-align: left; vertical-align: top; }
        th { background: #eef2e9; white-space: nowrap; }
        .actions { display: flex; gap: 8px; flex-wrap: wrap; }
        .actions form { margin: 0; }
        .errors { margin: 0 0 18px; padding: 12px 16px; border-radius: 6px; background: #ffe9e7; color: #9a2c23; }
        .empty { background: #fff; border: 1px solid #d9dfd3; border-radius: 8px; padding: 24px; }
        .page-summary { display: flex; gap: 16px; align-items: center; margin: 12px 0; color: #40513b; }
        .pager { display: flex; gap: 10px; align-items: center; flex-wrap: wrap; margin: 16px 0; }
        .pager a { padding: 6px 10px; border: 1px solid #bfc8b8; border-radius: 6px; background: #fff; text-decoration: none; }
        .pager form { display: flex; gap: 8px; align-items: center; margin: 0; }
        .pager select, .pager input { padding: 6px 8px; border: 1px solid #bfc8b8; border-radius: 6px; }
    </style>
</head>
<body>
<main>
    <p><a href="<%= contextPath %>/frontend/blog/select_page.jsp">回 Blog 管理首頁</a></p>
    <h1>全部 Blog</h1>

    <div class="toolbar">
        <a class="btn" href="<%= contextPath %>/frontend/blog/addBlog.jsp">新增 Blog</a>
        <a class="btn secondary" href="<%= contextPath %>/blog/blog.do?action=getAll">重新整理</a>
    </div>

    <% if (errorMsgs != null && !errorMsgs.isEmpty()) { %>
        <ul class="errors">
            <% for (String message : errorMsgs) { %>
                <li><%= message %></li>
            <% } %>
        </ul>
    <% } %>

    <% if (list.isEmpty()) { %>
        <div class="empty">目前沒有文章資料。</div>
    <% } else { %>
        <%@ include file="page1.file" %>
        <table>
            <thead>
            <tr>
                <th>編號</th>
                <th>標題</th>
                <th>會員</th>
                <th>農夫</th>
                <th>分類</th>
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
                    <td><%= blogVO.getBlogId() %></td>
                    <td><%= blogVO.getBlogTitle() %></td>
                    <td><%= blogVO.getUserId() %></td>
                    <td><%= blogVO.getFarmerId() %></td>
                    <td><%= blogVO.getBlogTypeId() %></td>
                    <td><%= blogVO.getProductId() %></td>
                    <td><%= blogVO.getBlogLike() %></td>
                    <td><%= blogVO.getBlogTime() == null ? "" : dateFormat.format(blogVO.getBlogTime()) %></td>
                    <td><%= blogVO.getBlogStatus() %></td>
                    <td>
                        <div class="actions">
                            <form method="post" action="<%= contextPath %>/blog/blog.do">
                                <input type="hidden" name="action" value="getOne_For_Display">
                                <input type="hidden" name="blog_id" value="<%= blogVO.getBlogId() %>">
                                <button class="btn secondary" type="submit">查看</button>
                            </form>
                            <form method="post" action="<%= contextPath %>/blog/blog.do">
                                <input type="hidden" name="action" value="getOne_For_Update">
                                <input type="hidden" name="blog_id" value="<%= blogVO.getBlogId() %>">
                                <button class="btn" type="submit">修改</button>
                            </form>
                            <form method="post" action="<%= contextPath %>/blog/blog.do" onsubmit="return confirm('確定要刪除此文章嗎？');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="blog_id" value="<%= blogVO.getBlogId() %>">
                                <button class="btn danger" type="submit">刪除</button>
                            </form>
                        </div>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
        <%@ include file="page2.file" %>
    <% } %>
</main>
</body>
</html>
