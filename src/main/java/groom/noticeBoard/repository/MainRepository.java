package groom.noticeBoard.repository;

import groom.noticeBoard.connection.DBConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainRepository {

  @Getter
  private static final MainRepository mainRepository = new MainRepository();

  public Connection getConnection() {
    return DBConnectionUtil.getConnection();
  }

  public void close(Connection conn, PreparedStatement preparedStatement, ResultSet resultSet) {
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (SQLException e) {
        log.info("error", e);
      }
    }

    if (preparedStatement != null) {
      try {
        preparedStatement.close();
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


}
