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
    if (email.isEmpty()) {
      return false;
    }
    return !userRepository.findByEmail(email).isNull();
  }

  public boolean usernameExist(String username) throws SQLException {
    return !userRepository.findByUsername(username).isNull();
  }

  public boolean verifyUserAccount(User user) throws SQLException {
    User storeUser = userRepository.findByUsername(user.getUsername());
    return storeUser.getPassword().equals(user.getPassword());
  }

  public User getStoredUser(User user) throws SQLException {
    return userRepository.findByUsername(user.getUsername());
  }

}
