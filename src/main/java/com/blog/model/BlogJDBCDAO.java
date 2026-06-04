package com.blog.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class BlogJDBCDAO implements BlogDAO_interface {

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/farmily?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";

	private static final String INSERT_STMT =
			"INSERT INTO Blog (blog_title, user_id, farmer_id, blog_type_id, product_id, blog_content, blog_img, blog_like, blog_time, blog_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String UPDATE =
			"UPDATE Blog SET blog_title = ?, user_id = ?, farmer_id = ?, blog_type_id = ?, product_id = ?, blog_content = ?, blog_img = ?, blog_status = ? WHERE blog_id = ?";

	private static final String DELETE =
			"DELETE FROM Blog WHERE blog_id = ?";

	private static final String GET_ONE_STMT =
			"SELECT blog_id, blog_title, user_id, farmer_id, blog_type_id, product_id, blog_content, blog_img, blog_like, blog_time, blog_status FROM Blog WHERE blog_id = ?";

	private static final String GET_ALL_STMT =
			"SELECT blog_id, blog_title, user_id, farmer_id, blog_type_id, product_id, blog_content, blog_img, blog_like, blog_time, blog_status FROM Blog ORDER BY blog_id";

	private static final String GET_ALL_USERS_STMT =
			"SELECT user_id, user_name FROM `user` ORDER BY user_id";

	private static final String GET_ALL_FARMERS_STMT =
			"SELECT farmer_id, farm_name FROM farmer ORDER BY farmer_id";

	private static final String GET_ALL_BLOGTYPE_STMT =
			"SELECT blog_type_id, blog_type_name FROM blog_type ORDER BY blog_type_id";

	private static final String GET_ALL_PRODUCT_STMT =
			"SELECT product_id, description FROM product_detail ORDER BY product_id";

	@Override
	public void insert(BlogVO blogVO) {
		try (Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(INSERT_STMT)) {

			pstmt.setString(1, blogVO.getBlogTitle());
			setNullableInteger(pstmt, 2, blogVO.getUserId());
			setNullableInteger(pstmt, 3, blogVO.getFarmerId());
			pstmt.setInt(4, blogVO.getBlogTypeId());
			pstmt.setInt(5, blogVO.getProductId());
			pstmt.setString(6, blogVO.getBlogContent());
			pstmt.setBytes(7, blogVO.getBlogImg());
			pstmt.setInt(8, blogVO.getBlogLike());
			pstmt.setTimestamp(9, blogVO.getBlogTime());
			pstmt.setString(10, toStatusName(blogVO.getBlogStatus()));

			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(BlogVO blogVO) {
		try (Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(UPDATE)) {

			pstmt.setString(1, blogVO.getBlogTitle());
			setNullableInteger(pstmt, 2, blogVO.getUserId());
			setNullableInteger(pstmt, 3, blogVO.getFarmerId());
			pstmt.setInt(4, blogVO.getBlogTypeId());
			pstmt.setInt(5, blogVO.getProductId());
			pstmt.setString(6, blogVO.getBlogContent());
			pstmt.setBytes(7, blogVO.getBlogImg());
			pstmt.setString(8, toStatusName(blogVO.getBlogStatus()));
			pstmt.setInt(9, blogVO.getBlogId());

			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(Integer blogId) {
		try (Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(DELETE)) {

			pstmt.setInt(1, blogId);
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BlogVO findByPrimaryKey(Integer blogId) {
		BlogVO blogVO = null;

		try (Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(GET_ONE_STMT)) {

			pstmt.setInt(1, blogId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					blogVO = buildBlogVO(rs);
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}

		return blogVO;
	}

	@Override
	public List<BlogVO> getAll() {
		List<BlogVO> list = new ArrayList<BlogVO>();

		try (Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(GET_ALL_STMT);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				list.add(buildBlogVO(rs));
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	@Override
	public List<UserVO> getAllUsers() {
		List<UserVO> list = new ArrayList<UserVO>();

		try (Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(GET_ALL_USERS_STMT);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				UserVO userVO = new UserVO();
				userVO.setUserId(rs.getInt("user_id"));
				userVO.setUserName(rs.getString("user_name"));
				list.add(userVO);
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	@Override
	public List<FarmerVO> getAllFarmers() {
		List<FarmerVO> list = new ArrayList<FarmerVO>();

		try (Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(GET_ALL_FARMERS_STMT);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				FarmerVO farmerVO = new FarmerVO();
				farmerVO.setFarmerId(rs.getInt("farmer_id"));
				farmerVO.setFarmName(rs.getString("farm_name"));
				list.add(farmerVO);
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	@Override
	public List<BlogTypeVO> getAllBlogTypes() {
		List<BlogTypeVO> list = new ArrayList<BlogTypeVO>();

		try (Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(GET_ALL_BLOGTYPE_STMT);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				BlogTypeVO blogTypeVO = new BlogTypeVO();
				blogTypeVO.setBlogTypeId(rs.getInt("blog_type_id"));
				blogTypeVO.setBlogTypeName(rs.getString("blog_type_name"));
				list.add(blogTypeVO);
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	@Override
	public List<ProductVO> getAllProducts() {
		List<ProductVO> list = new ArrayList<ProductVO>();

		try (Connection con = getConnection();
				PreparedStatement pstmt = con.prepareStatement(GET_ALL_PRODUCT_STMT);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				ProductVO productVO = new ProductVO();
				productVO.setProductId(rs.getInt("product_id"));
				productVO.setDescription(rs.getString("description"));
				list.add(productVO);
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		return DriverManager.getConnection(url, userid, passwd);
	}

	private BlogVO buildBlogVO(ResultSet rs) throws SQLException {
		BlogVO blogVO = new BlogVO();

		blogVO.setBlogId(rs.getInt("blog_id"));
		blogVO.setBlogTitle(rs.getString("blog_title"));
		blogVO.setUserId(getNullableInteger(rs, "user_id"));
		blogVO.setFarmerId(getNullableInteger(rs, "farmer_id"));
		blogVO.setBlogTypeId(rs.getInt("blog_type_id"));
		blogVO.setProductId(rs.getInt("product_id"));
		blogVO.setBlogContent(rs.getString("blog_content"));
		blogVO.setBlogImg(rs.getBytes("blog_img"));
		blogVO.setBlogLike(rs.getInt("blog_like"));
		blogVO.setBlogTime(rs.getTimestamp("blog_time"));
		blogVO.setBlogStatus(toBlogStatus(rs.getString("blog_status")));

		return blogVO;
	}

	private Integer getNullableInteger(ResultSet rs, String columnName) throws SQLException {
		int value = rs.getInt(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return value;
	}

	private void setNullableInteger(PreparedStatement pstmt, int parameterIndex, Integer value) throws SQLException {
		if (value == null) {
			pstmt.setNull(parameterIndex, Types.INTEGER);
		} else {
			pstmt.setInt(parameterIndex, value);
		}
	}

	private String toStatusName(BlogStatus blogStatus) {
		return blogStatus == null ? null : blogStatus.name();
	}

	private BlogStatus toBlogStatus(String value) {
		return value == null ? null : BlogStatus.valueOf(value);
	}

	public static void main(String[] args) {
		BlogJDBCDAO dao = new BlogJDBCDAO();
		List<BlogVO> list = dao.getAll();

		for (BlogVO blogVO : list) {
			System.out.println(blogVO.getBlogId() + ",");
			System.out.println(blogVO.getBlogTitle() + ",");
			System.out.println(blogVO.getUserId() + ",");
			System.out.println(blogVO.getFarmerId() + ",");
			System.out.println(blogVO.getBlogTypeId() + ",");
			System.out.println(blogVO.getProductId() + ",");
			System.out.println(blogVO.getBlogContent() + ",");
			System.out.println(blogVO.getBlogImg() + ",");
			System.out.println(blogVO.getBlogLike() + ",");
			System.out.println(blogVO.getBlogTime() + ",");
			System.out.println(blogVO.getBlogStatus() + ",");
			System.out.println("======================");
		}
	}
}
