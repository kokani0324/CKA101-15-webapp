package com.blog.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.blog.model.BlogService;
import com.blog.model.BlogVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@MultipartConfig
public class Blogservlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");

		String action = req.getParameter("action");

		// 依 blog_id 讀取圖片，直接把圖片二進位資料回傳給瀏覽器。
		if ("getImage".equals(action)) {
			Integer blogId = Integer.valueOf(req.getParameter("blog_id"));

			BlogService blogSvc = new BlogService();
			BlogVO blogVO = blogSvc.getOneBlog(blogId);

			if (blogVO == null || blogVO.getBlogImg() == null) {
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			res.setContentType("image/jpeg");
			res.getOutputStream().write(blogVO.getBlogImg());
			return;
		}

		// 查詢全部文章。
		if ("getAll".equals(action)) {
			forwardBlogList(req, res);
			return;
		}

		// 查詢單一文章並顯示明細頁。
		if ("getOne_For_Display".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			req.setAttribute("blogId", req.getParameter("blog_id"));

			Integer blogId = parseRequiredInteger(req.getParameter("blog_id"), "請輸入 Blog 編號", errorMsgs);
			//從JSP拿到訊息 eeq.getParmeter("blog_id") ，先給parseRequiredInteger檢查
			//parseRequiredInteger 會檢查  有沒有空白  能不能轉成整數，如果有錯就會吧錯誤訊息加進 errorMsgs
			//parseRequiredInteger(接受前端傳來的參數名， 錯誤時顯示的errorMsg 前端， 用來收集錯誤訊息的 List)
			//最後都檢查正常 Integer blogId = "2" 就會變成 Integer blogId = 2
			if (!errorMsgs.isEmpty()) { //isEmpty 是不是空的 前面加!反轉，變成如果錯誤清單不是空的，
				//代表前面有錯誤
				forward(req, res, "/frontend/blog/select_page.jsp");
				return;
			}

			BlogService blogSvc = new BlogService();
			BlogVO blogVO = blogSvc.getOneBlog(blogId);
			if (blogVO == null) {
				errorMsgs.add("查無此 Blog 編號：" + blogId);
			}

			if (!errorMsgs.isEmpty()) {
				forward(req, res, "/frontend/blog/select_page.jsp");
				return;
			}

			req.setAttribute("blogVO", blogVO);
			forward(req, res, "/frontend/blog/listOneBlog.jsp");
			return;
		}

		// 進入修改頁前，先用主鍵查出原本資料。
		if ("getOne_For_Update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			Integer blogId = parseRequiredInteger(req.getParameter("blog_id"), "請輸入 Blog 編號", errorMsgs);
			if (!errorMsgs.isEmpty()) {
				forwardBlogList(req, res);
				return;
			}

			BlogService blogSvc = new BlogService();
			BlogVO blogVO = blogSvc.getOneBlog(blogId);
			if (blogVO == null) {
				errorMsgs.add("查無此 Blog 編號：" + blogId);
				forwardBlogList(req, res);
				return;
			}

			req.setAttribute("blogVO", blogVO);
			forward(req, res, "/frontend/blog/update_blog_input.jsp");
			return;
		}

		// 修改文章。
		if ("update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			BlogVO blogVO = buildBlogFromRequest(req, true, errorMsgs);

			BlogService blogSvc = new BlogService();

			// 若使用者有重新上傳圖片，就讀取新的圖片內容。
			byte[] blogImg = getImageBytes(req, "blog_img");

			// 若沒有重新上傳圖片，沿用資料庫中原本的圖片，避免更新時把圖片清空。
			if (blogImg == null) {
				BlogVO oldBlogVO = blogSvc.getOneBlog(blogVO.getBlogId());

				if (oldBlogVO != null) {
					blogImg = oldBlogVO.getBlogImg();
				}
			}

			blogVO.setBlogImg(blogImg);

			if (!errorMsgs.isEmpty()) {
				req.setAttribute("blogVO", blogVO);
				forward(req, res, "/frontend/blog/update_blog_input.jsp");
				return;
			}

			try {
				blogVO = blogSvc.updateBlog(
						blogVO.getBlogTitle(),
						blogVO.getUserId(),
						blogVO.getFarmerId(),
						blogVO.getBlogTypeId(),
						blogVO.getProductId(),
						blogVO.getBlogContent(),
						blogImg,
						blogVO.getBlogStatus(),
						blogVO.getBlogId());
			} catch (RuntimeException e) {
				errorMsgs.add("修改失敗：請確認會員編號、農夫編號、商品編號與文章分類編號是否存在。");
				req.setAttribute("blogVO", blogVO);
				forward(req, res, "/frontend/blog/update_blog_input.jsp");
				return;
			}

			req.setAttribute("blogVO", blogVO);
			forward(req, res, "/frontend/blog/listOneBlog.jsp");
			return;
		}

		// 新增文章。
		if ("insert".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>(); // 建立一個(字串)錯誤訊息的陣列
			req.setAttribute("errorMsgs", errorMsgs);

			BlogVO blogVO = buildBlogFromRequest(req, false, errorMsgs);

			byte[] blogImg = getImageBytes(req, "blog_img");
			blogVO.setBlogImg(blogImg);

			if (!errorMsgs.isEmpty()) {
				req.setAttribute("blogVO", blogVO);

				// 依來源頁面回到對應的新增表單，保留使用者剛剛輸入的資料。
				String source = req.getParameter("source");
				if ("farmer".equals(source)) {
					forward(req, res, "/farmer/blog/addFarmDiary.jsp");
				} else {
					forward(req, res, "/frontend/blog/addBlog.jsp");
				}
				return;
			}

			BlogService blogSvc = new BlogService();
			try {
				blogSvc.addBlog(
						blogVO.getBlogTitle(),
						blogVO.getUserId(),
						blogVO.getFarmerId(),
						blogVO.getBlogTypeId(),
						blogVO.getProductId(),
						blogVO.getBlogContent(),
						blogImg,
						blogVO.getBlogStatus());
			} catch (RuntimeException e) {
				errorMsgs.add("新增失敗：請確認會員編號、農夫編號、商品編號與文章分類編號是否存在。");
				req.setAttribute("blogVO", blogVO);
				String source = req.getParameter("source");
				if ("farmer".equals(source)) {
					forward(req, res, "/farmer/blog/addFarmDiary.jsp");
				} else {
					forward(req, res, "/frontend/blog/addBlog.jsp");
				}
				return;
			}

			forwardBlogList(req, res);
			return;
		}

		// 刪除文章後回到列表頁。
		if ("delete".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			Integer blogId = parseRequiredInteger(req.getParameter("blog_id"), "請輸入 Blog 編號", errorMsgs);
			if (errorMsgs.isEmpty()) {
				BlogService blogSvc = new BlogService();
				blogSvc.deleteBlog(blogId);
			}

			forwardBlogList(req, res);
			return;
		}

		forward(req, res, "/frontend/blog/select_page.jsp");
	}

	// 將 request 參數集中轉成 BlogVO，讓新增與修改共用欄位驗證邏輯。
	private BlogVO buildBlogFromRequest(HttpServletRequest req, boolean includeBlogId, List<String> errorMsgs) {
		BlogVO blogVO = new BlogVO();

		if (includeBlogId) {
			blogVO.setBlogId(parseRequiredInteger(req.getParameter("blog_id"), "請輸入 Blog 編號", errorMsgs));
		}

		String blogTitle = trim(req.getParameter("blog_title"));
		String blogContent = trim(req.getParameter("blog_content"));
		String blogStatus = trim(req.getParameter("blog_status"));

		if (blogTitle.length() == 0) {
			errorMsgs.add("標題請勿空白");
		}
		if (blogContent.length() == 0) {
			errorMsgs.add("文章內容請勿空白");
		}
		if (blogStatus.length() == 0) {
			errorMsgs.add("文章狀態請勿空白");
		}

		blogVO.setBlogTitle(blogTitle);
		blogVO.setBlogContent(blogContent);
		blogVO.setBlogStatus(blogStatus);

		if (includeBlogId) {
			blogVO.setUserId(parseRequiredInteger(req.getParameter("user_id"), "請輸入會員編號", errorMsgs));
			blogVO.setFarmerId(parseRequiredInteger(req.getParameter("farmer_id"), "請輸入農夫編號", errorMsgs));
		} else {
			blogVO.setUserId(parseOptionalInteger(req.getParameter("user_id"), "會員編號只能輸入數字", errorMsgs));
			blogVO.setFarmerId(parseOptionalInteger(req.getParameter("farmer_id"), "農夫編號只能輸入數字", errorMsgs));
		}

		blogVO.setBlogTypeId(parseRequiredInteger(req.getParameter("blog_type_id"), "請輸入文章分類編號", errorMsgs));
		blogVO.setProductId(parseRequiredInteger(req.getParameter("product_id"), "請輸入商品編號", errorMsgs));

		return blogVO;
	}

	private Integer parseRequiredInteger(String value, String blankMessage, List<String> errorMsgs) {
		String trimmedValue = trim(value);
		if (trimmedValue.length() == 0) {
			errorMsgs.add(blankMessage);
			return null;
		}

		try {
			return Integer.valueOf(trimmedValue);
		} catch (NumberFormatException e) {
			errorMsgs.add(blankMessage + "，且只能輸入數字");
			return null;
		}
	}

	private Integer parseOptionalInteger(String value, String errorMessage, List<String> errorMsgs) {
		String trimmedValue = trim(value);
		if (trimmedValue.length() == 0) {
			return null;
		}

		try {
			return Integer.valueOf(trimmedValue);
		} catch (NumberFormatException e) {
			errorMsgs.add(errorMessage);
			return null;
		}
	}

	private String trim(String value) {
		return value == null ? "" : value.trim();
	}

	private void forwardBlogList(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		BlogService blogSvc = new BlogService();
		req.setAttribute("blogList", blogSvc.getAll());
		forward(req, res, "/frontend/blog/listAllBlog.jsp");
	}

	private void forward(HttpServletRequest req, HttpServletResponse res, String url)
			throws ServletException, IOException {
		RequestDispatcher view = req.getRequestDispatcher(url);
		view.forward(req, res);
	}

	// 讀取 multipart/form-data 上傳的圖片；未選檔時回傳 null。
	private byte[] getImageBytes(HttpServletRequest req, String partName) throws IOException, ServletException {
		Part part = req.getPart(partName);

		if (part == null || part.getSize() == 0) {
			return null;
		}

		return part.getInputStream().readAllBytes();
	}
}
