package api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.UserDto;

public interface UsersService {
	
	@GetMapping("/users")
	List<UserDto> getUsers();
	
	@GetMapping("/users/user")
	UserDto getUserByEmail(@RequestParam String email);
	
	@PostMapping("/users/newAdmin")
	ResponseEntity<?> createAdmin(@RequestHeader String authorization, @RequestBody UserDto dto);
	
	@PostMapping("/users/newUser")
	ResponseEntity<?> createUser(@RequestHeader String authorization, @RequestBody UserDto dto);
	
	@PutMapping("/users")
	ResponseEntity<?> updateUser(@RequestHeader String authorization, @RequestBody UserDto dto);
	
	@DeleteMapping("/users/{email}")
	ResponseEntity<?> deleteUser(@RequestHeader String authorization, @PathVariable String email);
	

}
