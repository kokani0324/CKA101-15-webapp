<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>Blog 詳細資料</title>
    <style>
        body { margin: 0; font-family: Arial, "Noto Sans TC", sans-serif; background: #f6f7f2; color: #253025; }
        main { max-width: 900px; margin: 40px auto; padding: 0 24px; }
        a { color: #37683c; }
        article { background: #fff; border: 1px solid #d9dfd3; border-radius: 8px; padding: 28px; }
        .meta { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; margin: 20px 0; color: #53614d; }
        .meta div { background: #eef2e9; border-radius: 6px; padding: 10px; }
        .content { white-space: pre-wrap; line-height: 1.7; }
        .toolbar { display: flex; gap: 12px; flex-wrap: wrap; margin-top: 24px; }
        .btn { border: 0; border-radius: 6px; padding: 10px 14px; background: #3f6f44; color: #fff; text-decoration: none; cursor: pointer; }
        .btn.secondary { background: #5f6f52; }
        form { margin: 0; }
        .empty { background: #fff; border: 1px solid #d9dfd3; border-radius: 8px; padding: 24px; }
        @media (max-width: 720px) { .meta { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<main>
    <p><a href="${pageContext.request.contextPath}/blog/blog.do?action=getAll">回全部 Blog</a></p>

    <c:choose>
        <c:when test="${empty blogVO}">
            <div class="empty">沒有可顯示的文章資料。</div>
        </c:when>
        <c:otherwise>
            <article>
                <h1><c:out value="${blogVO.blogTitle}" /></h1>
                <div class="meta">
                    <div>編號：<c:out value="${blogVO.blogId}" /></div>
                    <div>會員：<c:out value="${blogVO.userId}" /></div>
                    <div>農夫：<c:out value="${blogVO.farmerId}" /></div>
                    <div>分類：<c:out value="${blogVO.blogTypeId}" /></div>
                    <div>商品：<c:out value="${blogVO.productId}" /></div>
                    <div>讚數：<c:out value="${blogVO.blogLike}" /></div>
                    <div>狀態：<c:out value="${blogVO.blogStatus}" /></div>
                    <div>時間：<fmt:formatDate value="${blogVO.blogTime}" pattern="yyyy-MM-dd HH:mm" /></div>
                </div>
                <div class="content"><c:out value="${blogVO.blogContent}" /></div>

                <div class="toolbar">
                    <form method="post" action="${pageContext.request.contextPath}/blog/blog.do">
                        <input type="hidden" name="action" value="getOne_For_Update">
                        <input type="hidden" name="blog_id" value="${blogVO.blogId}">
                        <button class="btn" type="submit">修改文章</button>
                    </form>
                    <a class="btn secondary" href="${pageContext.request.contextPath}/frontend/blog/select_page.jsp">回查詢頁</a>
                </div>
            </article>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
