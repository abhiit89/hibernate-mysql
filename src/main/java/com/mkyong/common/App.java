package com.mkyong.common;
import org.hibernate.Session;
import com.mkyong.persistence.HibernateUtil;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Maven + Hibernate + MySQL");
        Stock stock = new Stock();
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        session.beginTransaction();
//        stock.setStockCode("3456");
//        stock.setStockName("abcd");
//        session.save(stock);
//        session.getTransaction().commit();
//        session.close();
//        stock = null;
        Session session1 = HibernateUtil.getSessionFactory().openSession();
        session1.beginTransaction();
        List list = session1.createQuery(" from Stock").list();
        //stock = (Stock) session1.get(Stock.class,2);
        Iterator itr = list.iterator();
        while(itr.hasNext())
        {
            Stock user = (Stock)itr.next();
            System.out.println(user);
        }
        session1.getTransaction().commit();
        session1.close();
    }
}
