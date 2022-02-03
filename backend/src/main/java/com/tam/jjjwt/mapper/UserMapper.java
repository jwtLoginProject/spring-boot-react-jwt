package com.tam.jjjwt.mapper;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tam.jjjwt.model.User;


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
@Mapper
public interface UserMapper {

	// 회원 가입
	int join(User user);

	// 유저 아이디 중복 체크
	int checkUserId(String userId);

	// 유저 고유값으로 유저 찾기
	Optional<User> findUserById(int id);
	
	// 유저 아이디로 유저 찾기
	Optional<User> findUserByUserId(String userId);
	
	// 유저 아이디로 유저 고유값 찾기
	int findPkByUserId(String userId);
	
	// TODO refresh token DB 저장
	int updateRefreshToken(@Param("refreshToken") String refreshToken, @Param("userId") String userId);

}