<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.blog.model.*" %>
<%
    String contextPath = request.getContextPath();
    List<BlogVO> blogList = (List<BlogVO>) request.getAttribute("blogList");
    if (blogList == null) {
        blogList = new BlogService().getAll();
    }
    String farmerIdParam = request.getParameter("farmer_id");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>我的農場日誌</title>
    <style>
        body { margin: 0; font-family: Arial, "Noto Sans TC", sans-serif; background: #f6f7f2; color: #253025; }
        main { max-width: 1080px; margin: 40px auto; padding: 0 24px; }
        a { color: #37683c; }
        .toolbar { display: flex; gap: 12px; flex-wrap: wrap; align-items: end; margin: 20px 0; }
        .toolbar form { display: flex; gap: 10px; align-items: end; flex-wrap: wrap; }
        label { display: block; margin-bottom: 6px; font-weight: 700; }
        input { padding: 9px 12px; border: 1px solid #bfc8b8; border-radius: 6px; font-size: 15px; }
        .btn { border: 0; border-radius: 6px; padding: 10px 14px; background: #3f6f44; color: #fff; text-decoration: none; cursor: pointer; }
        .btn.secondary { background: #5f6f52; }
        .list { display: grid; gap: 14px; }
        article { background: #fff; border: 1px solid #d9dfd3; border-radius: 8px; padding: 18px; }
        h2 { margin: 0 0 10px; }
        .meta { color: #5f6f52; margin-bottom: 12px; }
        .content { white-space: pre-wrap; line-height: 1.7; }
        .actions { margin-top: 14px; display: flex; gap: 8px; flex-wrap: wrap; }
        .actions form { margin: 0; }
        .empty { background: #fff; border: 1px solid #d9dfd3; border-radius: 8px; padding: 24px; }
    </style>
</head>
<body>
<main>
    <p><a href="<%= contextPath %>/frontend/blog/select_page.jsp">回 Blog 管理首頁</a></p>
    <h1>我的農場日誌</h1>

    <div class="toolbar">
        <a class="btn" href="<%= contextPath %>/farmer/blog/addFarmDiary.jsp">新增農場日誌</a>
        <form method="get" action="<%= contextPath %>/farmer/blog/MyBloglist.jsp">
            <div>
                <label for="farmer_id">農夫編號篩選</label>
                <input id="farmer_id" type="number" name="farmer_id" value="<%= farmerIdParam == null ? "" : farmerIdParam %>" min="1">
            </div>
            <button class="btn secondary" type="submit">篩選</button>
        </form>
    </div>

    <div class="list">
        <%
            boolean hasBlog = false;
            for (BlogVO blogVO : blogList) {
                if (farmerIdParam != null && farmerIdParam.trim().length() > 0
                        && !farmerIdParam.equals(String.valueOf(blogVO.getFarmerId()))) {
                    continue;
                }
                hasBlog = true;
        %>
            <article>
                <h2><%= blogVO.getBlogTitle() %></h2>
                <div class="meta">
                    編號 <%= blogVO.getBlogId() %> |
                    農夫 <%= blogVO.getFarmerId() %> |
                    商品 <%= blogVO.getProductId() %> |
                    <%= blogVO.getBlogTime() == null ? "" : dateFormat.format(blogVO.getBlogTime()) %> |
                    <%= blogVO.getBlogStatus() %>
                </div>
                <div class="content"><%= blogVO.getBlogContent() %></div>
                <div class="actions">
                    <form method="post" action="<%= contextPath %>/blog/blog.do">
                        <input type="hidden" name="action" value="getOne_For_Update">
                        <input type="hidden" name="blog_id" value="<%= blogVO.getBlogId() %>">
                        <button class="btn" type="submit">修改</button>
                    </form>
                    <form method="post" action="<%= contextPath %>/blog/blog.do">
                        <input type="hidden" name="action" value="getOne_For_Display">
                        <input type="hidden" name="blog_id" value="<%= blogVO.getBlogId() %>">
                        <button class="btn secondary" type="submit">查看</button>
                    </form>
                </div>
            </article>
        <% } %>
    </div>

    <% if (!hasBlog) { %>
        <div class="empty">目前沒有符合條件的農場日誌。</div>
    <% } %>
</main>
</body>
</html>
