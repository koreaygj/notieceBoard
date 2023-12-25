package groom.noticeBoard.controllers;

import groom.noticeBoard.entity.Post;
import groom.noticeBoard.entity.User;
import groom.noticeBoard.service.PostService;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@Controller
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping
  public String getAllPost(Model model) throws SQLException {
    List<Post> storedPost = postService.getAllPost();
    model.addAttribute("posts", storedPost);
    return "posts";
  }


  @GetMapping("/new-post")
  public String newPostForm(Model model, HttpSession session,
      RedirectAttributes redirectAttributes) {
    User curUser = (User) session.getAttribute("user");
    if (curUser == null) {
      redirectAttributes.addFlashAttribute("warningMsg", "로그인이 필요한 기능입니다.");
      return "redirect:/user/log-in";
    }
    model.addAttribute("post", new Post());
    return "form/newPostForm";
  }

  @PostMapping("/new-post")
  public String registerNewPost(@ModelAttribute Post post, HttpSession session)
      throws SQLException {
    User user = (User) session.getAttribute("user");
    post.setUserId(user.getUserId());
    post.setUsername(user.getUsername());
    Post storePost = postService.registerPost(post);
    return "posts";
  }

}
