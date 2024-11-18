package com.ganzithon.Hexfarming.domain.post;

import com.ganzithon.Hexfarming.global.enumeration.Ability;
import com.ganzithon.Hexfarming.global.enumeration.Ability;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


public interface PostRepository extends CrudRepository<Post, Long> {
    @Override
    List<Post> findAll();

    Optional<List<Post>> findByTimerBeforeAndIsTimerOverFalse(LocalDateTime dateTime);

    // 전체 카테고리: 조회수 기준 상위 2개 게시물 가져오기
    @Query("SELECT p FROM Post p ORDER BY p.view DESC")
    List<Post> findTopByOrderByViewDesc(Pageable pageable);

    // 카테고리별: 조회수 기준 상위 2개 게시물 가져오기
    @Query("SELECT p FROM Post p WHERE p.ability = :ability ORDER BY p.view DESC")
    List<Post> findTopByCategoryOrderByViewDesc(@Param("ability") Ability ability, Pageable pageable);

    // 전체 카테고리: 마감 임박 게시글 상위 2개 가져오기
    @Query("SELECT p FROM Post p WHERE p.isTimerOver = false ORDER BY p.timer ASC")
    List<Post> findTopByOrderByTimerAsc(Pageable pageable);

    // 카테고리별: 마감 임박 게시글 상위 2개 가져오기
    @Query("SELECT p FROM Post p WHERE p.ability = :ability AND p.isTimerOver = false ORDER BY p.timer ASC")
    List<Post> findTopByCategoryAndOrderByTimerAsc(@Param("ability") Ability ability, Pageable pageable);
    Optional<List<Post>> findByTitleContaining(String titleContains);
    Optional<List<Post>> findByTitleContainingAndAbility(String titleContains, Ability ability);
}
