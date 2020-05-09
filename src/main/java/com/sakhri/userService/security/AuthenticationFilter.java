//package com.sakhri.userService.security;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.core.env.Environment;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sakhri.userService.beans.LoginRequest;
//import com.sakhri.userService.dto.UserDto;
//import com.sakhri.userService.service.UserService;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//
//public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//	private Environment environment;
//	
//	private UserService userService;
//	
//	public AuthenticationFilter(UserService usersService, Environment environment) {
//		this.userService = usersService;
//		this.environment = environment;
//	}
//
//	@Override
//	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
//			throws AuthenticationException {
//		try {
//
//			LoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);
//
//			return getAuthenticationManager().authenticate(
//					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
//
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Override
//	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
//			Authentication auth) throws IOException, ServletException {
//    	String userName = ((User) auth.getPrincipal()).getUsername();
//    	UserDto userDetails = userService.getUserDetailsByEmail(userName);
//    	
//        String token = Jwts.builder()
//                .setSubject(userDetails.getUserId())
//                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration.time"))))
//                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
//                .compact();
//        
//        res.addHeader("token", token);
//        res.addHeader("userId", userDetails.getUserId());
//        
//        res.setStatus(HttpServletResponse.SC_OK);
//        res.getWriter().write("Authenticated");
//        res.getWriter().flush();
//        res.getWriter().close();
//
//	}
//}
