<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>新增 Blog</title>
    <style>
        body { margin: 0; font-family: Arial, "Noto Sans TC", sans-serif; background: #f6f7f2; color: #253025; }
        main { max-width: 960px; margin: 40px auto; padding: 0 24px; }
        a { color: #37683c; }
        form { display: grid; gap: 16px; background: #fff; border: 1px solid #d9dfd3; border-radius: 8px; padding: 24px; }
        .grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 16px; }
        label { display: block; margin-bottom: 8px; font-weight: 700; }
        input, select, textarea { width: 100%; box-sizing: border-box; padding: 10px 12px; border: 1px solid #bfc8b8; border-radius: 6px; font-size: 16px; }
        textarea { min-height: 180px; resize: vertical; }
        .btn { border: 0; border-radius: 6px; padding: 10px 16px; background: #3f6f44; color: #fff; cursor: pointer; }
        .errors { margin: 0; padding: 12px 16px; border-radius: 6px; background: #ffe9e7; color: #9a2c23; }
        @media (max-width: 720px) { .grid { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<main>
    <p><a href="${pageContext.request.contextPath}/frontend/blog/select_page.jsp">回 Blog 管理首頁</a></p>
    <h1>新增 Blog</h1>

    <form method="post" action="${pageContext.request.contextPath}/blog/blog.do">
        <input type="hidden" name="action" value="insert">

        <c:if test="${not empty errorMsgs}">
            <ul class="errors">
                <c:forEach var="message" items="${errorMsgs}">
                    <li><c:out value="${message}" /></li>
                </c:forEach>
            </ul>
        </c:if>

        <div>
            <label for="blog_title">文章標題</label>
            <input id="blog_title" name="blog_title" value="${blogVO.blogTitle}" required maxlength="100">
        </div>

        <div class="grid">
            <div>
                <label for="user_id">會員編號</label>
                <input id="user_id" type="number" name="user_id" value="${blogVO.userId}" min="1" required>
            </div>
            <div>
                <label for="farmer_id">農夫編號</label>
                <input id="farmer_id" type="number" name="farmer_id" value="${blogVO.farmerId}" min="1" required>
            </div>
            <div>
                <label for="blog_type_id">文章分類編號</label>
                <input id="blog_type_id" type="number" name="blog_type_id" value="${blogVO.blogTypeId}" min="1" required>
            </div>
            <div>
                <label for="product_id">商品編號</label>
                <input id="product_id" type="number" name="product_id" value="${blogVO.productId}" min="1" required>
            </div>
        </div>

        <div>
            <label for="blog_status">文章狀態</label>
            <select id="blog_status" name="blog_status" required>
                <option value="上架" ${blogVO.blogStatus == '上架' ? 'selected' : ''}>上架</option>
                <option value="下架" ${blogVO.blogStatus == '下架' ? 'selected' : ''}>下架</option>
                <option value="草稿" ${blogVO.blogStatus == '草稿' ? 'selected' : ''}>草稿</option>
            </select>
        </div>

        <div>
            <label for="blog_content">文章內容</label>
            <textarea id="blog_content" name="blog_content" required>${blogVO.blogContent}</textarea>
        </div>

        <button class="btn" type="submit">新增文章</button>
    </form>
</main>
</body>
</html>
