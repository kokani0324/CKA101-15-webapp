package com.blog.model;

import java.util.List;

public class BlogService {

	private BlogDAO_interface dao;

	public BlogService() {

		// JDBC 版
		dao = new BlogJDBCDAO();

		// Hibernate 版
		// dao = new BlogHibernateDAO();
	}

	public BlogVO addBlog(String blogTitle, Integer userId, Integer farmerId, Integer blogTypeId,
	                      Integer productId, String blogContent, byte[] blogImg, String blogStatus) {

		BlogVO blogVO = new BlogVO();

		blogVO.setBlogTitle(blogTitle);
		blogVO.setUserId(userId);
		blogVO.setFarmerId(farmerId);
		blogVO.setBlogTypeId(blogTypeId);
		blogVO.setProductId(productId);
		blogVO.setBlogContent(blogContent);
		blogVO.setBlogImg(blogImg);

		blogVO.setBlogLike(0); // 新文章按讚數預設為 0
		blogVO.setBlogTime(new java.sql.Timestamp(System.currentTimeMillis())); // 抓取現在當下的時間

		blogVO.setBlogStatus(blogStatus);

		dao.insert(blogVO);

		return blogVO;
	}

	public BlogVO updateBlog(String blogTitle, Integer userId, Integer farmerId, Integer blogTypeId,
	                         Integer productId, String blogContent, byte[] blogImg,
	                         String blogStatus, Integer blogId) {

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

		// 保留原本按讚數與時間
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
}
