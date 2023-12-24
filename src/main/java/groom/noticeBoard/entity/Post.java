package groom.noticeBoard.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.w3c.dom.Text;

@Data
public class Post {

  private Long postId;
  private Long userId;
  private String title;
  private String content;
  private String postDate;

  private String editDate;

  public Post(Long postId, Long userId, String title, String content, String postDate) {
    this.postId = postId;
    this.userId = userId;
    this.title = title;
    this.content = content;
    this.postDate = postDate;
  }

  public Post() {
  }
}
