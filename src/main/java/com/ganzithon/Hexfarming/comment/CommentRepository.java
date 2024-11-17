package com.ganzithon.Hexfarming.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ganzithon.Hexfarming.post.Post;
import com.ganzithon.Hexfarming.domain.user.User;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_PostId(Long postId);
    boolean existsByPostAndWriter(Post post, User writer);
    @Query("SELECT AVG(c.score) FROM Comment c WHERE c.post.id = :postId")
    Double getAverageScoreByPostId(@Param("postId") Long postId);
    @Query("SELECT AVG(c.score) FROM Comment c WHERE c.post.id = :postId AND c.post.timer > CURRENT_TIMESTAMP")
    Double getAverageScoreByPostIdWithRemainingTime(@Param("postId") Long postId);


}

