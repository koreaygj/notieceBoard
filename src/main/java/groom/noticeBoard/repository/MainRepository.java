package groom.noticeBoard.repository;

import static groom.noticeBoard.connection.ConnectionConst.PASSWORD;
import static groom.noticeBoard.connection.ConnectionConst.URL;
import static groom.noticeBoard.connection.ConnectionConst.USERNAME;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

@Slf4j
public class MainRepository {


  @Getter
  private final static MainRepository mainRepository = dataSourceConnectionPool();

  private final DataSource dataSource;

  public MainRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  static public MainRepository dataSourceConnectionPool() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(URL);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setMaximumPoolSize(10);
    dataSource.setPoolName("noticeBoardPool");
    return new MainRepository(dataSource);
  }

  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  public void close(Connection conn, PreparedStatement preparedStatement, ResultSet resultSet) {
    JdbcUtils.closeResultSet(resultSet);
    JdbcUtils.closeStatement(preparedStatement);
    JdbcUtils.closeConnection(conn);
  }
}
