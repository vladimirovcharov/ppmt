package com.example.ppmt.services.validation;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MapValidationErrorService {

	public ResponseEntity<?> validateMapError(BindingResult result) {
		if (result.hasErrors()) {
			Map<String, String> errors = result.getFieldErrors()
					.stream()
					.filter(e -> e.getDefaultMessage() != null)
					.collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage, (a, b) -> b));

			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}

		return null;
	}
}
