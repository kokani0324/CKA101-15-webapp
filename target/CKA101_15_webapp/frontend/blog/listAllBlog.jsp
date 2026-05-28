<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

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
        @media (max-width: 900px) {
            table, thead, tbody, tr, th, td { display: block; }
            thead { display: none; }
            tr { border-bottom: 1px solid #d9dfd3; padding: 10px 0; }
            td { border: 0; padding: 8px 12px; }
            td::before { content: attr(data-label); display: block; font-weight: 700; color: #5f6f52; margin-bottom: 4px; }
        }
    </style>
</head>
<body>
<main>
    <p><a href="${pageContext.request.contextPath}/frontend/blog/select_page.jsp">回 Blog 管理首頁</a></p>
    <h1>全部 Blog</h1>

    <div class="toolbar">
        <a class="btn" href="${pageContext.request.contextPath}/frontend/blog/addBlog.jsp">新增 Blog</a>
        <a class="btn secondary" href="${pageContext.request.contextPath}/blog/blog.do?action=getAll">重新整理</a>
    </div>

    <c:if test="${not empty errorMsgs}">
        <ul class="errors">
            <c:forEach var="message" items="${errorMsgs}">
                <li><c:out value="${message}" /></li>
            </c:forEach>
        </ul>
    </c:if>

    <c:choose>
        <c:when test="${empty blogList}">
            <div class="empty">目前沒有文章資料。</div>
        </c:when>
        <c:otherwise>
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
                <c:forEach var="blogVO" items="${blogList}">
                    <tr>
                        <td data-label="編號"><c:out value="${blogVO.blogId}" /></td>
                        <td data-label="標題"><c:out value="${blogVO.blogTitle}" /></td>
                        <td data-label="會員"><c:out value="${blogVO.userId}" /></td>
                        <td data-label="農夫"><c:out value="${blogVO.farmerId}" /></td>
                        <td data-label="分類"><c:out value="${blogVO.blogTypeId}" /></td>
                        <td data-label="商品"><c:out value="${blogVO.productId}" /></td>
                        <td data-label="讚數"><c:out value="${blogVO.blogLike}" /></td>
                        <td data-label="時間"><fmt:formatDate value="${blogVO.blogTime}" pattern="yyyy-MM-dd HH:mm" /></td>
                        <td data-label="狀態"><c:out value="${blogVO.blogStatus}" /></td>
                        <td data-label="操作">
                            <div class="actions">
                                <form method="post" action="${pageContext.request.contextPath}/blog/blog.do">
                                    <input type="hidden" name="action" value="getOne_For_Display">
                                    <input type="hidden" name="blog_id" value="${blogVO.blogId}">
                                    <button class="btn secondary" type="submit">查看</button>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/blog/blog.do">
                                    <input type="hidden" name="action" value="getOne_For_Update">
                                    <input type="hidden" name="blog_id" value="${blogVO.blogId}">
                                    <button class="btn" type="submit">修改</button>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/blog/blog.do" onsubmit="return confirm('確定要刪除此文章嗎？');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="blog_id" value="${blogVO.blogId}">
                                    <button class="btn danger" type="submit">刪除</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
