package groom.noticeBoard.controllers;

import groom.noticeBoard.entity.Post;
import groom.noticeBoard.entity.User;
import groom.noticeBoard.service.PostService;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    return "form/postForm";
  }

  @PostMapping("/new-post")
  public String registerNewPost(@ModelAttribute Post post, HttpSession session)
      throws SQLException {
    Post storedPost = postService.registerPost(
        postService.setUserInfo(post, (User) session.getAttribute("user")));
    return "posts";
  }

  @GetMapping("/details/{id}")
  public String getPostDetails(@PathVariable("id") Long postId, Model model) throws SQLException {
    model.addAttribute("post", postService.getPost(postId));
    return "details/post";
  }

  @GetMapping("/edit/{id}")
  public String editPost(@PathVariable("id") Long postId, Model model, HttpSession session,
      RedirectAttributes redirectAttributes)
      throws SQLException {
    User curUser = (User) session.getAttribute("user");
    if (curUser == null) {
      redirectAttributes.addFlashAttribute("warningMsg", "로그인이 필요한 기능입니다.");
      return "redirect:/user/log-in";
    }
    if (!postService.canUserAccessPost(curUser.getUserId(), postId)) {
      redirectAttributes.addFlashAttribute("warningMsg", "수정 권한이 없습니다.");
      return "redirect:/posts";
    }
    model.addAttribute("post", postService.getPost(postId));
    return "/form/postForm";
  }

  @PostMapping("/edit/{id}")
  public String updatePost(Model model, @ModelAttribute Post post, @PathVariable String id)
      throws SQLException {
    log.info("post={}", model);
    post.setPostId(Long.valueOf(id));
    Post updatedPost = postService.updatePost(
        postService.setUpdatePost(post));
    return "redirect:/posts/details/" + id;
  }


  @RequestMapping("/delete/{id}")
  public String updatePost(Model model, @PathVariable("id") Long postId, HttpSession session,
      RedirectAttributes redirectAttributes) throws SQLException {
    User curUser = (User) session.getAttribute("user");
    if (curUser == null) {
      redirectAttributes.addFlashAttribute("warningMsg", "로그인이 필요한 기능입니다.");
      return "redirect:/user/log-in";
    }
    if (!postService.canUserAccessPost(curUser.getUserId(), postId)) {
      redirectAttributes.addFlashAttribute("warningMsg", "수정 권한이 없습니다.");
      return "redirect:/posts";
    }
    redirectAttributes.addFlashAttribute("warningMsg", "삭제되었습니다.");
    postService.deletePost(postId);
    return "redirect:/posts";
  }


}
