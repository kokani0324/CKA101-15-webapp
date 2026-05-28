<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.*"%>
<%@ page import="com.blog.model.BlogVO"%>

<%
String contextPath = request.getContextPath();
BlogVO blogVO = (BlogVO) request.getAttribute("blogVO");
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8">
<title>Blog 詳細資料</title>

<style>
body {
    margin: 0;
    font-family: Arial, "Noto Sans TC", sans-serif;
    background: #f6f7f2;
    color: #253025;
}

main {
    max-width: 900px;
    margin: 40px auto;
    padding: 0 24px;
}

a {
    color: #37683c;
}

article {
    background: #fff;
    border: 1px solid #d9dfd3;
    border-radius: 8px;
    padding: 28px;
}

h1 {
    margin-top: 0;
    margin-bottom: 20px;
}

/* Blog 圖片區 */
.blog-img {
    margin: 20px 0;
    text-align: center;
}

.blog-img img {
    max-width: 100%;
    max-height: 420px;
    border-radius: 8px;
    object-fit: contain;
    border: 1px solid #d9dfd3;
}

/* 文章資訊 */
.meta {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 12px;
    margin: 20px 0;
    color: #53614d;
}

.meta div {
    background: #eef2e9;
    border-radius: 6px;
    padding: 10px;
}

/* 文章內容 */
.content {
    white-space: pre-wrap;
    line-height: 1.7;
    margin-top: 20px;
}

/* 按鈕列 */
.toolbar {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
    margin-top: 24px;
}

.btn {
    border: 0;
    border-radius: 6px;
    padding: 10px 14px;
    background: #3f6f44;
    color: #fff;
    text-decoration: none;
    cursor: pointer;
    display: inline-block;
}

.btn.secondary {
    background: #5f6f52;
}

form {
    margin: 0;
}

.empty {
    background: #fff;
    border: 1px solid #d9dfd3;
    border-radius: 8px;
    padding: 24px;
}
</style>
</head>

<body>
    <main>
        <p>
            <a href="<%=contextPath%>/blog/blog.do?action=getAll">回全部 Blog</a>
        </p>

        <%
        if (blogVO == null) {
        %>

        <div class="empty">沒有可顯示的文章資料。</div>

        <%
        } else {
        %>

        <article>
            <h1><%=blogVO.getBlogTitle()%></h1>

            <%
            if (blogVO.getBlogImg() != null) {
            %>
            <div class="blog-img">
                <img
                    src="<%=contextPath%>/blog/blog.do?action=getImage&blog_id=<%=blogVO.getBlogId()%>"
                    alt="Blog 圖片">
            </div>
            <%
            }
            %>

            <div class="meta">
                <div>編號：<%=blogVO.getBlogId()%></div>
                <div>會員：<%=blogVO.getUserId()%></div>
                <div>農夫：<%=blogVO.getFarmerId()%></div>
                <div>分類：<%=blogVO.getBlogTypeId()%></div>
                <div>商品：<%=blogVO.getProductId()%></div>
                <div>讚數：<%=blogVO.getBlogLike()%></div>
                <div>狀態：<%=blogVO.getBlogStatus()%></div>
                <div>
                    時間：<%=blogVO.getBlogTime() == null ? "" : dateFormat.format(blogVO.getBlogTime())%>
                </div>
            </div>

            <div class="content"><%=blogVO.getBlogContent()%></div>

            <div class="toolbar">
                <form method="post" action="<%=contextPath%>/blog/blog.do">
                    <input type="hidden" name="action" value="getOne_For_Update">
                    <input type="hidden" name="blog_id" value="<%=blogVO.getBlogId()%>">
                    <button class="btn" type="submit">修改文章</button>
                </form>

                <a class="btn secondary" href="<%=contextPath%>/frontend/blog/select_page.jsp">
                    回查詢頁
                </a>
            </div>
        </article>

        <%
        }
        %>
    </main>
</body>
</html>