package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.blog.model.BlogVO;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure()
                    .addAnnotatedClass(BlogVO.class)
                    .buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("SessionFactory 建立失敗：" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}