package com.blog.model;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class BlogHibernateDAO implements BlogDAO_interface {

	private SessionFactory factory;

	public BlogHibernateDAO() {
		factory = HibernateUtil.getSessionFactory();
	}

	@Override
	public void insert(BlogVO blogVO) {
		Transaction tx = null;

		try (Session session = factory.openSession()) {
			tx = session.beginTransaction();

			session.persist(blogVO);

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
	}

	@Override
	public void update(BlogVO blogVO) {
		Transaction tx = null;

		try (Session session = factory.openSession()) {
			tx = session.beginTransaction();

			// merge() 適合處理從 Servlet/Service 傳來的 detached object，
			// Hibernate 會依 blogId 判斷要更新既有資料。
			session.merge(blogVO);

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
	}

	@Override
	public void delete(Integer blogId) {
		Transaction tx = null;

		try (Session session = factory.openSession()) {
			tx = session.beginTransaction();

			// 先查出 Entity，再交給 Hibernate 刪除，避免刪除不存在資料時拋出不必要錯誤。
			BlogVO blogVO = session.get(BlogVO.class, blogId);

			if (blogVO != null) {
				session.remove(blogVO);
			}

			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
	}

	@Override
	public BlogVO findByPrimaryKey(Integer blogId) {
		try (Session session = factory.openSession()) {
			return session.get(BlogVO.class, blogId);
		}
	}

	@Override
	public List<BlogVO> getAll() {
		try (Session session = factory.openSession()) {
			return session.createQuery("from BlogVO order by blogId", BlogVO.class)
					.getResultList();
		}
	}

	@Override
	public List<UserVO> getAllUsers() {
		return new BlogJDBCDAO().getAllUsers();
	}

	@Override
	public List<FarmerVO> getAllFarmers() {
		return new BlogJDBCDAO().getAllFarmers();
	}

	public static void main(String[] args) {
		BlogHibernateDAO dao = new BlogHibernateDAO();

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
