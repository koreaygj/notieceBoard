package groom.noticeBoard.entity;

import lombok.Data;

@Data
public class User {
  private Long userId;
  private String username;
  private String password;
  private String email;
  private String registerDate;

  public User(Long userId, String username, String password, String email, String registerDate) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.email = email;
    this.registerDate = registerDate;
  }
  public User(){}
}
