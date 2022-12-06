package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction t = null;
        try (Session session = sessionFactory.openSession()) {
            t = session.beginTransaction();
            session.createNativeQuery(
                """
                    CREATE TABLE IF NOT EXISTS user (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        age TINYINT NOT NULL CHECK (age >= 0)
                    )
                """)
                    .executeUpdate();
            t.commit();
        } catch (Exception e) {
                Util.silentRollback(t);
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction t = null;
        try (Session session = sessionFactory.openSession()) {
            t = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS user;")
                    .executeUpdate();
            t.commit();
        } catch (Exception e) {
            Util.silentRollback(t);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction t = null;
        try (Session session = sessionFactory.openSession()) {
            t = session.beginTransaction();
            session.save(new User(name, lastName, age));
            t.commit();
        } catch (Exception e) {
            Util.silentRollback(t);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction t = null;
        try (Session session = sessionFactory.openSession()) {
            t = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            t.commit();
        } catch (Exception e) {
            Util.silentRollback(t);
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction t = null;
        List<User> result = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            t = session.beginTransaction();
            result = session.createQuery("select u from User u", User.class)
                    .list();
            t.commit();
        } catch (Exception e) {
            if (result.size() > 0) {
                result = new ArrayList<>();
            }
            Util.silentRollback(t);
        }
        return result;
    }

    @Override
    public void cleanUsersTable() {
        Transaction t = null;
        try (Session session = sessionFactory.openSession()) {
            t = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE user;")
                    .executeUpdate();
            t.commit();
        } catch (Exception e) {
            Util.silentRollback(t);
        }
    }
}
