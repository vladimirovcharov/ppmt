package com.example.ppmt.web;

import com.example.ppmt.domain.User;
import com.example.ppmt.payload.JWTLoginSuccessResponse;
import com.example.ppmt.payload.LoginRequest;
import com.example.ppmt.security.JwtTokenProvider;
import com.example.ppmt.services.UserService;
import com.example.ppmt.services.validation.MapValidationErrorService;
import com.example.ppmt.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.ppmt.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final MapValidationErrorService mapValidationErrorService;
	private final UserService userService;
	private final UserValidator userValidator;
	private JwtTokenProvider tokenProvider;
	private AuthenticationManager authenticationManager;

	@Autowired
	public UserController(MapValidationErrorService mapValidationErrorService, UserService userService, UserValidator userValidator, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
		this.mapValidationErrorService = mapValidationErrorService;
		this.userService = userService;
		this.userValidator = userValidator;
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
		ResponseEntity<?> errorMap = mapValidationErrorService.validateMapError(result);
		if (errorMap != null) return errorMap;

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(),
						loginRequest.getPassword()
				)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		userValidator.validate(user, result);

		ResponseEntity<?> errorMap = mapValidationErrorService.validateMapError(result);
		if (errorMap != null) return errorMap;

		User newUser = userService.save(user);

		return new ResponseEntity<>(newUser, HttpStatus.CREATED);
	}
}