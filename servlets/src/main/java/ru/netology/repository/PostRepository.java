package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// Stub
public class PostRepository {
  private final AtomicInteger idCounter = new AtomicInteger(0);
  private final ConcurrentHashMap<Integer, Post> postMap = new ConcurrentHashMap<>();

  public List<Post> all() {
    return postMap.values().stream()
            .filter(post -> !post.isRemoved())
            .collect(Collectors.toList());
  }

  public Optional<Post> getById(long id) {
    if(id < postMap.size() && id >= 0) {
      if(idIsExistsAndNotRemoved((int) id)) {
        return Optional.of(postMap.get((int) id));
      }
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    int postId = (int) post.getId();
    if(postId == 0) {
      int id = idCounter.getAndIncrement();
      post.setId(id);
      postMap.putIfAbsent(id, post);
    } else if(postId > 0 && idIsExistsAndNotRemoved(postId)) {
      postMap.replace(postId, post);
    }
    return post;
  }

  private boolean idIsExistsAndNotRemoved(int id) {
    for(Map.Entry<Integer, Post> post : postMap.entrySet()) {
      if(post.getKey() == id && !post.getValue().isRemoved()) {
        return true;
      }
    }
    return false;
  }

  public void removeById(long id) {
    for(Map.Entry<Integer, Post> post : postMap.entrySet()) {
      if(post.getKey() == id) {
        postMap.get((int) id).removeIt();
      }
    }
  }
}
