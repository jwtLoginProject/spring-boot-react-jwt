package com.tam.jjjwt.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tam.jjjwt.config.JwtTokenUtil;
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
 */
@Controller
public class UserController {
	
	@Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    
    
    @PostMapping("/auth/signInProc")
    public String signIn(@RequestBody User user , HttpServletResponse response) throws Exception {

        System.out.println(user);
        System.out.println(user.getUserId());
        System.out.println(user.getPassword());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        String accessToken = "";
        String refreshToken = "";

        accessToken = jwtTokenUtil.generateToken(user.getUserId() , 1);
        refreshToken = jwtTokenUtil.generateToken(user.getUserId() , 3);
        Cookie accessCookie = new Cookie("accessCookie" , accessToken);
        Cookie refreshCookie = new Cookie("refreshToken" , refreshToken);
        
        accessCookie.setMaxAge(1 * 60);
        response.addCookie(accessCookie);
        
        refreshCookie.setMaxAge(3 * 60);
        response.addCookie(refreshCookie);
        userService.updateRefreshToken(refreshToken, user.getUserId());

        // TODO 리턴값 변경
        return "success";
    }

    @PostMapping("/auth/refreshToken")
    public String refreshToken(@RequestBody User user , HttpServletRequest request) throws Exception{

        String accessToken = "";
        String refreshToken = "";

        Cookie [] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0 ) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    if(jwtTokenUtil.checkClaim(refreshToken)) {
                        accessToken = jwtTokenUtil.generateToken(user.getUserId() , 1);
                    }else {
                        throw new InvalidRefreshTokenException();
                    }
                }
            }
        }

        if(refreshToken == null || "".equals(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }


        return accessToken;
    }
}
