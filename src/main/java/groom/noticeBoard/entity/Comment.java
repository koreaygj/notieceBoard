package groom.noticeBoard.entity;

import lombok.Data;

@Data
public class Comment {

  private Long commentId;
  private Long postId;
  private Long userId;
  private String content;
  private String commentDate;
  private String updateDate;

  private boolean isDeleted;

  public Comment(Long commentId, Long postId, Long userId, String content, String commentDate,
      boolean isDeleted) {
    this.commentId = commentId;
    this.postId = postId;
    this.userId = userId;
    this.content = content;
    this.commentDate = commentDate;
    this.isDeleted = isDeleted;
  }

  public Comment() {
  }
}
