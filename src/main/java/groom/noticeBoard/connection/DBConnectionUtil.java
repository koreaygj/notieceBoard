package groom.noticeBoard.connection;

import static groom.noticeBoard.connection.ConnectionConst.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionUtil {

  public static Connection getConnection() {
    try {
      Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
      log.info("get connection = {}, class = {}", connection, connection.getClass());
      return connection;
    } catch (SQLException e) {
      log.error("SQLException Message: " + e.getMessage());
      log.error("SQLState: " + e.getSQLState());
      log.error("ErrorCode: " + e.getErrorCode());
      throw new IllegalStateException();
    }
  }
}
