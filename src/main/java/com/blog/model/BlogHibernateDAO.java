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

            /*
             * merge() 適合用在 servlet/service 傳進來的 detached object。
             * 它會根據 blogId 判斷資料庫中是否有這筆資料，再進行更新。
             */
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

    public static void main(String[] args) {
        BlogHibernateDAO dao = new BlogHibernateDAO();

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