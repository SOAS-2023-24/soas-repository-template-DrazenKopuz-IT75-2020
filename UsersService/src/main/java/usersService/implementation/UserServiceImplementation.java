package usersService.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dto.UserDto;
import api.services.UsersService;
import usersService.model.UserModel;
import usersService.repository.UsersServiceRepository;

@RestController
public class UserServiceImplementation implements UsersService {

	@Autowired
	private UsersServiceRepository repo;

	@Override
	public List<UserDto> getUsers() {
		List<UserModel> listOfModels = repo.findAll();
		ArrayList<UserDto> listOfDtos = new ArrayList<UserDto>();
		for(UserModel um: listOfModels) {
			listOfDtos.add(convertModelToDto(um));
		}
		return listOfDtos;
	}

	@Override
	public ResponseEntity<?> createAdmin(UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) == null) {
			dto.setRole("ADMIN");
			UserModel model = convertDtoToModel(dto);
			return ResponseEntity.status(201).body(repo.save(model));
		}
		return ResponseEntity.status(409).body("Admin with forwarded email already exists");
	}

	@Override
	public ResponseEntity<?> createUser(UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) == null) {
			dto.setRole("USER");
			UserModel model = convertDtoToModel(dto);
			return ResponseEntity.status(201).body(repo.save(model));
		}
		return ResponseEntity.status(409).body("User with forwarded email already exists");
	}

	@Override
    public ResponseEntity<?> updateUser(UserDto dto) {
        UserModel user = repo.findByEmail(dto.getEmail());
        if (user != null && user.getRole().equals("USER")) {
            repo.updateUser(dto.getEmail(), dto.getPassword(), dto.getRole());
            return ResponseEntity.status(200).body(dto);
        }
        return ResponseEntity.status(404).body("User not found or is not a USER");
    }
	
	@Override
    public ResponseEntity<?> updateAdmin(UserDto dto) {
        UserModel admin = repo.findByEmail(dto.getEmail());
        if (admin != null && admin.getRole().equals("ADMIN")) {
            repo.updateUser(dto.getEmail(), dto.getPassword(), dto.getRole());
            return ResponseEntity.status(200).body(dto);
        }
        return ResponseEntity.status(404).body("Admin not found or is not an ADMIN");
    }
	
	  @Override
	    public ResponseEntity<?> deleteUserByEmail(String email) {
	        UserModel user = repo.findByEmail(email);        
	        if (user != null && user.getRole().equals("USER")) {
	        	repo.deleteByEmail(email);
	        	return ResponseEntity.status(200).body("User deleted successfully");
	        }
	        return ResponseEntity.status(404).body("User not found or is not a USER");
	    }
	  
	  @Override
	    public ResponseEntity<?> deleteAdminByEmail(String email) {
	        UserModel user = repo.findByEmail(email);        
	        if (user != null && user.getRole().equals("ADMIN")) {
	        	repo.deleteByEmail(email);
	        	return ResponseEntity.status(200).body("Admin deleted successfully");
	        }
	        return ResponseEntity.status(404).body("Admin not found or is not a Admin");
	    }
	
	public UserModel convertDtoToModel(UserDto dto) {
		return new UserModel(dto.getEmail(), dto.getPassword(), dto.getRole());
	}
	
	public UserDto convertModelToDto(UserModel model) {
		return new UserDto(model.getEmail(), model.getPassword(), model.getRole());
	}
	
	
	
	
	
	
}
