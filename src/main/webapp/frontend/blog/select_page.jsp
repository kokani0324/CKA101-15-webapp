<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%
    String contextPath = request.getContextPath();
    List<String> errorMsgs = (List<String>) request.getAttribute("errorMsgs");
    String blogId = request.getAttribute("blogId") == null ? "" : String.valueOf(request.getAttribute("blogId"));
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>Blog 管理首頁</title>
    <style>
        body { margin: 0; font-family: Arial, "Noto Sans TC", sans-serif; background: #f6f7f2; color: #253025; }
        main { max-width: 960px; margin: 48px auto; padding: 0 24px; }
        h1 { margin-bottom: 8px; }
        .toolbar { display: flex; gap: 12px; flex-wrap: wrap; margin: 24px 0; }
        .btn { border: 0; border-radius: 6px; padding: 10px 16px; background: #3f6f44; color: #fff; text-decoration: none; cursor: pointer; }
        .btn.secondary { background: #5f6f52; }
        .panel { background: #fff; border: 1px solid #d9dfd3; border-radius: 8px; padding: 24px; }
        .errors { margin: 0 0 18px; padding: 12px 16px; border-radius: 6px; background: #ffe9e7; color: #9a2c23; }
        label { display: block; margin-bottom: 8px; font-weight: 700; }
        input { width: 100%; box-sizing: border-box; padding: 10px 12px; border: 1px solid #bfc8b8; border-radius: 6px; font-size: 16px; }
        form { display: grid; gap: 14px; max-width: 420px; }
    </style>
</head>
<body>
<main>
    <h1>Blog 管理首頁</h1>
    <p>可新增文章、查看全部文章，或用文章編號查詢單筆資料。</p>

    <div class="toolbar">
        <a class="btn" href="<%= contextPath %>/frontend/blog/addBlog.jsp">新增 Blog</a>
        <a class="btn secondary" href="<%= contextPath %>/blog/blog.do?action=getAll">查看全部 Blog</a>
        <a class="btn secondary" href="<%= contextPath %>/farmer/blog/addFarmDiary.jsp">新增農場日誌</a>
    </div>

    <section class="panel">
        <% if (errorMsgs != null && !errorMsgs.isEmpty()) { %>
            <ul class="errors">
                <% for (String message : errorMsgs) { %>
                    <li><%= message %></li>
                <% } %>
            </ul>
        <% } %>

        <h2>查詢單筆 Blog(9000~)</h2>
        <form method="post" action="<%= contextPath %>/blog/blog.do">
            <input type="hidden" name="action" value="getOne_For_Display">
            <div>
                <label for="blog_id">Blog 編號</label>
                <input id="blog_id" type="number" name="blog_id" min="1" value="<%= blogId %>" required>
            </div>
            <button class="btn" type="submit">送出查詢</button>
        </form>
    </section>
</main>
</body>
</html>
