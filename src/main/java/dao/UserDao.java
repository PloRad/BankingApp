package dao;

import model.User;
import org.hibernate.Session;
import java.util.List;

public class UserDao extends dao.GenericDao<User> {

    public User findByUsername(String username) {
        Session session = sessionFactory.openSession();
        List <User> result = session.createQuery("FROM User u WHERE u.username = :username")
                .setParameter("username", username)
                .list();
        System.out.println(result);
        session.close();
        if(result.size() == 0){
            return null;
        }else return result.get(0);
    }
}
