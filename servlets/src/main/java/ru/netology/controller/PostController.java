package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
  private final String APPLICATION_JSON = "application/json";
  private final PostService service;
  private final Gson JSON;

  public PostController(PostService service) {
    this.service = service;
    this.JSON = new Gson();
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final var data = service.all();
    response.getWriter().print(JSON.toJson(data));
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);

    response.getWriter().print(JSON.toJson(service.getById(id)));
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);

    final var post = JSON.fromJson(body, Post.class);

    final var data = service.save(post);
    response.getWriter().print(JSON.toJson(data));
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    service.removeById(id);
    response.getWriter().print("{delete: true}");
  }
}
