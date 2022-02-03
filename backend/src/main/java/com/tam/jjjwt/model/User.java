package com.tam.jjjwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	
	private int id;

    private String userId;

    private String password;
}
