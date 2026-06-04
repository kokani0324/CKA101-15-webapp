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

	private static final String MSG_BLOG_ID_REQUIRED = "請輸入 Blog 編號";
	private static final String MSG_BLOG_NOT_FOUND = "查無此 Blog 編號：";
	private static final String MSG_TITLE_REQUIRED = "標題請勿空白";
	private static final String MSG_CONTENT_REQUIRED = "文章內容請勿空白";
	private static final String MSG_STATUS_REQUIRED = "文章狀態請勿空白";
	private static final String MSG_USER_ID_FORMAT = "會員編號格式錯誤";
	private static final String MSG_FARMER_ID_FORMAT = "小農編號格式錯誤";
	private static final String MSG_OWNER_REQUIRED = "會員和小農不能同時空白";
	private static final String MSG_BLOG_TYPE_REQUIRED = "請輸入文章分類編號";
	private static final String MSG_PRODUCT_REQUIRED = "請輸入商品編號";
	private static final String MSG_NUMBER_REQUIRED = "，請輸入數字";
	private static final String MSG_INSERT_FAILED = "新增失敗：請確認會員、小農、文章分類與商品編號是否存在";
	private static final String MSG_UPDATE_FAILED = "修改失敗：請確認會員、小農、文章分類與商品編號是否存在";

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

		if ("getAll".equals(action)) {
			forwardBlogList(req, res);
			return;
		}

		if ("getOne_For_Display".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			req.setAttribute("blogId", req.getParameter("blog_id"));

			Integer blogId = parseRequiredInteger(req.getParameter("blog_id"), MSG_BLOG_ID_REQUIRED, errorMsgs);
			if (!errorMsgs.isEmpty()) {
				forward(req, res, "/frontend/blog/select_page.jsp");
				return;
			}

			BlogService blogSvc = new BlogService();
			BlogVO blogVO = blogSvc.getOneBlog(blogId);
			if (blogVO == null) {
				errorMsgs.add(MSG_BLOG_NOT_FOUND + blogId);
			}

			if (!errorMsgs.isEmpty()) {
				forward(req, res, "/frontend/blog/select_page.jsp");
				return;
			}

			req.setAttribute("blogVO", blogVO);
			forward(req, res, "/frontend/blog/listOneBlog.jsp");
			return;
		}

		if ("getOne_For_Update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			Integer blogId = parseRequiredInteger(req.getParameter("blog_id"), MSG_BLOG_ID_REQUIRED, errorMsgs);
			if (!errorMsgs.isEmpty()) {
				forwardBlogList(req, res);
				return;
			}

			BlogService blogSvc = new BlogService();
			BlogVO blogVO = blogSvc.getOneBlog(blogId);
			if (blogVO == null) {
				errorMsgs.add(MSG_BLOG_NOT_FOUND + blogId);
				forwardBlogList(req, res);
				return;
			}

			req.setAttribute("blogVO", blogVO);
			forward(req, res, "/frontend/blog/update_blog_input.jsp");
			return;
		}

		if ("update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			BlogVO blogVO = buildBlogFromRequest(req, true, errorMsgs);

			BlogService blogSvc = new BlogService();
			byte[] blogImg = getImageBytes(req, "blog_img");

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
				errorMsgs.add(MSG_UPDATE_FAILED);
				req.setAttribute("blogVO", blogVO);
				forward(req, res, "/frontend/blog/update_blog_input.jsp");
				return;
			}

			req.setAttribute("blogVO", blogVO);
			forward(req, res, "/frontend/blog/listOneBlog.jsp");
			return;
		}

		if ("insert".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			BlogVO blogVO = buildBlogFromRequest(req, false, errorMsgs);
			byte[] blogImg = getImageBytes(req, "blog_img");
			blogVO.setBlogImg(blogImg);

			if (!errorMsgs.isEmpty()) {
				req.setAttribute("blogVO", blogVO);
				forwardInsertForm(req, res);
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
				errorMsgs.add(MSG_INSERT_FAILED);
				req.setAttribute("blogVO", blogVO);
				forwardInsertForm(req, res);
				return;
			}

			forwardBlogList(req, res);
			return;
		}

		if ("delete".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			Integer blogId = parseRequiredInteger(req.getParameter("blog_id"), MSG_BLOG_ID_REQUIRED, errorMsgs);
			if (errorMsgs.isEmpty()) {
				BlogService blogSvc = new BlogService();
				blogSvc.deleteBlog(blogId);
			}

			forwardBlogList(req, res);
			return;
		}

		forward(req, res, "/frontend/blog/select_page.jsp");
	}

	private BlogVO buildBlogFromRequest(HttpServletRequest req, boolean includeBlogId, List<String> errorMsgs) {
		BlogVO blogVO = new BlogVO();

		if (includeBlogId) {
			blogVO.setBlogId(parseRequiredInteger(req.getParameter("blog_id"), MSG_BLOG_ID_REQUIRED, errorMsgs));
		}

		String blogTitle = trim(req.getParameter("blog_title"));
		String blogContent = trim(req.getParameter("blog_content"));
		String blogStatus = trim(req.getParameter("blog_status"));

		if (blogTitle.length() == 0) {
			errorMsgs.add(MSG_TITLE_REQUIRED);
		}
		if (blogContent.length() == 0) {
			errorMsgs.add(MSG_CONTENT_REQUIRED);
		}
		if (blogStatus.length() == 0) {
			errorMsgs.add(MSG_STATUS_REQUIRED);
		}

		blogVO.setBlogTitle(blogTitle);
		blogVO.setBlogContent(blogContent);
		blogVO.setBlogStatus(blogStatus);
		blogVO.setUserId(parseOptionalInteger(req.getParameter("user_id"), MSG_USER_ID_FORMAT, errorMsgs));
		blogVO.setFarmerId(parseOptionalInteger(req.getParameter("farmer_id"), MSG_FARMER_ID_FORMAT, errorMsgs));
		if (blogVO.getUserId() == null && blogVO.getFarmerId() == null) {
			errorMsgs.add(MSG_OWNER_REQUIRED);
		}
		blogVO.setBlogTypeId(parseRequiredInteger(req.getParameter("blog_type_id"), MSG_BLOG_TYPE_REQUIRED, errorMsgs));
		blogVO.setProductId(parseRequiredInteger(req.getParameter("product_id"), MSG_PRODUCT_REQUIRED, errorMsgs));

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
			errorMsgs.add(blankMessage + MSG_NUMBER_REQUIRED);
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

	private void forwardInsertForm(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String source = req.getParameter("source");
		if ("farmer".equals(source)) {
			forward(req, res, "/farmer/blog/addFarmDiary.jsp");
		} else {
			forward(req, res, "/frontend/blog/addBlog.jsp");
		}
	}

	private void forward(HttpServletRequest req, HttpServletResponse res, String url)
			throws ServletException, IOException {
		RequestDispatcher view = req.getRequestDispatcher(url);
		view.forward(req, res);
	}

	private byte[] getImageBytes(HttpServletRequest req, String partName) throws IOException, ServletException {
		Part part = req.getPart(partName);

		if (part == null || part.getSize() == 0) {
			return null;
		}

		return part.getInputStream().readAllBytes();
	}
}
