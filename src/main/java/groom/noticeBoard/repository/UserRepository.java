package groom.noticeBoard.repository;

import groom.noticeBoard.connection.DBConnectionUtil;
import groom.noticeBoard.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRepository {

  public User save(User user) throws SQLException {
    String sql = "insert into users(username, password, email, register_date) values (?, ? , ?, ?)";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    try {
      conn = getConnection();
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
      close(conn, preparedStatement, null);
    }
  }

  private void close(Connection conn, Statement statement, ResultSet resultSet) {
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (SQLException e) {
        log.info("error", e);
      }
    }

    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        log.info("error", e);
      }
    }

    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        log.info("error", e);
      }
    }
  }

  private Connection getConnection() {
    return DBConnectionUtil.getConnection();
  }
}
