package com.ganzithon.Hexfarming.domain.post;

import com.ganzithon.Hexfarming.domain.post.dto.fromClient.DeletePictureClientDto;
import com.ganzithon.Hexfarming.domain.post.dto.fromClient.PostRequestDto;
import com.ganzithon.Hexfarming.domain.post.dto.fromClient.PostUpdateRequestDto;
import com.ganzithon.Hexfarming.domain.post.dto.fromServer.*;
import com.ganzithon.Hexfarming.global.enumeration.Ability;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
    @Tag(name = "게시글")
    @Operation(summary = "게시글 생성", description = "게시글을 작성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {
        PostResponseDto postResponseDto = postService.createPost(postRequestDto);
        return ResponseEntity.ok(postResponseDto);
    }

    // 게시물 상세 조회
    @Tag(name = "게시글")
    @Operation(summary = "게시글 상세 조회", description = "게시글을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.getPost(postId);
        return ResponseEntity.ok(postResponseDto);
    }

    // 게시글 목록 조회
    @Tag(name = "게시글")
    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 게시글 수정
    @Tag(name = "게시글")
    @Operation(summary = "게시글 수정", description = "게시글을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.PUT)
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequestDto updateRequest) {
        PostResponseDto updatedPost = postService.updatePost(postId, updateRequest);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @Tag(name = "게시글")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.DELETE)
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // 평균 점수 확인
    @Tag(name = "게시글")
    @Operation(summary = "평균 점수 확인", description = "게시글의 평균 점수를 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AverageScoreResponseDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/{postId}/average-score")
    public ResponseEntity<AverageScoreResponseDto> getAverageScore(@PathVariable Long postId) {
        // 평균 점수 계산 (남아있는 시간 기준으로 계산)
        int averageScore = postService.getAverageScoreByPostId(postId);

        AverageScoreResponseDto responseDto = new AverageScoreResponseDto(postId, averageScore);
        return ResponseEntity.ok(responseDto);
    }

    // 전체 카테고리: 조회수 기준 상위 2개 게시물 반환
    @Tag(name = "게시글")
    @Operation(summary = "전체 카테고리에서 조회수 높은 게시글 상위 2개 조회", description = "전체 카테고리에서 조회수 높은 게시글 상위 2개를 조회한다.")
    @GetMapping("/top")
    public List<PostResponseDto> getTop2PostsByView() {
        return postService.getTop2PostsByView();
    }

    // 카테고리별: 조회수 기준 상위 2개 게시물 반환
    @Tag(name = "게시글")
    @Operation(summary = "카테고리별 조회수 높은 게시글 상위 2개 조회", description = "특정 카테고리에서 조회수 높은 게시글 상위 2개를 조회한다.")
    @GetMapping("/top-by-ability/{ability}")
    public List<PostResponseDto> getTop2PostsByCategory(@PathVariable("ability") String ability) {
        return postService.getTop2PostsByCategory(Ability.valueOf(ability.toUpperCase()));
    }

    // 전체 카테고리: 마감 임박 게시글 상위 2개 반환
    @Tag(name = "게시글")
    @Operation(summary = "전체 카테고리에서 마감시간 임박한 게시글 2개 조회", description = "전체 카테고리에서 마감시간 임박한 게시글 2개를 조회한다.")
    @GetMapping("/expiring")
    public List<PostResponseDto> getTop2ExpiringPosts() {
        return postService.getTop2ExpiringPosts();
    }

    // 카테고리별: 마감 임박 게시글 상위 2개 반환
    @Tag(name = "게시글")
    @Operation(summary = "카테고리별 마감시간 임박한 게시글 2개 조회", description = "특정 카테고리에서 마감시간 임박한 게시글 2개를 조회한다.")
    @GetMapping("/expiring/{ability}")
    public List<PostResponseDto> getTop2ExpiringPostsByCategory(@PathVariable("ability") String ability) {
        Ability parsedAbility;
        try {
            parsedAbility = Ability.valueOf(ability.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ability: " + ability);
        }
        return postService.getTop2ExpiringPostsByCategory(parsedAbility);
    }

    // 전체 카테고리 검색
    @Tag(name = "게시글")
    @Operation(summary = "전체 카테고리 검색", description = "입력어가 포함된 제목을 가진 게시글을 전체 카테고리에서 찾는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostResponseDto.class))))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/searchAllPost/{searchTitleText}")
    public List<PostResponseDto> searchAllPost(@PathVariable String searchTitleText) {
        return postService.searchPost(searchTitleText, null);
    }

    // 특정 카테고리 검색
    @Tag(name = "게시글")
    @Operation(summary = "특정 카테고리 검색", description = "입력어가 포함된 제목을 가진 게시글을 특정 카테고리에서 찾는다.\n\nability 종류: LEADERSHIP(리더십), CREATIVITY(창의력), COMMUNICATION_SKILL(소통 역량), DILIGENCE(성실성), RESILIENCE(회복 탄력성), TENACITY(인성)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostResponseDto.class))))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/searchPost/{ability}/{searchTitleText}")
    public List<PostResponseDto> searchPost(@PathVariable Ability ability, @PathVariable String searchTitleText) {
        return postService.searchPost(searchTitleText, ability);
    }

    // 특정 카테고리 검색
    @Tag(name = "게시글")
    @Operation(summary = "가장 최신 글 가져오기", description = "각 카테고리 별 가장 최근에 올라온 게시글 하나의 정보를 반환한다. 각 역량 별 하나씩 총 6개를 반환한다.\n\n해당 카테고리에 작성된 글이 없을 시, postId를 -1으로 반환한다.\n\nability 종류: LEADERSHIP(리더십), CREATIVITY(창의력), COMMUNICATION_SKILL(소통 역량), DILIGENCE(성실성), RESILIENCE(회복 탄력성), TENACITY(인성)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostTitleServerDto.class))))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/getLatestPosts")
    public List<PostTitleServerDto> getLatestPosts() {
        return postService.getLatestPosts();
    }

    // 내가 쓴 게시글 수 조회
    @Tag(name = "게시글")
    @Operation(summary = "내가 쓴 게시글 수 조회", description = "각 카테고리 별로 내가 작성한 게시글의 수를 반환한다.\n\nability 종류: LEADERSHIP(리더십), CREATIVITY(창의력), COMMUNICATION_SKILL(소통 역량), DILIGENCE(성실성), RESILIENCE(회복 탄력성), TENACITY(인성)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyPostCountServerDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/getMyPostsCount")
    public MyPostCountServerDto getMyPostsCount() {
        return postService.getMyPostsCount();
    }

    // 내가 쓴 게시글 카테고리 별로 조회
    @Tag(name = "게시글")
    @Operation(summary = "내가 쓴 게시글 카테고리 별 조회", description = "특정 카테고리에 내가 작성한 게시글 목록을 반환한다.\n\nability 종류: LEADERSHIP(리더십), CREATIVITY(창의력), COMMUNICATION_SKILL(소통 역량), DILIGENCE(성실성), RESILIENCE(회복 탄력성), TENACITY(인성)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostResponseDto.class))))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    @GetMapping("/getMyPostsByAbility/{ability}")
    public List<PostResponseDto> getMyPostsByAbility(@PathVariable Ability ability) {
        return postService.getMyPostsByAbility(ability);
    }

    // S3에 사진 올리기
    @Tag(name = "게시글")
    @Operation(summary = "S3에 사진 올리기", description = "S3 서버에 사진을 업로드한 후 Url을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @PostMapping(value = "/uploadPicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PictureUrlServerDto uploadPicture(
            @RequestParam()
            MultipartFile multipartFile) {
        return postService.uploadPicture(multipartFile);
    }

    // S3에 올라간 사진 삭제
    @Tag(name = "게시글")
    @Operation(summary = "S3의 사진 삭제", description = "S3 서버에 업로드된 사진을 삭제한다.\n\nUrl은 파일의 Url 전체를 입력해 주세요 (ex. https://hexfarming.s3.ap-northeast-2.amazonaws.com/images/340bef22c86b48088a76e0a117b02e39.jpg)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.DELETE)
    @DeleteMapping(value = "/deletePicture")
    public ResponseEntity<Void> deletePicture(@RequestBody DeletePictureClientDto dto) {
        postService.deletePicture(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }
}

