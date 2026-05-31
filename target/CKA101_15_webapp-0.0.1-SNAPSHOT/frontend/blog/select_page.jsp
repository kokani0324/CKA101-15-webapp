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
        body { margin: 0; font-family: Arial, "Noto Sans TC", sans-serif; background: linear-gradient(180deg, #f6f7f2 0%, #eef3ea 100%); color: #253025; min-height: 100vh; }
        main { max-width: 960px; margin: 48px auto; padding: 0 24px; }
        h1 { margin-bottom: 8px; font-size: 42px; letter-spacing: 0; }
        .toolbar { display: flex; gap: 12px; flex-wrap: wrap; margin: 24px 0 36px; }
        .btn { border: 0; border-radius: 6px; padding: 10px 16px; background: #3f6f44; color: #fff; text-decoration: none; cursor: pointer; transition: background-color .18s ease, transform .18s ease, box-shadow .18s ease; box-shadow: 0 4px 12px rgba(37, 48, 37, 0.12); }
        .btn:hover { background: #315936; transform: translateY(-1px); box-shadow: 0 8px 18px rgba(37, 48, 37, 0.16); }
        .btn.secondary { background: #5f6f52; }
        .btn.secondary:hover { background: #4e5d43; }
        .panel { position: relative; overflow: hidden; background: #fff; border: 1px solid #d9dfd3; border-radius: 8px; padding: 28px 24px 24px; box-shadow: 0 10px 28px rgba(37, 48, 37, 0.08); }
        .panel:before { content: ""; position: absolute; top: 0; left: 0; right: 0; height: 5px; background: linear-gradient(90deg, #3f6f44, #9eb58e); }
        .errors { margin: 0 0 18px; padding: 12px 16px; border-radius: 6px; background: #ffe9e7; color: #9a2c23; }
        label { display: block; margin-bottom: 8px; font-weight: 700; }
        input { width: 100%; box-sizing: border-box; padding: 10px 12px; border: 1px solid #bfc8b8; border-radius: 6px; font-size: 16px; transition: border-color .18s ease, box-shadow .18s ease; }
        input:focus { outline: none; border-color: #3f6f44; box-shadow: 0 0 0 3px rgba(63, 111, 68, 0.14); }
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
        <a class="btn secondary" href="<%= contextPath %>/farmer/blog/addFarmDiary.jsp">新增農場日記</a>
        <a class="btn secondary" href="<%= contextPath %>/farmer/blog/MyBloglist.jsp">查看我的農場日記</a>
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
                <input id="blog_id" type="number" name="blog_id" min="1" value="<%= blogId %>" required placeholder="9001">
            </div>
            <button class="btn" type="submit">送出查詢</button>
        </form>
    </section>
</main>
</body>
</html>
