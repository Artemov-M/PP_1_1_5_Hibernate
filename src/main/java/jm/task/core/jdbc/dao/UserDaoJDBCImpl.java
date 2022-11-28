package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(
            """
                    CREATE TABLE IF NOT EXISTS user (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        age TINYINT NOT NULL CHECK (age >= 0)
                    )
                """
            );
        } catch (SQLException ignore) {
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS user;");
        } catch (SQLException ignore) {
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             var statement = connection.prepareStatement(
                     "INSERT INTO user (name, last_name, age) VALUES (?, ?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        } catch (SQLException ignore) {
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             var statement = connection.prepareStatement(
                     "DELETE FROM user WHERE id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException ignore) {
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(
                    "SELECT id, name, last_name, age FROM user");
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
            statement.execute("TRUNCATE TABLE user");
        } catch (SQLException ignore) {
        }
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setAge(resultSet.getByte("age"));
        return user;
    }
}
