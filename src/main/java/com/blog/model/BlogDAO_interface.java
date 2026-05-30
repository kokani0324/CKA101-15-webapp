package com.blog.model;

import java.util.List;

// Blog DAO 的共同規格，讓 JDBC 與 Hibernate 實作可以被 Service 層替換使用。
public interface BlogDAO_interface {

	public void insert(BlogVO blogVO);

	public void update(BlogVO blogVO);

	// 依主鍵刪除文章。
	public void delete(Integer blogId);

	// 依主鍵查詢單一文章；查不到時回傳 null。
	public BlogVO findByPrimaryKey(Integer blogId);

	// 查詢全部文章。
	public List<BlogVO> getAll();

	// 可保留給未來複合查詢使用，例如用 Map 傳入多個查詢條件。
	// public List<BlogVO> getAll(Map<String, String[]> map);

	//下拉式選單用的
	public List<UserVO> getAllUsers();

	public List<FarmerVO> getAllFarmers();
}
