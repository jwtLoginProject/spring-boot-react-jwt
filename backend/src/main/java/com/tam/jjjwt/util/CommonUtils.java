package com.tam.jjjwt.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
@Component
public class CommonUtils {


    // 아이디 유효성 검사
    public static boolean isUserId(String str) {
        String userIdRegex = "^[a-zA-Z0-9]+$";
        boolean patternUserId = Pattern.matches(userIdRegex, str);
        if (patternUserId == false) {
            return false;
        }
        if (str.length() < 3 || str.length() > 12) {
            return false;
        }
        return true;
    }

    //TODO 비밀번호와 영대소문자 포함 필수 정규식 작성
    // 비밀번호 유효성 검사
    public static boolean isPassword(String str) {
        String passwordRegex = "^[a-zA-Z0-9]+$";
        boolean patternPassword = Pattern.matches(passwordRegex, str);
        if (patternPassword == false) {
            return false;
        }
        if (str.length() < 3 || str.length() > 12) {
            return false;
        }
        return true;
    }


    // 빈 값 체크
    public static boolean isNotEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        if ((obj instanceof String) && (((String) obj).trim().length() == 0)) {
            return false;
        }
        if ((obj instanceof Map) && ((Map) obj).isEmpty()) {
            return false;
        }
        if ((obj instanceof List) && ((List) obj).isEmpty()) {
            return false;
        }
        return true;
    }

}

