package com.example.ppmt.services.springdatajpa;

import com.example.ppmt.domain.User;
import com.example.ppmt.exceptions.UsernameAlreadyExistsException;
import com.example.ppmt.repositories.UserRepository;
import com.example.ppmt.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserJpaService implements UserService {

	private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserJpaService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public User save(User newUser) {
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			//Username has to be unique (exception)
			newUser.setUsername(newUser.getUsername());
			// Make sure that password and confirmPassword match
			// We don't persist or show the confirmPassword
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
		} catch (Exception e) {
			throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
		}
	}
}
