package com.staskost.dto.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.staskost.dto.model.User;
import com.staskost.dto.model.UserDTO;
import com.staskost.dto.repositiry.UserRepository;

@RestController
@RequestMapping("/users")
public class Controller {

	private UserRepository userRepository;

	public Controller(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/{id}")
	public UserDTO getUserDTO(@PathVariable int id) {
		Optional<User> optional = userRepository.findById(id);
		User user = optional.get();
		UserDTO userDTO = new UserDTO();
		ModelMapper mapper = new ModelMapper();
		mapper.map(user, userDTO);
		return userDTO;
	}

	@GetMapping
	public UserDTO getUserDTO(@RequestBody User user) {
		if (user != null) {
			UserDTO userDTO = new UserDTO();
			ModelMapper mapper = new ModelMapper();
			mapper.map(user, userDTO);
			return userDTO;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
		}
	}

	@GetMapping("/all")
	public List<UserDTO> findAll() {
		List<User> users = userRepository.findAll();
		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		ModelMapper mapper = new ModelMapper();
		for (User user : users) {
			UserDTO userDTO = new UserDTO();
			mapper.map(user, userDTO);
			usersDTO.add(userDTO);
		}
		return usersDTO;

	}

	@PostMapping
	public UserDTO createUser(@RequestBody User user) {
		if (user != null) {
			userRepository.save(user);
			UserDTO userDTO = new UserDTO();
			ModelMapper mapper = new ModelMapper();
			mapper.map(user, userDTO);
			return userDTO;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteUser(@PathVariable int id) {
		Optional<User> optional = userRepository.findById(id);
		User user = optional.get();
		userRepository.delete(user);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();

	}

	@DeleteMapping
	public ResponseEntity deleteDTOUser(@RequestBody UserDTO userDTO) {
		User user = userRepository.findByEmail(userDTO.getUserEmail());
		if (user != null) {
			userRepository.delete(user);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
		}
	}
}
