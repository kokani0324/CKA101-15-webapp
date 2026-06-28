# CKA101_15_webapp — 部落格(Blog)

「Farmily」小農電商網站專題中的 **部落格模組**,提供文章的新增、查詢、修改、刪除(CRUD),
並區分 **會員前台**、**小農後台**、**管理員後台** 三種使用情境。

採用傳統的 **Servlet + JSP** MVC 架構,以 WAR 部署到 Servlet 容器(Tomcat)執行。
資料存取層同時實作了 **JDBC** 與 **Hibernate** 兩種版本,方便對照學習。

---

## 技術棧

| 項目 | 版本 / 說明 |
|------|------------|
| Java | 17 |
| Web | Jakarta Servlet 6、JSP + JSTL 3 |
| ORM / 資料存取 | Hibernate 6.6.4、原生 JDBC(兩種並存) |
| 資料庫 | MySQL(schema 名稱:`farmily`) |
| 前端 | JSP、HTML |
| 建置 / 相依管理 | Maven、Spring Boot 3.4.1 父專案(打包為 `war`) |

---

## 專案結構

```
src/main/java/
├── util/HibernateUtil.java              # Hibernate SessionFactory 工具
└── com/blog/
    ├── controller/
    │   └── Blogservlet.java             # 主控 Servlet:/blog/blog.do(含完整欄位驗證)
    └── model/                           # Model 層(Service + DAO + VO 全在此套件)
        ├── BlogService.java             # 業務邏輯層
        ├── BlogDAO_interface.java       # DAO 共同介面
        ├── BlogJDBCDAO.java             # JDBC 實作(目前 Service 預設使用)
        ├── BlogHibernateDAO.java        # Hibernate 實作(可替換)
        ├── BlogVO.java                  # 文章(@Entity,對應 blog 表)
        ├── BlogTypeVO.java              # 文章分類
        ├── FarmerVO / ProductVO / UserVO# 關聯資料(下拉選單用)
        └── BlogStatus.java              # 文章狀態列舉(VISIBLE / HIDDEN)

src/main/resources/
└── hibernate.cfg.xml                    # Hibernate 連線與 Entity 對應設定

src/main/webapp/
├── admin/                # 管理員後台(index.html、login.html)
├── farmer/blog/          # 小農後台(MyBloglist.jsp、addFarmDiary.jsp)
├── frontend/blog/        # 會員前台 JSP
│   ├── addBlog.jsp           # 新增文章
│   ├── listAllBlog.jsp       # 查詢全部文章(含分頁)
│   ├── listOneBlog.jsp       # 查詢單一文章
│   ├── update_blog_input.jsp # 修改文章
│   └── select_page.jsp       # 查詢入口頁
├── frontend/images/      # 圖片資源
└── WEB-INF/web.xml
```

---

## 運作流程(MVC)

1. 前端 JSP 表單將請求送往 `Blogservlet`(URL:`/blog/blog.do`)。
2. `Blogservlet` 解析參數、做欄位驗證(標題、內容、會員/小農、分類、商品等),呼叫 `BlogService`。
3. `BlogService` 透過 `BlogDAO_interface`(JDBC 或 Hibernate 實作)存取資料庫。
4. 處理完成後,Servlet 以 `RequestDispatcher` 轉交回對應的 JSP 顯示結果。

`BlogService` 提供的主要操作:

| 方法 | 說明 |
|------|------|
| `addBlog(...)` | 新增文章(自動帶入按讚數 0 與建立時間) |
| `updateBlog(...)` | 修改文章(保留原按讚數與建立時間) |
| `deleteBlog(blogId)` | 刪除文章 |
| `getOneBlog(blogId)` | 查詢單一文章 |
| `getAll()` | 查詢全部文章 |
| `getAllUsers / getAllFarmers / getAllBlogTypes / getAllProducts` | 下拉選單用的關聯資料 |

---

## 資料庫設定

預設連線(同時寫在 `hibernate.cfg.xml` 與 `BlogJDBCDAO`):

```
URL : jdbc:mysql://localhost:3306/farmily?serverTimezone=Asia/Taipei
User: root
Pass: 123456
```

- 需先在 MySQL 建立 `farmily` 資料庫,並準備好 `blog`、`user`、`farmer`、`blog_type`、`product` 等資料表。
- Hibernate 設定為 `schema-generation = none`,**不會自動建表**,使用既有資料表。



---

## 建置與部署

```bash
# 建置(產生 war)
mvn clean package
```

將產出的 `target/CKA101_15_webapp-0.0.1-SNAPSHOT.war` 部署到 Tomcat(或於 Eclipse / IntelliJ 內掛 Server 執行)。

啟動後前台入口位於 `frontend/blog/`,文章相關操作統一送往 `/blog/blog.do`。

---

## 資料存取層切換

`BlogService` 建構子預設使用 JDBC 實作,要改用 Hibernate 只需切換這兩行:

```java
public BlogService() {
    dao = new BlogJDBCDAO();
    // dao = new BlogHibernateDAO();
}
```
