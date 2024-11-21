package com.ganzithon.Hexfarming.domain.comment;

import com.ganzithon.Hexfarming.domain.comment.dto.fromClient.CommentRequestDto;
import com.ganzithon.Hexfarming.domain.comment.dto.fromClient.SelectCommentClientDto;
import com.ganzithon.Hexfarming.domain.comment.dto.fromServer.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Tag(name = "댓글")
    @Operation(summary = "댓글 생성", description = "댓글을 생성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
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
    @Tag(name = "댓글")
    @Operation(summary = "댓글 조회", description = "댓글을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @Tag(name = "댓글")
    @Operation(summary = "댓글 수정", description = "댓글을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.PUT)
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
    @Tag(name = "댓글")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.DELETE)
    @DeleteMapping("/{postId}/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        commentService.deleteComment(postId, commentId, username);
        return ResponseEntity.noContent().build();
    }

    // 댓글 채택 API
    @Tag(name = "댓글")
    @Operation(summary = "댓글 채택", description = "댓글을 채택한다.")
    @PostMapping("/{postId}/{commentId}/accept")
    public ResponseEntity<CommentResponseDto> selectComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody SelectCommentClientDto dto
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(null);
        }

        String username = authentication.getName();

        CommentResponseDto selectedComment = commentService.selectComment(dto, postId, commentId, username);

        return ResponseEntity.ok(selectedComment);
    }

    // 채택된 댓글 조회 API
    @Tag(name = "댓글")
    @Operation(summary = "채택된 댓글 조회", description = "채택된 댓글을 조회한다.")
    @GetMapping("/{postId}/{commentId}/accepted")
    public ResponseEntity<CommentResponseDto> getSelectedComment(@PathVariable Long postId) {
        CommentResponseDto selectedComment = commentService.getSelectedComment(postId);
        return ResponseEntity.ok(selectedComment);
    }
}
