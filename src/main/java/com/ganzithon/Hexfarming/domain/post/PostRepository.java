package com.ganzithon.Hexfarming.domain.post;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface PostRepository extends CrudRepository<Post, Long> {
    @Override
    List<Post> findAll();

    Optional<List<Post>> findByTimerBeforeAndIsTimerOverFalse(LocalDateTime dateTime);
}
