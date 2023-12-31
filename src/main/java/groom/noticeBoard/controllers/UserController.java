package groom.noticeBoard.controllers;

import groom.noticeBoard.entity.User;
import groom.noticeBoard.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  // register new User
  @GetMapping("/register-new")
  public String addUserForm(Model model) {
    model.addAttribute("user", new User());
    return "form/registerNewUserForm";
  }


  @PostMapping("/register-new")
  public String registerUser(@ModelAttribute User user, BindingResult bindingResult)
      throws SQLException {
    log.info("user={}", user);
    if (user.getUsername() == null) {
      bindingResult.addError(new FieldError("user", "username", "username은 필수 항목입니다."));
    }
    if (user.getPassword() == null) {
      bindingResult.addError(new FieldError("user", "password", "password는 필수 항목입니다."));
    }
    if (userService.usernameExist(user.getUsername())) {
      bindingResult.addError(new FieldError("user", "username", "username이 이미 사용중입니다."));
    }
    if (userService.emailExist(user.getEmail())) {
      bindingResult.addError(new FieldError("user", "email", "email이 이미 사용중입니다."));
    }

    if (bindingResult.hasErrors()) {
      return "form/registerNewUserForm";
    }
    User registerUser = userService.registerNewUserAccount(user);
    return "redirect:/user/log-in";
  }

  // user login form
  @GetMapping("/log-in")
  public String userLoginForm(Model model) {
    model.addAttribute("user", new User());
    return "form/loginForm";
  }

  @PostMapping("/log-in")
  public String userLogin(@ModelAttribute User user, BindingResult bindingResult,
      HttpSession session) throws SQLException {
    log.info("user={}", user);
    if (user.getUsername() == null) {
      bindingResult.addError(new FieldError("user", "username", "username은 필수 항목입니다."));
    }
    if (user.getPassword() == null) {
      bindingResult.addError(new FieldError("user", "password", "password는 필수 항목입니다."));
    }
    if (!userService.verifyUserAccount(user)) {
      bindingResult.addError(
          new FieldError("user", "password", "username 또는 password가 올바르지 않습니다."));
    }
    if (bindingResult.hasErrors()) {
      return "form/loginForm";
    }
    User storedUser = userService.getStoredUser(user);
    session.setAttribute("user", storedUser);
    return "redirect:/posts";
  }


}
