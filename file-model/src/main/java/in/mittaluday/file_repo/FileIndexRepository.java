package in.mittaluday.file_repo;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import in.mittaluday.file_model.FileIndex;

public class FileIndexRepository {
	private static SessionFactory factory;

	public FileIndexRepository(){
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public Integer addFileIndex(String fileName, Date dateOfLastUpdate, Date dateOfLastIndex,
			Integer numberOfTokens, String subdomain, String title) {
	
		Session session = factory.openSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
	        FileIndex fi = new FileIndex(fileName, dateOfLastUpdate, dateOfLastIndex,
	        		numberOfTokens, subdomain, title);
			id = (Integer) session.save(fi);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return id;
	}

}
