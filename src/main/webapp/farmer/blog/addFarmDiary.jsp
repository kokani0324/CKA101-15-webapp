<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.blog.model.BlogVO" %>
<%
    String contextPath = request.getContextPath();
    List<String> errorMsgs = (List<String>) request.getAttribute("errorMsgs");
    BlogVO blogVO = (BlogVO) request.getAttribute("blogVO");
    if (blogVO == null) {
        blogVO = new BlogVO();
    }
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/farmily?serverTimezone=Asia/Taipei";
    String userid = "root";
    String passwd = "123456";
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>新增農場日誌</title>
    <style>
        body { margin: 0; font-family: Arial, "Noto Sans TC", sans-serif; background: #f6f7f2; color: #253025; }
        main { max-width: 900px; margin: 40px auto; padding: 0 24px; }
        a { color: #37683c; }
        form { display: grid; gap: 16px; background: #fff; border: 1px solid #d9dfd3; border-radius: 8px; padding: 24px; }
        .grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 16px; }
        label { display: block; margin-bottom: 8px; font-weight: 700; }
        input, select, textarea { width: 100%; box-sizing: border-box; padding: 10px 12px; border: 1px solid #bfc8b8; border-radius: 6px; font-size: 16px; }
        textarea { min-height: 220px; resize: vertical; }
        .btn { border: 0; border-radius: 6px; padding: 10px 16px; background: #3f6f44; color: #fff; cursor: pointer; }
        .errors { margin: 0; padding: 12px 16px; border-radius: 6px; background: #ffe9e7; color: #9a2c23; }
    </style>
</head>
<body>
<main>
    <p><a href="<%= contextPath %>/farmer/blog/MyBloglist.jsp">回我的農場日誌</a></p>
    <h1>新增農場日誌</h1>

    <form method="post" action="<%= contextPath %>/blog/blog.do">
        <input type="hidden" name="action" value="insert">
        <input type="hidden" name="source" value="farmer">
        <input type="hidden" name="blog_status" value="VISIBLE">

        <% if (errorMsgs != null && !errorMsgs.isEmpty()) { %>
            <ul class="errors">
                <% for (String message : errorMsgs) { %>
                    <li><%= message %></li>
                <% } %>
            </ul>
        <% } %>

        <div>
            <label for="blog_title">日誌標題</label>
            <input id="blog_title" name="blog_title" value="<%= blogVO.getBlogTitle() == null ? "" : blogVO.getBlogTitle() %>" required maxlength="100">
        </div>

        <div class="grid">
            <div>
                <label for="farmer_id">農夫編號</label>
                <select id="farmer_id" name="farmer_id">
                    <option value="">-- 請選擇農夫 --</option>
                    <%
                    try {
                        Class.forName(driver);
                        try (Connection con = DriverManager.getConnection(url, userid, passwd);
                             PreparedStatement pstmt = con.prepareStatement(
                                     "SELECT farmer_id, farm_name FROM farmer ORDER BY farmer_id");
                             ResultSet rs = pstmt.executeQuery()) {

                            while (rs.next()) {
                                Integer farmerId = rs.getInt("farmer_id");
                                String farmName = rs.getString("farm_name");
                                String selected = farmerId.equals(blogVO.getFarmerId()) ? "selected" : "";
                    %>
                    <option value="<%= farmerId %>" <%= selected %>><%= farmerId %> - <%= farmName == null ? "" : farmName %></option>
                    <%
                            }
                        }
                    } catch (Exception e) {
                    %>
                    <option value="" disabled>農夫資料讀取失敗</option>
                    <%
                    }
                    %>
                </select>
            </div>
            <div>
                <label for="product_id">關聯商品編號</label>
                <input id="product_id" type="number" name="product_id" value="<%= blogVO.getProductId() == null ? "" : blogVO.getProductId() %>" min="1" required>
            </div>
            <div>
                <label for="user_id">會員編號</label>
                <select id="user_id" name="user_id">
                    <option value="">-- 請選擇會員 --</option>
                    <%
                    try {
                        Class.forName(driver);
                        try (Connection con = DriverManager.getConnection(url, userid, passwd);
                             PreparedStatement pstmt = con.prepareStatement(
                                     "SELECT user_id, user_name, user_nickname FROM `user` ORDER BY user_id");
                             ResultSet rs = pstmt.executeQuery()) {

                            while (rs.next()) {
                                Integer userId = rs.getInt("user_id");
                                String userName = rs.getString("user_name");
                                String userNickname = rs.getString("user_nickname");
                                String showName = userName != null && userName.trim().length() > 0 ? userName : userNickname;
                                String selected = userId.equals(blogVO.getUserId()) ? "selected" : "";
                    %>
                    <option value="<%= userId %>" <%= selected %>><%= userId %> - <%= showName == null ? "" : showName %></option>
                    <%
                            }
                        }
                    } catch (Exception e) {
                    %>
                    <option value="" disabled>會員資料讀取失敗</option>
                    <%
                    }
                    %>
                </select>
            </div>
            <div>
                <label for="blog_type_id">日誌分類編號</label>
                <input id="blog_type_id" type="number" name="blog_type_id" value="<%= blogVO.getBlogTypeId() == null ? 1 : blogVO.getBlogTypeId() %>" min="1" required>
            </div>
        </div>

        <div>
            <label for="blog_content">日誌內容</label>
            <textarea id="blog_content" name="blog_content" required><%= blogVO.getBlogContent() == null ? "" : blogVO.getBlogContent() %></textarea>
        </div>

        <button class="btn" type="submit">新增日誌</button>
    </form>
</main>
</body>
</html>
