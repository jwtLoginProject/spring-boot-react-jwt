package com.tam.jjjwt.controller;

import com.tam.jjjwt.dto.BaseResponseDTO;
import com.tam.jjjwt.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.tam.jjjwt.service.UserService;
import org.springframework.web.bind.annotation.*;


/**
 * @author 전예지
 * @version 1.0
 * @Description
 * @Modification Information
 * Created 2022/02/03
 * @
 * @ 수정일         수정자                   수정내용
 * @ ———    ————    —————————————
 * @ 2022/02/03		전예지			최초 작성
 * @ 2022/02/04		전예지			refresh token 재발급, 토큰 생성 파라미터 변경
 */
@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // todo
    // 1. db 설계 오류
    //    user 테이블의 user_id가 유니크하지 않음 -> 반복해서 동일한 아이디로 회원가입이 가능한 오류 발생
    // 2. 비즈니스 로직은 서비스에서 처리하도록 변경

    @PostMapping("/auth/signUpProc")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        return ResponseEntity.ok(userService.signUp(user));
    }

    @PostMapping("/auth/signInProc")
    public ResponseEntity<?> signIn(@RequestBody User user) {
        return ResponseEntity.ok(userService.signIn(user));
    }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody User user) {
        BaseResponseDTO responseDTO = userService.refreshToken(user);
        if (responseDTO.getCode().equals("BD001") || responseDTO.getCode().equals("BD002")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        if (responseDTO.getCode().startsWith("ER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
        }
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/auth/memberInfo")
    public ResponseEntity<?> memberInfo(@RequestParam String userId) {
        // todo
        return ResponseEntity.ok(userId);
    }
}
