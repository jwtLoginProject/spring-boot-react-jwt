package com.tam.jjjwt.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tam.jjjwt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.tam.jjjwt.config.JwtTokenUtil;
import com.tam.jjjwt.config.PrincipalDetail;
import com.tam.jjjwt.config.PrincipalDetailService;
import com.tam.jjjwt.response.exception.InvalidRefreshTokenException;
import com.tam.jjjwt.service.UserService;


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
@Controller
public class UserController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private PrincipalDetailService principalDetailService;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;


    // 회원 가입 요청
    @ResponseBody
    @PostMapping("/auth/signUpProc")
    public String signUp(@RequestBody User user) {
        userService.join(user);

        return "Success";
    }


    // 로그인 화면 조회
    @GetMapping("/auth/signInForm")
    public String signInForm() {
        return "user/singInForm";

    }
    
    @ResponseBody
    @PostMapping("/auth/signInProc")
    public Map<String, String> signIn(@RequestBody User user , HttpServletResponse response) throws Exception {

        System.out.println(user);
        System.out.println(user.getUserId());
        System.out.println(user.getPassword());

        
        try {
        	System.out.println("BEFORE authentication");
        	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());
        	System.out.println(token);
        	authenticationManager.authenticate(token);
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword()));
            System.out.println("authentication OK");
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = principalDetailService.loadUserByUsername(user.getUserId());
        System.out.println(userDetails);
        String accessToken = "";
        String refreshToken = "";

        Map<String, String> resultMap = new HashMap<>();

        accessToken = jwtTokenUtil.generateToken(userDetails, 40); // 테스트 40초 , 유효 기간 : 1시간
        System.out.println(accessToken);
        refreshToken = jwtTokenUtil.generateToken(userDetails, 80); // 테스트 80초, 유효 기간 : 7일
        System.out.println(refreshToken);

        resultMap.put("AccessToken",accessToken);
        resultMap.put("RefreshToken",refreshToken);

//        Cookie accessCookie = new Cookie("accessCookie", accessToken);
//        accessCookie.setMaxAge(60 * 60);
//        response.addCookie(accessCookie);
//
//
//        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
//        refreshCookie.setMaxAge(60 * 60 * 24 * 7);
//        response.addCookie(refreshCookie);
        userService.updateRefreshToken(refreshToken, user.getUserId());


        //        UserResponseDto.TokenInfo tokenInfo = jwtTokenUtil.generateToken(userDetails);
//        tokenInfo.userId = user.getUserId();

        // TODO ResponseDto 적용
        return resultMap;
    }
    
    @ResponseBody
    @PostMapping("/auth/refreshToken")
    public String refreshToken(@RequestBody User user , HttpServletRequest request, HttpServletResponse response) throws Exception{

        System.out.println("refreshToken 요청 받음");

    	final UserDetails userDetails = principalDetailService.loadUserByUsername(user.getUserId());
    	
        String accessToken = "";
        String refreshToken = "";

        // TODO refreshToken DB와 비교 로직 추가
        
        Cookie [] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0 ) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    if(jwtTokenUtil.checkClaim(refreshToken)) {
                        accessToken = jwtTokenUtil.generateToken(userDetails, 40);
                    }else {
                        throw new InvalidRefreshTokenException();
                    }
                }
            }
        }

        if(refreshToken == null || "".equals(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        // TODO refreshToken 재발급 로직
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(jwtTokenUtil.getExpirationDateFromToken(refreshToken));
        calendar.add(Calendar.DATE, +1);
        
        // refreshToken 만료 하루 전 재발급 후 cookie에 담아 응답
        if(calendar.getTime().compareTo(new Date()) == 0) { // refreshToken의 유효 날짜+1 == 현재 시간
        	refreshToken = jwtTokenUtil.generateToken(userDetails, 80); //테스트 1분// 유효 기간 : 7일
        	Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            
        	refreshCookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(refreshCookie);
            userService.updateRefreshToken(refreshToken, user.getUserId());
        }

        Cookie accessCookie = new Cookie("accessCookie", accessToken);
        
        accessCookie.setMaxAge(60 * 60);
        response.addCookie(accessCookie);
        
        return "success";
    }
    
    
    
//    TODO 기존 accesstoken 재발급 로직 주석처리
//    @PostMapping("/auth/refreshToken")
//    public String refreshToken(@RequestBody User user , HttpServletRequest request, HttpServletResponse response) throws Exception{
//
//        String accessToken = "";
//        String refreshToken = "";
//
//        Cookie [] cookies = request.getCookies();
//        if(cookies != null && cookies.length > 0 ) {
//            for(Cookie cookie : cookies) {
//                if(cookie.getName().equals("refreshToken")) {
//                    refreshToken = cookie.getValue();
//                    if(jwtTokenUtil.checkClaim(refreshToken)) {
//                        accessToken = jwtTokenUtil.generateToken(user.getUserId(), 60);
//                    }else {
//                        throw new InvalidRefreshTokenException();
//                    }
//                }
//            }
//        }
//
//        if(refreshToken == null || "".equals(refreshToken)) {
//            throw new InvalidRefreshTokenException();
//        }
//
//        // TODO refreshToken 재발급 로직
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(jwtTokenUtil.getExpirationDateFromToken(refreshToken));
//        calendar.add(Calendar.DATE, +1);
//        
//        // refreshToken 만료 하루 전 재발급 후 cookie에 담아 응답
//        if(calendar.getTime().compareTo(new Date()) == 0) { // refreshToken의 유효 날짜+1 == 현재 시간
//        	refreshToken = jwtTokenUtil.generateToken(user.getUserId(), 60 * 24 * 7); // 유효 기간 : 7일
//        	Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
//            
//        	refreshCookie.setMaxAge(60 * 60 * 24 * 7);
//            response.addCookie(refreshCookie);
//            userService.updateRefreshToken(refreshToken, user.getUserId());
//        }
//
//        Cookie accessCookie = new Cookie("accessCookie", accessToken);
//        
//        accessCookie.setMaxAge(60 * 60);
//        response.addCookie(accessCookie);
//        
//        return "success";
//    }
}
