<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.blog.model.BlogService" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
    if (request.getAttribute("blogList") == null) {
        request.setAttribute("blogList", new BlogService().getAll());
    }
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
    <p><a href="${pageContext.request.contextPath}/frontend/blog/select_page.jsp">回 Blog 管理首頁</a></p>
    <h1>我的農場日誌</h1>

    <div class="toolbar">
        <a class="btn" href="${pageContext.request.contextPath}/farmer/blog/addFarmDiary.jsp">新增農場日誌</a>
        <form method="get" action="${pageContext.request.contextPath}/farmer/blog/MyBloglist.jsp">
            <div>
                <label for="farmer_id">農夫編號篩選</label>
                <input id="farmer_id" type="number" name="farmer_id" value="${param.farmer_id}" min="1">
            </div>
            <button class="btn secondary" type="submit">篩選</button>
        </form>
    </div>

    <c:set var="hasBlog" value="${false}" />
    <div class="list">
        <c:forEach var="blogVO" items="${blogList}">
            <c:if test="${empty param.farmer_id || blogVO.farmerId == param.farmer_id}">
                <c:set var="hasBlog" value="${true}" />
                <article>
                    <h2><c:out value="${blogVO.blogTitle}" /></h2>
                    <div class="meta">
                        編號 <c:out value="${blogVO.blogId}" /> |
                        農夫 <c:out value="${blogVO.farmerId}" /> |
                        商品 <c:out value="${blogVO.productId}" /> |
                        <fmt:formatDate value="${blogVO.blogTime}" pattern="yyyy-MM-dd HH:mm" /> |
                        <c:out value="${blogVO.blogStatus}" />
                    </div>
                    <div class="content"><c:out value="${blogVO.blogContent}" /></div>
                    <div class="actions">
                        <form method="post" action="${pageContext.request.contextPath}/blog/blog.do">
                            <input type="hidden" name="action" value="getOne_For_Update">
                            <input type="hidden" name="blog_id" value="${blogVO.blogId}">
                            <button class="btn" type="submit">修改</button>
                        </form>
                        <form method="post" action="${pageContext.request.contextPath}/blog/blog.do">
                            <input type="hidden" name="action" value="getOne_For_Display">
                            <input type="hidden" name="blog_id" value="${blogVO.blogId}">
                            <button class="btn secondary" type="submit">查看</button>
                        </form>
                    </div>
                </article>
            </c:if>
        </c:forEach>
    </div>

    <c:if test="${not hasBlog}">
        <div class="empty">目前沒有符合條件的農場日誌。</div>
    </c:if>
</main>
</body>
</html>
