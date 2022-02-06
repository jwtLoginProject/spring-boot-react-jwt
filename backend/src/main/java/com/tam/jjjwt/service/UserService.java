package com.tam.jjjwt.service;


import com.tam.jjjwt.config.JwtTokenUtil;
import com.tam.jjjwt.config.PrincipalDetail;
import com.tam.jjjwt.config.PrincipalDetailService;
import com.tam.jjjwt.dto.BaseResponseDTO;
import com.tam.jjjwt.dto.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tam.jjjwt.mapper.UserMapper;
import com.tam.jjjwt.model.User;
import com.tam.jjjwt.response.exception.ApiException;
import org.springframework.util.StringUtils;

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
@Slf4j
@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PrincipalDetailService principalDetailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    //1. [프론트엔드] ID와 비밀번호를 준다.
    //2. [백엔드] ID와 비밀번호를 검증하고 AccessToken과 RefreshToken, AccessToken의 만료시간을 반환해준다. 이 때 생성한 RefreshToken은 DB에 {ID,RefreshToken}으로 저장한다.
    //3. [프론트엔드] 반환받은 AccessToken을 매 api 호출마다 헤더에 붙여서 전송한다.
    //4. [백엔드] api호출시 헤더의 AccessToken을 확인하고 유효한지, 만료기간이 지났는지를 체크 후 api를 동작시킨다.
    //5. [프론트엔드] AccessToken의 만료 기간이 지나거나, 30초 미만으로 남았다면, 백엔드에 RefreshToken을 붙여 Reissue 요청을 보낸다.
    //6. [백엔드] Reissue요청이 들어올 경우, RefreshToken이 DB에 있는 것인지 확인한 후, 맞다면 AccessToken과 새로운 AccessToken 만료 시간을 반환한다.
    //7. [프론트엔드] Reissue결과 반환된 AccessToken과 만료기간을 저장하여 다음 api호출에 사용한다.

    // 회원가입
    @Transactional
    public BaseResponseDTO signUp(User user) throws ApiException {
        String rawPassword = user.getPassword();

        String encPassword = encoder.encode(rawPassword);
        log.info("encPassword : {}", encPassword);

        user.setPassword(encPassword);

        // todo 이름은 가입시 안 받나요?
        userMapper.join(user);

        final UserDetails userDetails = principalDetailService.loadUserByUsername(user.getUserId());

        UserResponseDto.TokenInfo tokenInfo = jwtTokenUtil.generateToken(userDetails);
        tokenInfo.userId = user.getUserId();

        updateRefreshToken(tokenInfo.getRefreshToken(), user.getUserId());

        return BaseResponseDTO.success(tokenInfo);
    }

    public BaseResponseDTO signIn(User user) throws ApiException {
        log.info("userId: {}, password: {}", user.getUserId(), user.getPassword());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return BaseResponseDTO.fail("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        final UserDetails userDetails = principalDetailService.loadUserByUsername(user.getUserId());

        UserResponseDto.TokenInfo tokenInfo = jwtTokenUtil.generateToken(userDetails);
        tokenInfo.userId = user.getUserId();

        updateRefreshToken(tokenInfo.getRefreshToken(), user.getUserId());

        return BaseResponseDTO.success(tokenInfo);
    }

    public BaseResponseDTO refreshToken(User user) throws ApiException {
        final Authentication authentication = jwtTokenUtil.getAuthentication();
        String currentUserId = authentication.getName();

        log.info("getPrincipal: {}", authentication.getPrincipal());
        log.info("getAuthorities: {}", authentication.getAuthorities());
        log.info("getDetails: {}", authentication.getDetails());
        log.info("getCredentials: {}", authentication.getCredentials());

        if (!StringUtils.hasText(user.getUserId()) || !StringUtils.hasText(user.getRefreshToken())) {
            return BaseResponseDTO.builder()
                    .code("BD001")
                    .messageType(BaseResponseDTO.FAIL)
                    .message("잘못된 요청입니다.")
                    .build();
        }

        // accessToken과 userId가 다른 경우
        if (!currentUserId.equals(user.getUserId())) {
            return BaseResponseDTO.builder()
                    .code("BD002")
                    .messageType(BaseResponseDTO.FAIL)
                    .message("잘못된 요청입니다.")
                    .build();
        }

        if (!jwtTokenUtil.validateToken(user.getRefreshToken())) {
            return BaseResponseDTO.builder()
                    .code("ER002")
                    .messageType(BaseResponseDTO.FAIL)
                    .message("Refresh Token 정보가 일치하지 않습니다.")
                    .build();
        }

        User currentUser = userMapper.findUserByUserId(user.getUserId()).get();
        if (!currentUser.getRefreshToken().equals(user.getRefreshToken())) {
            return BaseResponseDTO.builder()
                    .code("ER003")
                    .messageType(BaseResponseDTO.FAIL)
                    .message("Refresh Token 정보가 일치하지 않습니다.")
                    .build();
        }

        UserDetails userDetails = new PrincipalDetail(currentUser);
        UserResponseDto.TokenInfo tokenInfo = jwtTokenUtil.generateToken(userDetails);
        tokenInfo.userId = user.getUserId();

        updateRefreshToken(tokenInfo.refreshToken, user.getUserId());
        return BaseResponseDTO.success(tokenInfo);
    }


    @Transactional
    public int updateRefreshToken(String refreshToken, String userId) {
        return userMapper.updateRefreshToken(refreshToken, userId);
    }
}
