package com.blog.model;

import java.util.List;

public class BlogService {

	private BlogDAO_interface dao;

	public BlogService() {

		// 目前預設使用 JDBC DAO；若要改用 Hibernate，可切換成 BlogHibernateDAO。
		dao = new BlogJDBCDAO();

		// Hibernate 版本
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

		// 新增文章時，讚數從 0 開始，建立時間使用目前系統時間。
		blogVO.setBlogLike(0);
		blogVO.setBlogTime(new java.sql.Timestamp(System.currentTimeMillis()));

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

		// 修改文章內容時保留原本的讚數與發文時間，避免被表單更新流程覆蓋。
		BlogVO oldBlogVO = dao.findByPrimaryKey(blogId);

		if (oldBlogVO != null) {
			blogVO.setBlogLike(oldBlogVO.getBlogLike()); //拿舊資料的讚數
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

}
