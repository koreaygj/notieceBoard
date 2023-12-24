package groom.noticeBoard.controllers;

import groom.noticeBoard.entity.Post;
import groom.noticeBoard.entity.User;
import groom.noticeBoard.service.PostService;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
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
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/new-post")
  public String newPostForm(Model model) {
    model.addAttribute("post", new Post());
    return "form/newPostForm";
  }

  @PostMapping("/new-post")
  public String registerNewPost(@ModelAttribute Post post, HttpSession session)
      throws SQLException {
    User user = (User) session.getAttribute("user");
    log.info("user = {}", user);
    log.info("user id={}", user.getUserId());
    post.setUserId(user.getUserId());
    Post storePost = postService.registerPost(post);
    return "posts";
  }

}
