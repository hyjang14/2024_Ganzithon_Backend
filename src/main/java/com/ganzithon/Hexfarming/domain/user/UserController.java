package com.ganzithon.Hexfarming.domain.user;

import com.ganzithon.Hexfarming.dto.fromClient.LoginClientDto;
import com.ganzithon.Hexfarming.dto.fromClient.SignUpClientDto;
import com.ganzithon.Hexfarming.dto.fromServer.ResponseTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j // 로그를 자세하게 남겨줘서 적용하면 좋음
@RequestMapping("/user") // <www.hexfarming.com/user> Url 아래로 들어오는 요청을 처리
@RestController // REST API로 사용되는 컨트롤러 (url의 엔드포인트를 매칭해준다)
public class UserController {

    @Autowired
    UserService userService; // UserService에 대한 의존성 주입 -> UserController에서 UserService 사용 가능

    @CrossOrigin(origins = "*", methods = RequestMethod.POST) // 해당 Endpoint로 들어오는 Post 요청은 CORS를 모두 허용
    @PostMapping("/signup") // /user/signup에 대한 Post 요청을 받겠다
    public ResponseTokenDto signUp(@RequestBody SignUpClientDto dto) { // SignUpClientDto를 요청받아서 SignUpServerDto를 반환함
        return userService.signUp(dto);
    }

    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @PostMapping("/login")
    public ResponseTokenDto logIn(@RequestBody LoginClientDto dto) {
        return userService.logIn(dto);
    }
}
