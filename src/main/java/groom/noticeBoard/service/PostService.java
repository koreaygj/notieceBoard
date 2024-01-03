package groom.noticeBoard.service;

import groom.noticeBoard.entity.Post;
import groom.noticeBoard.entity.User;
import groom.noticeBoard.repository.PostRepository;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostService {

  private final PostRepository postRepository = new PostRepository();

  public Post registerPost(Post post) throws SQLException {
    return postRepository.save(post);
  }

  public Post updatePost(Post post) throws SQLException {
    return postRepository.update(post);
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

  public Post setUpdatePost(Post curPost) throws SQLException {
    Post storedPost = postRepository.getSinglePost(curPost.getPostId());
    storedPost.setTitle(curPost.getTitle());
    storedPost.setContent(curPost.getContent());
    return storedPost;
  }

  public void deletePost(Long postId) throws SQLException {
    postRepository.delete(postId);
  }
}
