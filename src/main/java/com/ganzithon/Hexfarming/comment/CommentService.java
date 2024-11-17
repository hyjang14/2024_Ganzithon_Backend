package com.ganzithon.Hexfarming.comment;

import com.ganzithon.Hexfarming.dto.fromClient.CommentRequestDto;
import com.ganzithon.Hexfarming.dto.fromServer.CommentResponseDto;
import com.ganzithon.Hexfarming.domain.user.User;
import com.ganzithon.Hexfarming.domain.user.UserRepository;
import com.ganzithon.Hexfarming.post.Post;
import com.ganzithon.Hexfarming.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, String username) {
        User user = getUserByUsername(username);
        Post post = getPostById(postId);

        // 댓글 중복 여부 확인
        boolean alreadyCommented = commentRepository.existsByPostAndWriter(post, user);
        if (alreadyCommented) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 해당 게시물에 댓글을 작성하셨습니다.");
        }

        int validatedScore = validateScore(commentRequestDto.getScore());

        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        comment.setWriterNickname(user.getNickname());
        comment.setWriterTier(user.getTier());
        comment.setScore(validatedScore);
        comment.setPost(post);
        comment.setWriter(user);

        commentRepository.save(comment);

        // 게시글 점수 합계
        post.setScoreSum(post.getScoreSum() + validatedScore);
        postRepository.save(post);

        return mapToDto(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto commentRequestDto, String username) {
        Post post = getPostById(postId);

        Comment comment = getCommentByIdAndVerifyOwnership(commentId, username);

        comment.setContent(commentRequestDto.getContent());

        postRepository.save(post);

        return mapToDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long postId, Long commentId, String username) {
        Post post = getPostById(postId);
        Comment comment = getCommentByIdAndVerifyOwnership(commentId, username);

        // 게시글 점수 합계
        post.setScoreSum(post.getScoreSum() - comment.getScore());
        postRepository.save(post);

        commentRepository.delete(comment);
    }

    // 유틸리티 메서드
    private User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "작성자를 찾을 수 없습니다.");
        }
        return user;
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));
    }

    private Comment getCommentByIdAndVerifyOwnership(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (!comment.getWriter().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 댓글만 수정/삭제할 수 있습니다.");
        }
        return comment;
    }

    private int validateScore(Integer score) {
        if (score == null) {
            return 150; // 기본값 설정
        }

        // score가 null이 아닌 경우만 유효성 검사
        if (score < 100 || score > 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "점수는 100에서 200 사이여야 합니다.");
        }

        return score; // 유효한 점수 반환
    }

    private CommentResponseDto mapToDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getWriterNickname(),
                comment.getWriterTier(),
                comment.getCreatedAt(),
                comment.getScore()
        );
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPost_PostId(postId);
        if (comments == null || comments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시물에 댓글이 없습니다.");
        }

        // 로그로 데이터 확인
        System.out.println("댓글 수: " + comments.size());
        return comments.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
