package com.tam.jjjwt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tam.jjjwt.mapper.UserMapper;
import com.tam.jjjwt.model.User;
import com.tam.jjjwt.response.ExceptionEnum;
import com.tam.jjjwt.response.exception.ApiException;
import com.tam.jjjwt.util.CommonUtils;


@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
    private BCryptPasswordEncoder encoder;
	
	// 회원가입
    @Transactional
    public Map<String, String> join(User requestUser) throws ApiException {

        if(CommonUtils.isNotEmpty(requestUser.getUserId()) == false) {
            throw new ApiException(ExceptionEnum.INVALID_SIGNUP_INPUT_06);
        }
        if (userMapper.checkUserId(requestUser.getUserId()) != 0) {
            throw new ApiException(ExceptionEnum.INVALID_SIGNUP_INPUT_01);
        }
        if(CommonUtils.isUserId(requestUser.getUserId()) == false) {
            throw new ApiException(ExceptionEnum.INVALID_SIGNUP_INPUT_02);
        }

        String rawPassword = requestUser.getPassword();
        if(CommonUtils.isNotEmpty(rawPassword) == false) {
            throw new ApiException(ExceptionEnum.INVALID_SIGNUP_INPUT_06);
        }
        if(CommonUtils.isPassword(rawPassword) == false) {
            throw new ApiException(ExceptionEnum.INVALID_SIGNUP_INPUT_03);
        }
        String encPassword = encoder.encode(rawPassword);
        requestUser.setPassword(encPassword);


        userMapper.join(requestUser);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("messageType", "Success");
        resultMap.put("message", "회원가입 완료");


        return resultMap;
    };

    
//    // TODO refresh token 저장
//    @Transactional
//    public int updateRefreshToken() {
//    	return userMapper.updateRefreshToken();
//    }
    
    


    // userId로 유저 고유값 찾기
    @Transactional
    public int findUserPk(String userId) {
    	return userMapper.findPkByUserId(userId);
    }
    
    
    
    // user sequence로 주문자(유저) 정보 찾기
    @Transactional
    public User findUser(int userSeq) {

        return userMapper.findUserById(userSeq).orElseGet(() -> {
            return new User();
        });
    };


    // 유저 아이디 중복 체크 (blur() 처리용)
    //TODO return값 통일해야할지 프론트와 상의
    @Transactional
    public Map<String, String> checkUserId(String userId) {
        int count = userMapper.checkUserId(userId);

        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("messageType", count == 0 ? "Success" : "Failure");
        resultMap.put("message", count == 0 ? "사용하실 수 있는 아이디입니다." : userId+"은 이미 있는 아이디입니다.");
        return resultMap;
    };
}
