package com.ganzithon.Hexfarming.comment;

import com.ganzithon.Hexfarming.dto.fromClient.CommentRequestDto;
import com.ganzithon.Hexfarming.dto.fromServer.CommentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId, // URL에서 게시글 ID 받기
            @RequestBody CommentRequestDto commentRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        CommentResponseDto response = commentService.createComment(postId, commentRequestDto, username);
        return ResponseEntity.ok(response);
    }

    // 댓글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/{postId}/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto commentRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        CommentResponseDto response = commentService.updateComment(postId, commentId, commentRequestDto, username);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        commentService.deleteComment(postId, commentId, username);
        return ResponseEntity.noContent().build();
    }
}
