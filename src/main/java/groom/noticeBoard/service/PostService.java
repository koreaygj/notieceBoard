package groom.noticeBoard.service;

import groom.noticeBoard.entity.Post;
import groom.noticeBoard.entity.User;
import groom.noticeBoard.repository.PostRepository;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostService {

  private final PostRepository postRepository = new PostRepository();

  public Post registerPost(Post post) throws SQLException {
    post = postRepository.save(post);
    return post;
  }

  public List<Post> getAllPost() throws SQLException {
    return postRepository.getAllPosts();
  }

  public Post getPost(Long id) throws SQLException {
    return postRepository.getSinglePost(id);
  }

  public boolean canUserAccessPost(Long userId, Long postId) throws SQLException {
    Post curPost = getPost(postId);
    return curPost.getUserId().equals(userId);
  }

  public Post setUserInfo(Post curPost, User curUser) {
    curPost.setUserId(curUser.getUserId());
    curPost.setUsername(curUser.getUsername());
    return curPost;
  }
}
