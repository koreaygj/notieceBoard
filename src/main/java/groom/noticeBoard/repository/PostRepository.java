package groom.noticeBoard.repository;

import groom.noticeBoard.connection.DBConnectionUtil;
import groom.noticeBoard.entity.Post;
import groom.noticeBoard.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostRepository {

  public Post save(Post post) throws SQLException {
    String sql = "insert into posts(user_id, title, content, post_date) values (?, ? , ?, ?)";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    try {
      conn = getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setLong(1, post.getUserId());
      preparedStatement.setString(2, post.getTitle());
      preparedStatement.setString(3, post.getContent());
      preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      preparedStatement.executeUpdate();
      return post;
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

  private void close(Connection conn, PreparedStatement preparedStatement, ResultSet resultSet) {

  }

  private Connection getConnection() {
    return DBConnectionUtil.getConnection();
  }
}
