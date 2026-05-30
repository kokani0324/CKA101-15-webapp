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

	private static final String MSG_BLOG_ID_REQUIRED = "\u8acb\u8f38\u5165 Blog \u7de8\u865f";
	private static final String MSG_BLOG_NOT_FOUND = "\u67e5\u7121\u6b64 Blog \u7de8\u865f\uff1a";
	private static final String MSG_TITLE_REQUIRED = "\u6a19\u984c\u8acb\u52ff\u7a7a\u767d";
	private static final String MSG_CONTENT_REQUIRED = "\u6587\u7ae0\u5167\u5bb9\u8acb\u52ff\u7a7a\u767d";
	private static final String MSG_STATUS_REQUIRED = "\u6587\u7ae0\u72c0\u614b\u8acb\u52ff\u7a7a\u767d";
	private static final String MSG_USER_ID_FORMAT = "\u6703\u54e1\u7de8\u865f\u683c\u5f0f\u932f\u8aa4";
	private static final String MSG_FARMER_ID_FORMAT = "\u5c0f\u8fb2\u7de8\u865f\u683c\u5f0f\u932f\u8aa4";
	private static final String MSG_OWNER_REQUIRED = "\u6703\u54e1\u548c\u5c0f\u8fb2\u4e0d\u80fd\u540c\u6642\u7a7a\u767d";
	private static final String MSG_BLOG_TYPE_REQUIRED = "\u8acb\u8f38\u5165\u6587\u7ae0\u5206\u985e\u7de8\u865f";
	private static final String MSG_PRODUCT_REQUIRED = "\u8acb\u8f38\u5165\u5546\u54c1\u7de8\u865f";
	private static final String MSG_NUMBER_REQUIRED = "\uff0c\u8acb\u8f38\u5165\u6578\u5b57";
	private static final String MSG_INSERT_FAILED = "\u65b0\u589e\u5931\u6557\uff1a\u8acb\u78ba\u8a8d\u6703\u54e1\u3001\u5c0f\u8fb2\u3001\u6587\u7ae0\u5206\u985e\u8207\u5546\u54c1\u7de8\u865f\u662f\u5426\u5b58\u5728";
	private static final String MSG_UPDATE_FAILED = "\u4fee\u6539\u5931\u6557\uff1a\u8acb\u78ba\u8a8d\u6703\u54e1\u3001\u5c0f\u8fb2\u3001\u6587\u7ae0\u5206\u985e\u8207\u5546\u54c1\u7de8\u865f\u662f\u5426\u5b58\u5728";

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
