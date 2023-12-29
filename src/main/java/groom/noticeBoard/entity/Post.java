package groom.noticeBoard.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.w3c.dom.Text;

@Data
public class Post {

  private Long postId;
  private Long userId;
  private String title;
  private String username;
  private String content;
  private String postDate;
  private String updateDate;
  private boolean isDeleted;

  public Post(Long postId, Long userId, String title, String username, String content,
      String postDate, boolean isDeleted) {
    this.postId = postId;
    this.userId = userId;
    this.title = title;
    this.username = username;
    this.content = content;
    this.postDate = postDate;
    this.isDeleted = isDeleted;
  }

  public Post() {
  }
}
