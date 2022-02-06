package com.tam.jjjwt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tam.jjjwt.mapper.UserMapper;
import com.tam.jjjwt.model.User;
import com.tam.jjjwt.response.exception.ApiException;

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
@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
    private BCryptPasswordEncoder encoder;
	
	// 회원가입
    @Transactional
    public Map<String, String> join(User requestUser) throws ApiException {

        String rawPassword = requestUser.getPassword();

        String encPassword = encoder.encode(rawPassword);
        requestUser.setPassword(encPassword);


        userMapper.join(requestUser);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("messageType", "Success");
        resultMap.put("message", "회원가입 완료");


        return resultMap;
    };

    
    // TODO refresh token 저장
    @Transactional
    public int updateRefreshToken(String refreshToken, String userId) {
    	return userMapper.updateRefreshToken(refreshToken, userId);
    }



}
