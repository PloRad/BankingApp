package dao;

import model.Account;
import org.hibernate.Session;

import java.util.List;

public class AccountDao extends GenericDao<Account> {

    public Account findByIban(String receiverIban) {
        Session session = sessionFactory.openSession();

        List<Account> result = session.createQuery("FROM Account a WHERE a.iban = :iban")
                .setParameter("iban", receiverIban)
                .list();
        System.out.println(result);
        session.close();
        if(result.size() == 0){
            return null;
        }else return result.get(0);
    }
}
