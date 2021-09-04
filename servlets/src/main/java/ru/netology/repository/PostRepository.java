package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// Stub
public class PostRepository {
  private static final AtomicInteger idCounter = new AtomicInteger(0);
  private static final ConcurrentHashMap<Integer, Post> postMap = new ConcurrentHashMap<>();

  public List<Post> all() {

    return new ArrayList<>(postMap.values());
  }

  public Optional<Post> getById(long id) {
    if(id < postMap.size() - 1 && id >= 0) {
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
      postMap.replace(postId, post);
    }
    return post;
  }

  public void removeById(long id) {
    for(Map.Entry<Integer, Post> post : postMap.entrySet()) {
      if(post.getKey() == id) {
        postMap.remove(post.getKey());
      }
    }
  }
}
