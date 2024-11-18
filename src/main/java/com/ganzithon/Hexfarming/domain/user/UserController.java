package com.ganzithon.Hexfarming.domain.user;

import com.ganzithon.Hexfarming.domain.user.dto.fromClient.*;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.CheckPasswordServerDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.ResponseTokenServerDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.CheckDuplicateServerDto;
import com.ganzithon.Hexfarming.domain.user.dto.fromServer.UserInformationServerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j // 로그를 자세하게 남겨줘서 적용하면 좋음
@RequestMapping("/user") // <www.hexfarming.com/user> Url 아래로 들어오는 요청을 처리
@RestController // REST API로 사용되는 컨트롤러 (url의 엔드포인트를 매칭해준다)
public class UserController {

    @Autowired
    UserService userService; // UserService에 대한 의존성 주입 -> UserController에서 UserService 사용 가능

    @Tag(name = "유저")
    @Operation(summary = "회원가입", description = "회원가입한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTokenServerDto.class))),
            @ApiResponse(responseCode = "400", description = "중복된 아이디 혹은 닉네임, 또는 비밀번호가 일치하지 않은 경우", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST) // 해당 Endpoint로 들어오는 Post 요청은 CORS를 모두 허용
    @PostMapping("/signup") // /user/signup에 대한 Post 요청을 받겠다
    public ResponseTokenServerDto signUp(@RequestBody SignUpClientDto dto) { // SignUpClientDto를 요청받아서 SignUpServerDto를 반환함
        return userService.signUp(dto);
    }

    @Tag(name = "유저")
    @Operation(summary = "로그인", description = "로그인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTokenServerDto.class))),
            @ApiResponse(responseCode = "400", description = "아이디 혹은 비밀번호가 일치하지 않은 경우", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @PostMapping("/login")
    public ResponseTokenServerDto logIn(@RequestBody LoginClientDto dto) {
        return userService.logIn(dto);
    }

    @Tag(name = "유저")
    @Operation(summary = "중복 이메일 검사", description = "중복된 이메일인지 검사한다.\n\n(중복이면 true, 중복이 아니면 false 반환)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검사 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CheckDuplicateServerDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @PostMapping("/checkDuplicateEmail")
    public CheckDuplicateServerDto checkDuplicateEmail(@RequestBody CheckDuplicateEmailClientDto dto) {
        return userService.checkDuplicateEmail(dto);
    }

    @Tag(name = "유저")
    @Operation(summary = "중복 이름 검사", description = "중복된 이름인지 검사한다.\n\n(중복이면 true, 중복이 아니면 false 반환)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검사 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CheckDuplicateServerDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @PostMapping("/checkDuplicateNickname")
    public CheckDuplicateServerDto checkDuplicateNickname(@RequestBody CheckDuplicateNicknameClientDto dto) {
        return userService.checkDuplicateNickname(dto);
    }

    @Tag(name = "유저")
    @Operation(summary = "회원가입 시 패스워드 검사", description = "입력된 두 패스워드(패스워드, 패스워드 확인)가 일치하는지 검사한다.\n\n(일치하면 true, 일치하지 않으면 false 반환)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검사 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CheckPasswordServerDto.class)))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @PostMapping("/checkRePassword")
    public CheckPasswordServerDto checkRePassword(@RequestBody CheckRePasswordClientDto dto) {
        return userService.checkRePassword(dto);
    }

    @Tag(name = "유저")
    @Operation(summary = "자신의 정보 조회", description = "현재 요청한 유저의 정보를 조회한다.\n\n(이메일과 이름, 닉네임만 불러온다)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInformationServerDto.class))),
            @ApiResponse(responseCode = "401", description = "잘못된 유저가 요청할 경우", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @GetMapping("/myInformation")
    public UserInformationServerDto myInformation() {
        return userService.myInformation();
    }

    @Tag(name = "유저")
    @Operation(summary = "특정 유저의 정보 조회", description = "특정 유저의 유저의 정보를 조회한다.\n\n(이메일과 이름, 닉네임만 불러온다)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserInformationServerDto.class))),
            @ApiResponse(responseCode = "401", description = "잘못된 유저가 요청할 경우", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @GetMapping("/userInformation")
    public UserInformationServerDto userInformation(@RequestBody UserInformationClientDto dto) {
        return userService.userInformation(dto.userId());
    }

    @Tag(name = "유저")
    @Operation(summary = "회원 정보 수정 전 패스워드 확인", description = "입력된 패스워드가 일치하는지 검사한다.\n\n(일치하면 true, 일치하지 않으면 false 반환)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검사 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CheckPasswordServerDto.class))),
            @ApiResponse(responseCode = "401", description = "잘못된 유저가 요청할 경우", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @PostMapping("/checkPassword")
    public CheckPasswordServerDto checkPassword(@RequestBody CheckPasswordClientDto dto) {
        return userService.checkPassword(dto);
    }

    @Tag(name = "유저")
    @Operation(summary = "패스워드 변경", description = "현재 요청을 보낸 유저의 패스워드를 변경한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "패스워드 변경 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "두 패스워드가 일치하지 않는 경우", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "잘못된 유저가 요청할 경우", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.PATCH)
    @PatchMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordClientDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }

    @Tag(name = "유저")
    @Operation(summary = "닉네임 변경", description = "현재 요청을 보낸 유저의 닉네임을 변경한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "닉네임 변경 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "잘못된 유저가 요청할 경우", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.PATCH)
    @PatchMapping("/changeNickname")
    public ResponseEntity<Void> changeNickname(@RequestBody ChangeNicknameClientDto dto) {
        userService.changeNickname(dto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }

    @Tag(name = "유저")
    @Operation(summary = "회원 탈퇴", description = "현재 요청을 보낸 유저를 DB에서 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "탈퇴 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "잘못된 유저가 요청할 경우", content = @Content(mediaType = "application/json"))
    })
    @CrossOrigin(origins = "*", methods = RequestMethod.DELETE)
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw() {
        userService.withdraw();
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }
}
