package com.tam.jjjwt.config;

import com.tam.jjjwt.config.PrincipalDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 이동은
 * @version 1.0
 * @Description
 * @Modification Information
 * Created 2021/02/03
 * @
 * @ 수정일       	수정자        수정내용
 * @ ———   			————    	—————————————
 * @ 2021/02/03     이동은        최초 작성
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private PrincipalDetailService principalDetailService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;


//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		try {
//			String jwt = null; //parseJwt(request);
//			if (jwt != null && jwtTokenUtil.checkClaim(jwt)) {
//				String username = jwtTokenUtil.getUserIdFromToken(jwt);
//
//				UserDetails userDetails = principalDetailService.loadUserByUsername(username);
//				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//						userDetails, null, userDetails.getAuthorities());
//				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//				SecurityContextHolder.getContext().setAuthentication(authentication);
//			}
//		} catch (Exception e) {
//			System.out.println("Cannot set user authentication: {}");
//		}
//
//		filterChain.doFilter(request, response);
//	}

//	private String parseJwt(HttpServletRequest request) {
//		String headerAuth = request.getHeader(AuthConstats.AUTH_HEADER);
//
//		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(AuthConstats.TOKEN_TYPE + " ")) {
//			return headerAuth.substring(7, headerAuth.length());
//		}
//
//		return null;
//	}




//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//		String token = jwtTokenUtil.resolveToken((HttpServletRequest) request);
//		if (token != null && jwtTokenUtil.validateToken(token)) {   // token 검증
//			Authentication auth = jwtTokenUtil.getAuthentication(token);    // 인증 객체 생성
//			SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContextHolder에 인증 객체 저장
//		}
//		filterChain.doFilter(request, response);
//	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String userId = null;
		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		}
//		else {
			System.out.println("JWT Token does not begin with Bearer String");
//
//		}

		// Once we get the token validate it.
		if (SecurityContextHolder.getContext().getAuthentication() == null) {			//userId != null &&

			System.out.println("JWT Token must be generated & User must be authenticated");

			UserDetails principalDetail = principalDetailService.loadUserByUsername(userId); //this. 없애고 테스트

			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, principalDetail)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						principalDetail, null, principalDetail.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

}
