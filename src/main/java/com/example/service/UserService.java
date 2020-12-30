package com.example.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bean.TokenDetails;
import com.example.bean.User;
import com.example.reposistory.UserRepo;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepo repo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User loadUserByUsername(String username) {
		User user = this.repo.findByUsername(username);
		if (user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

	public void saveUser() {
		User user = new User();
		user.setEmail("dfsdfsdf");
		user.setPassword(passwordEncoder.encode("hash"));
		user.setRoles("ADMIN");
		repo.saveUser(user);
	}

	public void persistToken(TokenDetails details) {
		this.repo.saveToken(details);
	}

}
