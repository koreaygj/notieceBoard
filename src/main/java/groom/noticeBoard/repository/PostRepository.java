package groom.noticeBoard.repository;

import groom.noticeBoard.connection.DBConnectionUtil;
import groom.noticeBoard.entity.Post;
import groom.noticeBoard.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostRepository {

  public Post save(Post post) throws SQLException {
    String sql = "insert into posts(user_id, title, username, content, post_date) values (?, ? , ?, ?, ?)";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    try {
      conn = getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setLong(1, post.getUserId());
      preparedStatement.setString(2, post.getTitle());
      preparedStatement.setString(3, post.getUsername());
      preparedStatement.setString(4, post.getContent());
      preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
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

  public List<Post> getAllPosts() throws SQLException {
    String sql = "select * from posts";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet;
    List<Post> posts = new ArrayList<>();
    try {
      conn = getConnection();
      preparedStatement = conn.prepareStatement(sql);
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Post post = setPost(resultSet);
        posts.add(post);
      }
      return posts;
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

  public Post getSinglePost(Long id) throws SQLException {
    String sql = "select * from posts where post_id = ?";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet;
    Post storedPost = new Post();
    try {
      conn = getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setLong(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        storedPost = setPost(resultSet);
      }
      return storedPost;
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

  private static Post setPost(ResultSet resultSet) throws SQLException {
    Post post = new Post();
    post.setUserId(resultSet.getLong("user_id"));
    post.setPostId(resultSet.getLong("post_id"));
    post.setUsername(resultSet.getString("username"));
    post.setTitle(resultSet.getString("title"));
    post.setContent(resultSet.getString("content"));
    post.setPostDate(resultSet.getString("post_date"));
    if (resultSet.getString("update_date") != null) {
      post.setUpdateDate(resultSet.getString("update_date"));
    }
    return post;
  }


  private void close(Connection conn, PreparedStatement preparedStatement, ResultSet resultSet) {

  }

  private Connection getConnection() {
    return DBConnectionUtil.getConnection();
  }
}
