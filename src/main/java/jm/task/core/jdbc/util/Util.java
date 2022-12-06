package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Util {
    // реализуйте настройку соеденения с БД

    private static final String DB_URL = "jdbc:mysql://localhost:3306/kata_pp_1_1_3";
    private static final String DB_USERNAME = "root";
    private static final String DB_PSWD = "root";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_DIALECT = "org.hibernate.dialect.MySQL8Dialect";

    private static Connection connection;
    private static Configuration configuration;
    private static SessionFactory sessionFactory;

    private Util() {
    }

    public static Connection getConnection() throws SQLException {

        if (connection != null) {
            if (!connection.isClosed()) {
                return connection;
            }
        }

        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PSWD);
        return connection;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory != null) {
            if (!sessionFactory.isClosed()) {
                return sessionFactory;
            }
        }
        if (configuration == null) {
            setConfiguration();
        }
        sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }

    private static void setConfiguration() {
        configuration = new Configuration()
                .setProperty("hibernate.connection.url", DB_URL)
                .setProperty("hibernate.connection.username", DB_USERNAME)
                .setProperty("hibernate.connection.password", DB_PSWD)
                .setProperty("hibernate.connection.driver_class", DB_DRIVER)
                .setProperty("hibernate.dialect", DB_DIALECT);

        configuration.addAnnotatedClass(User.class);
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static void silentRollback(Transaction t) {
        if (t != null) {
            try {
                t.rollback();
            } catch (Exception ignore) {
            }
        }
    }

}
