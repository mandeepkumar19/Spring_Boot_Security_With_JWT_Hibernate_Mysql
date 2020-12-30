package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bean.TokenDetails;
import com.example.bean.User;
import com.example.jwt.jwtUtil;
import com.example.service.UserService;

@RestController()
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private jwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/authenticate")
	public ResponseEntity<?> hello(@RequestBody User user) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final String jwt = this.jwtUtil.generateToken(User.getCurrentUser());
			TokenDetails tokenDetails = new TokenDetails();
			tokenDetails.setToken(jwt);
			tokenDetails.setUserId(User.getCurrentUser().getId());
			this.service.persistToken(tokenDetails);
			return ResponseEntity.ok(jwt);
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			return ResponseEntity.ok(e);
		}
	}

	@GetMapping("/register")
	public ResponseEntity<?> saveUser() {
		service.saveUser();
		return ResponseEntity.ok("Hello world");
	}

	@GetMapping("/user")
	public ResponseEntity<?> sdfgd() {
		User user = User.getCurrentUser();
		user.setPassword(null);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/admin")
	public ResponseEntity<?> admin() {
		return ResponseEntity.ok("Hello HIIIIIIIIIII");
	}

}
