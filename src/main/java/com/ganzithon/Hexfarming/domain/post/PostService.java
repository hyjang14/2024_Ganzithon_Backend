package com.ganzithon.Hexfarming.domain.post;

import com.ganzithon.Hexfarming.domain.comment.Comment;
import com.ganzithon.Hexfarming.domain.experience.ExperienceService;
import com.ganzithon.Hexfarming.domain.notification.NotificationService;
import com.ganzithon.Hexfarming.domain.post.dto.fromClient.PostRequestDto;
import com.ganzithon.Hexfarming.domain.post.dto.fromClient.PostUpdateRequestDto;
import com.ganzithon.Hexfarming.domain.post.dto.fromServer.PostResponseDto;
import com.ganzithon.Hexfarming.domain.user.User;
import com.ganzithon.Hexfarming.domain.user.UserRepository;
import com.ganzithon.Hexfarming.global.enumeration.Ability;
import com.ganzithon.Hexfarming.global.utility.JwtManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.ganzithon.Hexfarming.domain.comment.CommentRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final ExperienceService experienceService;
    private final NotificationService notificationService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtManager jwtManager; // JWT 토큰 처리 클래스

    @Autowired
    public PostService(ExperienceService experienceService, NotificationService notificationService, PostRepository postRepository,
                       UserRepository userRepository, JwtManager jwtManager, CommentRepository commentRepository) {
        this.experienceService = experienceService;
        this.notificationService = notificationService;
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

        User writer = userRepository.findByEmail(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "작성자를 찾을 수 없습니다."));
        System.out.println("작성자 정보: " + writer);

        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setAbility(postRequestDto.getAbility());
        post.setWriter(writer);
        post.setWriterNickname(writer.getNickname());
        post.setCreatedAt(LocalDateTime.now());
        post.setTimer(LocalDateTime.now().plusHours(24));
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
        if (!post.getWriter().getEmail().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 게시물을 수정할 권한이 없습니다.");
        }

        // 수정 내용 적용
        if (updateRequest.getTitle() != null) {
            post.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getContent() != null) {
            post.setContent(updateRequest.getContent());
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
        if (!post.getWriter().getEmail().equals(username)) {
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

    @Scheduled(cron = "0 * * * * *")
    public void checkIfTimerOver() {
        LocalDateTime now = LocalDateTime.now();
        Optional<List<Post>> postsOptional = postRepository.findByTimerBeforeAndIsTimerOverFalse(now);
        if (postsOptional.isPresent()) {
            postsOptional.get().stream()
                    .forEach(post -> {
                        int writerId = post.getWriter().getId();
                        experienceService.inceaseExperience(writerId, post.getAbility(), getAverageScoreByPostId(post.getPostId()));
                        post.setTimerOver(true);
                        notificationService.saveCheckPoints(post);
                    });
        }
    }

    @Transactional
    public void processPostAfterTimerEnds(Post post) {
        try {
            List<Comment> comments = commentRepository.findByPost_PostId(post.getPostId());
            if (!comments.isEmpty()) {
                // 댓글 랜덤 선택 (혹은 다른 기준으로 선택 가능)
                Comment selectedComment = comments.get(0); // 첫 번째 댓글로 채택 (임시 로직)
                selectedComment.setSelected(true);
                commentRepository.save(selectedComment);

                // 채택된 댓글 작성자에게 경험치 지급
                experienceService.inceaseExperience(
                        selectedComment.getWriter().getId(),
                        post.getAbility(),
                        post.getScoreSum() // 총점 혹은 평균 점수
                );
            }

            // 게시글 타이머 종료 상태 업데이트
            post.setTimerOver(true);
            postRepository.save(post);

        } catch (Exception e) {
            // 오류 발생 시 처리
            System.err.println("Timer processing failed for post ID: " + post.getPostId());
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> searchPost(String titleContains, Ability ability) {
        Optional<List<Post>> postsOptional = postRepository.findByTitleContaining(titleContains);
        if (ability != null) {
            postsOptional = postRepository.findByTitleContainingAndAbility(titleContains, ability);
        }

        if (postsOptional.isEmpty() || postsOptional.get().isEmpty()) {
            return Collections.emptyList();
        }

        return postsOptional.get().stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 전체 카테고리: 조회수 기준 상위 2개 게시물 반환
    @Transactional(readOnly = true)
    public List<PostResponseDto> getTop2PostsByView() {
        Pageable pageable = PageRequest.of(0, 2); // 0번째 페이지에서 2개만 가져오기
        List<Post> topPosts = postRepository.findTopByOrderByViewDesc(pageable);
        return topPosts.stream()
                .map(PostResponseDto::fromEntity)
                .toList();
    }

    // 카테고리별: 조회수 기준 상위 2개 게시물 반환
    @Transactional(readOnly = true)
    public List<PostResponseDto> getTop2PostsByCategory(Ability ability) {
        Pageable pageable = PageRequest.of(0, 2); // 0번째 페이지에서 2개만 가져오기
        List<Post> posts = postRepository.findTopByCategoryOrderByViewDesc(ability, pageable);
        return posts.stream()
                .map(PostResponseDto::fromEntity)
                .toList();
    }

    // 전체 카테고리: 마감 임박 게시글 상위 2개 반환
    @Transactional(readOnly = true)
    public List<PostResponseDto> getTop2ExpiringPosts() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Post> posts = postRepository.findTopByOrderByTimerAsc(pageable);
        return posts.stream()
                .map(PostResponseDto::fromEntity)
                .toList();
    }

    // 카테고리별: 마감 임박 게시글 상위 2개 반환
    @Transactional(readOnly = true)
    public List<PostResponseDto> getTop2ExpiringPostsByCategory(Ability ability) {
        Pageable pageable = PageRequest.of(0, 2); // 0번째 페이지에서 2개만 가져오기
        List<Post> posts = postRepository.findTopByCategoryAndOrderByTimerAsc(ability, pageable);
        return posts.stream()
                .map(PostResponseDto::fromEntity)
                .toList();
    }

}