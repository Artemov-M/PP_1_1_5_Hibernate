package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS user (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                age TINYINT NOT NULL CHECK (age >= 0)
            )
            """;

    private final String DROP_TABLE = """   
            DROP TABLE IF EXISTS user;
            """;

    private final String SAVE_USER = """
            INSERT INTO user (name, last_name, age)
            VALUES (?, ?, ?)
            """;

    private final String REMOVE_USER_BY_ID = """
            DELETE FROM user
            WHERE id = ?
            """;

    private final String GET_ALL_USERS = """
            SELECT id, name, last_name, age
            FROM user;
            """;

    private final String CLEAN_USERS_TABLE = """
            TRUNCATE TABLE user;
            """;

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE);
        } catch (SQLException ignore) {
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(DROP_TABLE);
        } catch (SQLException ignore) {
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             var statement = connection.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        } catch (SQLException ignore) {
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             var statement = connection.prepareStatement(REMOVE_USER_BY_ID)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException ignore) {
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(GET_ALL_USERS);
            while (resultSet.next()) {
                result.add(createUser(resultSet));
            }
        } catch (SQLException ignore) {
        }
        return result;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_USERS_TABLE);
        } catch (SQLException ignore) {
        }
    }

    private User createUser (ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setAge(resultSet.getByte("age"));
        return user;
    }
}
