package com.ganzithon.Hexfarming.post;

import com.ganzithon.Hexfarming.dto.fromClient.PostRequestDto;
import com.ganzithon.Hexfarming.dto.fromClient.PostUpdateRequestDto;
import com.ganzithon.Hexfarming.dto.fromServer.PostResponseDto;
import com.ganzithon.Hexfarming.domain.user.User;
import com.ganzithon.Hexfarming.domain.user.UserRepository;
import com.ganzithon.Hexfarming.utility.JwtManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.ganzithon.Hexfarming.comment.CommentRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtManager jwtManager; // JWT 토큰 처리 클래스

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, JwtManager jwtManager, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.jwtManager = jwtManager;
        this.commentRepository = commentRepository;
    }


    // 게시물 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        // 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자가 인증되지 않았습니다.");
        }

        String username = authentication.getName();
        System.out.println("인증된 사용자: " + username);

        User writer = userRepository.findByUsername(username);
        if (writer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "작성자를 찾을 수 없습니다.");
        }
        System.out.println("작성자 정보: " + writer);

        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setCategory(postRequestDto.getCategory());
        post.setWriter(writer);
        post.setWriterNickname(writer.getNickname());
        post.setCreatedAt(LocalDateTime.now());
        post.setTimer(LocalDateTime.now().plusHours(24));
        post.setTier(writer.getTier());
        post.setView(0);

        postRepository.save(post);

        return PostResponseDto.fromEntity(post);
    }


    // 게시글 목록 조회
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostResponseDto::fromEntity)
                .toList();
    }


    // 게시물 상세 조회(조회수 증가 로직 포함되어있음)
    @Transactional
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

        // 조회수 증가
        post.setView(post.getView() + 1);
        postRepository.save(post);

        return PostResponseDto.fromEntity(post);
    }


    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto updateRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자가 인증되지 않았습니다.");
        }

        String username = authentication.getName();
        if (!post.getWriter().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 게시물을 수정할 권한이 없습니다.");
        }

        // 수정 내용 적용
        if (updateRequest.getTitle() != null) {
            post.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getContent() != null) {
            post.setContent(updateRequest.getContent());
        }
        if (updateRequest.getCategory() != null) {
            post.setCategory(Category.valueOf(updateRequest.getCategory().toUpperCase())); // Enum 변환
        }

        // 저장
        Post updatedPost = postRepository.save(post);
        return PostResponseDto.fromEntity(updatedPost);
    }


    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자가 인증되지 않았습니다.");
        }

        String username = authentication.getName();
        if (!post.getWriter().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 게시물을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    // 평균 점수 구하기
    @Transactional(readOnly = true)
    public int getAverageScoreByPostId(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."));

        // 댓글 평균 점수 계산 (남아있는 시간 기준)
        Double averageScore = commentRepository.getAverageScoreByPostIdWithRemainingTime(postId);

        // 평균 점수가 null이면 댓글이 없거나 남은 시간이 없으므로 기본값 100 반환
        return (averageScore != null) ? (int) Math.round(averageScore) : 100;
    }

}