package com.blog.model;

import java.util.List;

public class BlogService {

	private BlogDAO_interface dao;

	public BlogService() {
		dao = new BlogJDBCDAO();
		// dao = new BlogHibernateDAO();
	}

	public BlogVO addBlog(String blogTitle, Integer userId, Integer farmerId, Integer blogTypeId,
			Integer productId, String blogContent, byte[] blogImg, BlogStatus blogStatus) {

		BlogVO blogVO = new BlogVO();

		blogVO.setBlogTitle(blogTitle);
		blogVO.setUserId(userId);
		blogVO.setFarmerId(farmerId);
		blogVO.setBlogTypeId(blogTypeId);
		blogVO.setProductId(productId);
		blogVO.setBlogContent(blogContent);
		blogVO.setBlogImg(blogImg);
		blogVO.setBlogLike(0);
		blogVO.setBlogTime(new java.sql.Timestamp(System.currentTimeMillis()));
		blogVO.setBlogStatus(blogStatus);

		dao.insert(blogVO);

		return blogVO;
	}

	public BlogVO updateBlog(String blogTitle, Integer userId, Integer farmerId, Integer blogTypeId,
			Integer productId, String blogContent, byte[] blogImg, BlogStatus blogStatus, Integer blogId) {

		BlogVO blogVO = new BlogVO();

		blogVO.setBlogId(blogId);
		blogVO.setBlogTitle(blogTitle);
		blogVO.setUserId(userId);
		blogVO.setFarmerId(farmerId);
		blogVO.setBlogTypeId(blogTypeId);
		blogVO.setProductId(productId);
		blogVO.setBlogContent(blogContent);
		blogVO.setBlogImg(blogImg);
		blogVO.setBlogStatus(blogStatus);

		BlogVO oldBlogVO = dao.findByPrimaryKey(blogId);
		if (oldBlogVO != null) {
			blogVO.setBlogLike(oldBlogVO.getBlogLike());
			blogVO.setBlogTime(oldBlogVO.getBlogTime());
		}

		dao.update(blogVO);

		return blogVO;
	}

	public void deleteBlog(Integer blogId) {
		dao.delete(blogId);
	}

	public BlogVO getOneBlog(Integer blogId) {
		return dao.findByPrimaryKey(blogId);
	}

	public List<BlogVO> getAll() {
		return dao.getAll();
	}

	public List<UserVO> getAllUsers() {
		return dao.getAllUsers();
	}

	public List<FarmerVO> getAllFarmers() {
		return dao.getAllFarmers();
	}

	public List<BlogTypeVO> getAllBlogTypes() {
		return dao.getAllBlogTypes();
	}

	public List<ProductVO> getAllProducts() {
		return dao.getAllProducts();
	}
}
