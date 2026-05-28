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

			Integer blogId = parseRequiredInteger(req.getParameter("blog_id"), "Please enter blog id", errorMsgs);
			if (!errorMsgs.isEmpty()) {
				forward(req, res, "/frontend/blog/select_page.jsp");
				return;
			}

			BlogService blogSvc = new BlogService();
			BlogVO blogVO = blogSvc.getOneBlog(blogId);
			if (blogVO == null) {
				errorMsgs.add("No blog data found");
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

			Integer blogId = parseRequiredInteger(req.getParameter("blog_id"), "Please enter blog id", errorMsgs);
			if (!errorMsgs.isEmpty()) {
				forwardBlogList(req, res);
				return;
			}

			BlogService blogSvc = new BlogService();
			BlogVO blogVO = blogSvc.getOneBlog(blogId);
			if (blogVO == null) {
				errorMsgs.add("No blog data found");
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

		    // 取得這次使用者上傳的新圖片
		    byte[] blogImg = getImageBytes(req, "blog_img");

		    // 如果沒有上傳新圖片，就保留原本的圖片
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
				String source = req.getParameter("source");
				if ("farmer".equals(source)) {
					forward(req, res, "/farmer/blog/addFarmDiary.jsp");
				} else {
					forward(req, res, "/frontend/blog/addBlog.jsp");
				}
				return;
			}
			
			BlogService blogSvc = new BlogService();
			blogSvc.addBlog(
					blogVO.getBlogTitle(),
					blogVO.getUserId(),
					blogVO.getFarmerId(),
					blogVO.getBlogTypeId(),
					blogVO.getProductId(),
					blogVO.getBlogContent(),
					blogImg,
					blogVO.getBlogStatus());

			forwardBlogList(req, res);
			return;
		}

		if ("delete".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			Integer blogId = parseRequiredInteger(req.getParameter("blog_id"), "Please enter blog id", errorMsgs);
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
			blogVO.setBlogId(parseRequiredInteger(req.getParameter("blog_id"), "Please enter blog id", errorMsgs));
		}

		String blogTitle = trim(req.getParameter("blog_title"));
		String blogContent = trim(req.getParameter("blog_content"));
		String blogStatus = trim(req.getParameter("blog_status"));

		if (blogTitle.length() == 0) {
			errorMsgs.add("Blog title is required");
		}
		if (blogContent.length() == 0) {
			errorMsgs.add("Blog content is required");
		}
		if (blogStatus.length() == 0) {
			errorMsgs.add("Blog status is required");
		}

		blogVO.setBlogTitle(blogTitle);
		blogVO.setBlogContent(blogContent);
		blogVO.setBlogStatus(blogStatus);
		if (includeBlogId) {
			blogVO.setUserId(parseRequiredInteger(req.getParameter("user_id"), "Please enter user id", errorMsgs));
			blogVO.setFarmerId(parseRequiredInteger(req.getParameter("farmer_id"), "Please enter farmer id", errorMsgs));
		} else {
			blogVO.setUserId(parseOptionalInteger(req.getParameter("user_id"), "User id must be a number", errorMsgs));
			blogVO.setFarmerId(parseOptionalInteger(req.getParameter("farmer_id"), "Farmer id must be a number", errorMsgs));
		}
		blogVO.setBlogTypeId(parseRequiredInteger(req.getParameter("blog_type_id"), "Please enter blog type id", errorMsgs));
		blogVO.setProductId(parseRequiredInteger(req.getParameter("product_id"), "Please enter product id", errorMsgs));

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
			errorMsgs.add(blankMessage + ", numbers only");
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
	//讀取圖片
	private byte[] getImageBytes(HttpServletRequest req, String partName) throws IOException, ServletException {
	    Part part = req.getPart(partName);
	    
	    if (part == null || part.getSize() == 0) {
	        return null;
	    }

	    return part.getInputStream().readAllBytes();
	}

}
