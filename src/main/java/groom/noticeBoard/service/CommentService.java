package groom.noticeBoard.service;

import groom.noticeBoard.entity.Comment;
import groom.noticeBoard.repository.CommentRepository;
import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

  CommentRepository commentRepository = CommentRepository.getCommentRepository();

  public Comment registerComment(Comment comment) throws SQLException {
    return commentRepository.save(comment);
  }

  public List<Comment> getComments(Long postId) throws SQLException {
    return commentRepository.searchCommentByPostId(postId);
  }

  public void deleteComment(Long commentId) throws SQLException {
    commentRepository.delete(commentId);
  }
}
