package in.mittaluday.file_repo;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import in.mittaluday.file_model.TokenFrequency;

public class TokenFrequencyRepository {
   private static SessionFactory factory; 
   
   public TokenFrequencyRepository(){
      try{
         factory = new Configuration().configure().buildSessionFactory();
      }catch (Throwable ex) { 
         System.err.println("Failed to create sessionFactory object." + ex);
         throw new ExceptionInInitializerError(ex); 
      }
   }
   
   //BoilerPlate
   public static void main(String[] args) {
	 
      TokenFrequencyRepository tfrepo = new TokenFrequencyRepository();

      /* Add few employee records in database */
      Integer id1 = tfrepo.addTokenFrequency("hello", 10);
   }

   public Integer addTokenFrequency(String content, Integer frequency){
      
	  Session session = factory.openSession();
      Transaction tx = null;
      Integer id = null;
      try{
         tx = session.beginTransaction();
         TokenFrequency tfreq = new TokenFrequency(content, frequency);
         id = (Integer) session.save(tfreq); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
      return id;
   }
}
