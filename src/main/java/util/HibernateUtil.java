package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.blog.model.BlogVO;

public class HibernateUtil {

	// SessionFactory 建立成本較高，整個應用程式共用同一個實例即可。
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			return new Configuration()
					.configure()
					.addAnnotatedClass(BlogVO.class)
					.buildSessionFactory();
			// configure() 會讀取 hibernate.cfg.xml。
			// addAnnotatedClass() 讓 Hibernate 知道 BlogVO 是要管理的 Entity。
		} catch (Throwable ex) {
			System.err.println("SessionFactory 建立失敗: " + ex);
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
