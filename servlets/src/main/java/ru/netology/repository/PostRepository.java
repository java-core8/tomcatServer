package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PostRepository {
  private final AtomicInteger idCounter = new AtomicInteger(0);
  private final ConcurrentHashMap<Integer, Post> postMap = new ConcurrentHashMap<>();

  public List<Post> all() {
    return new ArrayList<>(postMap.values());
  }

  public Optional<Post> getById(long id) {
    if(id < postMap.size() && id >= 0) {
      return Optional.of(postMap.get((int) id));
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    int postId = (int) post.getId();
    if(postId == 0) {
      int id = idCounter.getAndIncrement();
      post.setId(id);
      postMap.putIfAbsent(id, post);
    } else if(postId > 0) {
      if(!idIsExists(postId)) {
        return getStubPost(postId);
      }
      postMap.replace(postId, post);
    }
    return post;
  }

  private Post getStubPost(int id) {
    Post stubPost = new Post();
    stubPost.setContent("Number of post - " +  id +  " isn't exist");
    return stubPost;
  }

  private boolean idIsExists(int id) {
    for(Map.Entry<Integer, Post> post : postMap.entrySet()) {
      if(post.getKey() == id) {
        System.out.println("true");
        return true;
      }
    }
    return false;
  }

  public void removeById(long id) {
    for(Map.Entry<Integer, Post> post : postMap.entrySet()) {
      if(post.getKey() == id) {
        postMap.remove(post.getKey());
      }
    }
  }
}
