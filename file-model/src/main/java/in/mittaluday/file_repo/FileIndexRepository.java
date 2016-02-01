package in.mittaluday.file_repo;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import in.mittaluday.file_model.FileIndex;
import in.mittaluday.file_model.ThreeGramFrequency;
import in.mittaluday.file_model.TokenFrequency;

public class FileIndexRepository {
	private static SessionFactory factory;

	// BoilerPlate
	public static void main(String[] args) {

		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		FileIndexRepository tfrepo = new FileIndexRepository();

		Integer id1 = tfrepo.addFileIndex("purvi.txt", new Date(), new Date(), 100, "aadi.com");
	}

	public Integer addFileIndex(String fileName, Date dateOfLastUpdate, Date dateOfLastIndex,
			Integer numberOfTokens, String subdomain) {

		Session session = factory.openSession();
		Transaction tx = null;
		Integer id = null;
		try {
			tx = session.beginTransaction();
	        FileIndex fi = new FileIndex(fileName, dateOfLastUpdate, dateOfLastIndex,
	        		numberOfTokens, subdomain);
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
