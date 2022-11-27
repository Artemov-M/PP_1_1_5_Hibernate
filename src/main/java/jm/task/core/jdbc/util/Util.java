package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Util {
    // реализуйте настройку соеденения с БД

    private static final String DB_URL = "jdbc:mysql://localhost:3306/kata_pp_1_1_3";
    private static final String DB_USERNAME = "root";
    private static final String DB_PSWD = "root";
    private static Connection connection;

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

}
