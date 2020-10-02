package util;

import model.Account;
import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {

  private static SessionFactory sessionFactory;

  public static SessionFactory getSessionFactory() {

    if (sessionFactory == null) {
      /*START BOILER PLATE CODE */
      Configuration configuration = new Configuration();
      Properties settings = new Properties();

      settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
      settings.put(Environment.URL, "jdbc:mysql://localhost:3306/proiectgrup?serverTimezone=UTC");
      settings.put(Environment.USER, "root");
      settings.put(Environment.PASS, "@Radu246892");
      settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
      settings.put(Environment.SHOW_SQL, "true");
      settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
      settings.put(Environment.HBM2DDL_AUTO, "update");
      configuration.setProperties(settings);



      configuration.addAnnotatedClass(User.class);
      configuration.addAnnotatedClass(Account.class);



      configuration.setProperties(settings);
      ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
          .applySettings(configuration.getProperties()).build();
      sessionFactory = configuration.buildSessionFactory(serviceRegistry);
      /*END BOILER PLATE CODE*/
    }

    return sessionFactory;
  }
}
