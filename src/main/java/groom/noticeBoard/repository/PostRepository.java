package groom.noticeBoard.repository;

import groom.noticeBoard.entity.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostRepository {

  MainRepository mainRepository = MainRepository.getMainRepository();

  @Getter
  private static final PostRepository postRepository = new PostRepository();

  public Post save(Post post) throws SQLException {
    String sql = "insert into posts(user_id, title, username, content, post_date, isDeleted) values (?, ? , ?, ?, ?, ?)";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    try {
      conn = mainRepository.getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setLong(1, post.getUserId());
      preparedStatement.setString(2, post.getTitle());
      preparedStatement.setString(3, post.getUsername());
      preparedStatement.setString(4, post.getContent());
      preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
      preparedStatement.setBoolean(6, false);
      preparedStatement.executeUpdate();
      return post;
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

  public Post update(Post post) throws SQLException {
    String sql = "update posts set title = ?, content = ?, update_date = ? where post_id = ?";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    try {
      conn = mainRepository.getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setString(1, post.getTitle());
      preparedStatement.setString(2, post.getContent());
      preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      preparedStatement.setLong(4, post.getPostId());
      preparedStatement.executeUpdate();
      return post;
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

  public void delete(Long postId) throws SQLException {
    String sql = "update posts set isDeleted = true where post_id = ?";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    try {
      conn = mainRepository.getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setLong(1, postId);
      preparedStatement.executeUpdate();
      return;
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

  public List<Post> getAllPosts() throws SQLException {
    String sql = "select * from posts where isDeleted = false";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    List<Post> posts = new ArrayList<>();
    try {
      conn = mainRepository.getConnection();
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
      mainRepository.close(conn, preparedStatement, resultSet);
    }
  }

  public Post getSinglePost(Long id) throws SQLException {
    String sql = "select * from posts where post_id = ? and isDeleted = false";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Post storedPost = new Post();
    try {
      conn = mainRepository.getConnection();
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
      mainRepository.close(conn, preparedStatement, resultSet);
    }
  }

  private static Post setPost(ResultSet resultSet) throws SQLException {
    Post post = new Post(
        resultSet.getLong("post_id"),
        resultSet.getLong("user_id"),
        resultSet.getString("title"),
        resultSet.getString("username"),
        resultSet.getString("content"),
        resultSet.getString("post_date"),
        resultSet.getBoolean("isDeleted")
    );
    if (resultSet.getString("update_date") != null) {
      post.setUpdateDate(resultSet.getString("update_date"));
    }
    return post;
  }


}
