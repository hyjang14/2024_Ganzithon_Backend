package com.ganzithon.Hexfarming.post;

import com.ganzithon.Hexfarming.dto.fromClient.PostRequestDto;
import com.ganzithon.Hexfarming.dto.fromClient.PostUpdateRequestDto;
import com.ganzithon.Hexfarming.dto.fromServer.AverageScoreResponseDto;
import com.ganzithon.Hexfarming.dto.fromServer.PostResponseDto;
import com.ganzithon.Hexfarming.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    // service 호출
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시물 생성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {
        PostResponseDto postResponseDto = postService.createPost(postRequestDto);
        return ResponseEntity.ok(postResponseDto);
    }

    // 게시물 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.getPost(postId);
        return ResponseEntity.ok(postResponseDto);
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequestDto updateRequest) {
        PostResponseDto updatedPost = postService.updatePost(postId, updateRequest);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // 평균 점수 확인
    @GetMapping("/{postId}/average-score")
    public ResponseEntity<AverageScoreResponseDto> getAverageScore(@PathVariable Long postId) {
        // 평균 점수 계산 (남아있는 시간 기준으로 계산)
        int averageScore = postService.getAverageScoreByPostId(postId);

        AverageScoreResponseDto responseDto = new AverageScoreResponseDto(postId, averageScore);
        return ResponseEntity.ok(responseDto);
    }


}

