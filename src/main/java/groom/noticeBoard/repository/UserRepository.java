package groom.noticeBoard.repository;

import groom.noticeBoard.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRepository {

  MainRepository mainRepository = MainRepository.getMainRepository();

  @Getter
  private static final UserRepository userRepository = new UserRepository();

  public User save(User user) throws SQLException {
    String sql = "insert into users(username, password, email, register_date) values (?, ? , ?, ?)";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    try {
      conn = mainRepository.getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setString(1, user.getUsername());
      preparedStatement.setString(2, user.getPassword());
      preparedStatement.setString(3, user.getEmail());
      preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      preparedStatement.executeUpdate();
      return user;
    } catch (SQLException e) {
      log.error("DB error", e);
      log.error("SQLException Message: " + e.getMessage());
      log.error("SQLState: " + e.getSQLState());
      log.error("ErrorCode: " + e.getErrorCode());
      throw e;
    } finally {
      mainRepository.close(conn, preparedStatement, null);
    }
  }


  public User findByUsername(String username) throws SQLException {
    String sql = "select * from users where username = ?";
    return getUser(username, sql);
  }

  private User getUser(String type, String sql) throws SQLException {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User user = new User();
    try {
      conn = mainRepository.getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setString(1, type);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        log.info("user_id={}", resultSet.getLong("user_id"));
        log.info("pw={}", resultSet.getString("password"));
        log.info("username={}", resultSet.getString("username"));
        log.info("email={}", resultSet.getString("email"));
        log.info("register_date={}", resultSet.getString("register_date"));
        user.setUserId(resultSet.getLong("user_id"));
        user.setPassword(resultSet.getString("password"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setRegisterDate(resultSet.getTimestamp("register_date").toString());
      }
      return user;
    } catch (SQLException e) {
      log.error("db error", e);
      throw e;
    } finally {
      mainRepository.close(conn, preparedStatement, resultSet);
    }
  }

  public User findByEmail(String email) throws SQLException {
    String sql = "select * from users where email = ?";
    return getUser(email, sql);
  }
}
