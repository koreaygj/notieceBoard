package groom.noticeBoard.controllers;

import groom.noticeBoard.entity.User;
import groom.noticeBoard.repository.UserRepository;
import groom.noticeBoard.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/add")
  public String addUserForm(Model model) {
    model.addAttribute("user", new User());
    return "form/userForm";
  }


  @PostMapping("/add")
  public String registerUser(@ModelAttribute User user) {
    log.info("user={}", user);
    User registerUser = userService.registerNewUserAccount(user);
    return "user";
  }
}
