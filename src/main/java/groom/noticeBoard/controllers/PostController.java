package groom.noticeBoard.controllers;

import groom.noticeBoard.entity.Comment;
import groom.noticeBoard.entity.Post;
import groom.noticeBoard.entity.User;
import groom.noticeBoard.service.CommentService;
import groom.noticeBoard.service.PostService;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@Controller
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  private final CommentService commentService;

  public PostController(PostService postService, CommentService commentService) {
    this.postService = postService;
    this.commentService = commentService;
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
    return "redirect:posts";
  }

  @GetMapping("/details/{id}")
  public String getPostDetails(@PathVariable("id") Long postId, Model model,
      RedirectAttributes redirectAttributes) throws SQLException {
    Post curPost = postService.getPost(postId);
    if (curPost.isDeleted()) {
      redirectAttributes.addFlashAttribute("warningMsg", "삭제된 post입니다.");
      return "redirect:posts";
    }
    model.addAttribute("post", curPost);
    model.addAttribute("comment", new Comment());
    model.addAttribute("comments", commentService.getComments(postId));
    return "details/post";
  }

  @GetMapping("/edit/{id}")
  public String editPost(@PathVariable("id") Long postId, Model model, HttpSession session,
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

  @PostMapping("comment/{postId}")
  public String addNewComment(@PathVariable("postId") Long postId, Model model, HttpSession session,
      @ModelAttribute Comment comment, RedirectAttributes redirectAttributes)
      throws SQLException {
    Post curPost = postService.getPost(postId);
    User curUser = (User) session.getAttribute("user");
    if (curPost.isDeleted()) {
      redirectAttributes.addFlashAttribute("warningMsg", "삭제된 post입니다.");
      return "redirect:/posts";
    }
    log.info("user={}", curUser);
    if (curUser == null) {
      redirectAttributes.addFlashAttribute("warningMsg", "로그인이 필요한 기능입니다.");
      return "redirect:/user/log-in";
    }
    comment.setPostId(postId);
    comment.setUserId(curUser.getUserId());
    comment.setUsername(curUser.getUsername());
    comment.setDeleted(false);
    log.info("comment={}", comment);
    commentService.registerComment(comment);
    return "redirect:/posts/details/" + postId;
  }

}
