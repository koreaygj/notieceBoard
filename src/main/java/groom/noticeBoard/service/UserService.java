package groom.noticeBoard.service;

import groom.noticeBoard.entity.User;
import groom.noticeBoard.error.UserAlreadyExistsException;
import groom.noticeBoard.repository.UserRepository;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

  private final UserRepository userRepository = new UserRepository();


  public User registerNewUserAccount(User user) throws SQLException, UserAlreadyExistsException {
    user = userRepository.save(user);
    return user;
  }

  public boolean emailExist(String email) throws SQLException {
    return !userRepository.findByEmail(email).isNull();
  }

  public boolean usernameExist(String username) throws SQLException {
    return !userRepository.findByUsername(username).isNull();
  }

}
