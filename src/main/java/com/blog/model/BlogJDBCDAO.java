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
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, blogVO.getBlogTitle());

			// userId 與 farmerId 在不同來源表單可能為空，因此用 setNull 寫入資料庫 NULL。
			if (blogVO.getUserId() == null) {
				pstmt.setNull(2, Types.INTEGER);
			} else {
				pstmt.setInt(2, blogVO.getUserId());
			}

			if (blogVO.getFarmerId() == null) {
				pstmt.setNull(3, Types.INTEGER);
			} else {
				pstmt.setInt(3, blogVO.getFarmerId());
			}

			pstmt.setInt(4, blogVO.getBlogTypeId());
			pstmt.setInt(5, blogVO.getProductId());
			pstmt.setString(6, blogVO.getBlogContent());
			pstmt.setBytes(7, blogVO.getBlogImg());
			pstmt.setInt(8, blogVO.getBlogLike());
			pstmt.setTimestamp(9, blogVO.getBlogTime());
			pstmt.setString(10, blogVO.getBlogStatus());

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@Override
	public void update(BlogVO blogVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, blogVO.getBlogTitle());
			if (blogVO.getUserId() == null) {
				pstmt.setNull(2, Types.INTEGER);
			} else {
				pstmt.setInt(2, blogVO.getUserId());
			}

			if (blogVO.getFarmerId() == null) {
				pstmt.setNull(3, Types.INTEGER);
			} else {
				pstmt.setInt(3, blogVO.getFarmerId());
			}

			pstmt.setInt(4, blogVO.getBlogTypeId());
			pstmt.setInt(5, blogVO.getProductId());
			pstmt.setString(6, blogVO.getBlogContent());
			pstmt.setBytes(7, blogVO.getBlogImg());
			pstmt.setString(8, blogVO.getBlogStatus());
			pstmt.setInt(9, blogVO.getBlogId());

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@Override
	public void delete(Integer blogId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(DELETE);
			pstmt.setInt(1, blogId);

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@Override
	public BlogVO findByPrimaryKey(Integer blogId) {
		BlogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ONE_STMT);
			pstmt.setInt(1, blogId);

			rs = pstmt.executeQuery();

			// ResultSet 游標移到第一筆資料後，再把資料庫欄位轉成 BlogVO。
			if (rs.next()) {
				blogVO = new BlogVO();

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
				blogVO.setBlogStatus(rs.getString("blog_status"));
			}

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		} catch (SQLException se) {
			throw new RuntimeException(se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}

		return blogVO;
	}

	@Override
	public List<BlogVO> getAll() {
		List<BlogVO> list = new ArrayList<BlogVO>();
		BlogVO blogVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			// 逐筆讀取查詢結果，轉成 BlogVO 後放進 List 回傳給 Service。
			while (rs.next()) {
				blogVO = new BlogVO();

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
				blogVO.setBlogStatus(rs.getString("blog_status"));

				list.add(blogVO);
			}

		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ignored) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException ignored) {
				}
			}

			if (con != null) {
				try {
					con.close();
				} catch (SQLException ignored) {
				}
			}
		}

		return list;
	}
	@Override
	public List<UserVO> getAllUsers() {
		List<UserVO> list = new ArrayList<UserVO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;



		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_USERS_STMT);
			rs = pstmt.executeQuery();

			while(rs.next()){
				UserVO userVO = new UserVO();
				Integer userId = rs.getInt("user_id");
				String userName = rs.getString("user_name");
				userVO.setUserId(userId);
				userVO.setUserName(userName);

				list.add(userVO);
			}


		}catch(Exception e){
			throw new RuntimeException(e);
		}finally {
			if(rs != null) try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(pstmt != null) try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ignored) {
				}
			}
		}
		return list;
	}

	@Override
	public List<FarmerVO> getAllFarmers() {
		List<FarmerVO> list = new ArrayList<FarmerVO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_FARMERS_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				FarmerVO farmerVO = new FarmerVO();
				farmerVO.setFarmerId(rs.getInt("farmer_id"));
				farmerVO.setFarmName(rs.getString("farm_name"));

				list.add(farmerVO);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ignored) {
				}
			}
		}
		return list;
	}
	@Override
	public List<BlogTypeVO> getAllBlogTypes() {
		List<BlogTypeVO> list = new ArrayList<BlogTypeVO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_BLOGTYPE_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BlogTypeVO blogTypeVO = new BlogTypeVO();
				blogTypeVO.setBlogTypeId(rs.getInt("blog_type_id"));
				blogTypeVO.setBlogTypeName(rs.getString("blog_type_name"));

				list.add(blogTypeVO);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ignored) {
				}
			}
		}
		return list;
	}

	@Override
	public List<ProductVO> getAllProducts() {
		List<ProductVO> list = new ArrayList<ProductVO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);

			pstmt = con.prepareStatement(GET_ALL_PRODUCT_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ProductVO productVO = new ProductVO();
				productVO.setProductId(rs.getInt("product_id"));
				productVO.setDescription(rs.getString("description"));

				list.add(productVO);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ignored) {
				}
			}
		}
		return list;
	}

	private Integer getNullableInteger(ResultSet rs, String columnName) throws SQLException {
		int value = rs.getInt(columnName);
		if (rs.wasNull()) {
			return null;
		}
		return value;
	}

	public static void main(String[] args) {
		BlogJDBCDAO dao = new BlogJDBCDAO();

		// 簡易測試：查詢全部文章並印出欄位內容。
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
