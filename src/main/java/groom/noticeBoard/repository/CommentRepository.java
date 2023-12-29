package groom.noticeBoard.repository;

import groom.noticeBoard.entity.Comment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentRepository {

  MainRepository mainRepository = MainRepository.getMainRepository();

  @Getter
  private static final CommentRepository commentRepository = new CommentRepository();

  public Comment save(Comment comment) throws SQLException {
    String sql = "insert into comments(post_id, user_id, username, content, comment_date, isDeleted) values (?, ?, ?, ?, ? , ?)";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    try {
      conn = mainRepository.getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setLong(1, comment.getPostId());
      preparedStatement.setLong(2, comment.getUserId());
      preparedStatement.setString(3, comment.getUsername());
      preparedStatement.setString(4, comment.getContent());
      preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
      preparedStatement.setBoolean(6, false);
      preparedStatement.executeUpdate();
      return comment;
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

  public List<Comment> searchCommentByPostId(Long postId) throws SQLException {
    String sql = "select * from comments where post_id = ? and isDeleted = false";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    List<Comment> comments = new ArrayList<>();
    try {
      conn = mainRepository.getConnection();
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setLong(1, postId);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        Comment comment = setComment(resultSet);
        comments.add(comment);
      }
      return comments;
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

  private Comment setComment(ResultSet resultSet) throws SQLException {
    Comment comment = new Comment(
        resultSet.getLong("comment_id"),
        resultSet.getLong("post_id"),
        resultSet.getLong("user_id"),
        resultSet.getString("username"),
        resultSet.getString("content"),
        resultSet.getString("comment_date"),
        resultSet.getBoolean("isDeleted")
    );
    if (resultSet.getString("update_date") != null) {
      comment.setUpdateDate(resultSet.getString("update_date"));
    }
    return comment;
  }
}
