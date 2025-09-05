package usersService.implementation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.decoder.HeaderDecoder;
import api.dtos.BankAccountDto;
import api.dtos.CryptoWalletDto;
import api.dtos.UserDto;
import api.feignProxies.BankAccountProxy;
import api.feignProxies.CryptoWalletProxy;
import api.services.UsersService;
import usersService.model.UserModel;
import usersService.repository.UsersServiceRepository;

@RestController
public class UsersServiceImplementation implements UsersService {
	
	@Autowired
	private UsersServiceRepository repo;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	
	@Autowired
	private HeaderDecoder headerDecoder;

	@Override
	public List<UserDto> getUsers() {
		List<UserModel> listOfModels = repo.findAll();
		ArrayList<UserDto> listOfDtos = new ArrayList<UserDto>();
		
		for(UserModel model: listOfModels) {
			listOfDtos.add(convertModelToDto(model));
		}
		
		return listOfDtos;
	}
	
	@Override
	public UserDto getUserByEmail(String email) {
		UserModel model = repo.findByEmail(email);
		UserDto dto = convertModelToDto(model);
		return dto;
	}

	@Override
	public ResponseEntity<?> createAdmin(String authorization, UserDto dto) {
		String email = headerDecoder.decodeHeader(authorization);
		UserDto user = getUserByEmail(email);
		
		if (user.getRole().equals("OWNER")) {
			
			if(repo.findByEmail(dto.getEmail()) == null) {
				dto.setRole("ADMIN");
				UserModel model = convertDtoToModel(dto);
				return ResponseEntity.status(201).body(repo.save(model));
			}
			
			return ResponseEntity.status(409).body("Admin with forwarded email already exists");
		}
		
		return ResponseEntity.status(403).body("Only owners can add admins");
	}

	@Override
	public ResponseEntity<?> createUser(String authorization, UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) == null) {
			dto.setRole("USER");
			UserModel model = convertDtoToModel(dto);
			bankAccountProxy.createBankAccount(new BankAccountDto(dto.getEmail(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
			cryptoWalletProxy.createCryptoWallet(new CryptoWalletDto(dto.getEmail(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
			return ResponseEntity.status(201).body(repo.save(model));
		}
		
		return ResponseEntity.status(409).body("User with forwarded email already exists");
	}

	@Override
	public ResponseEntity<?> updateUser(String authorization, UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) != null) {
			UserModel userToUpdate = repo.findByEmail(dto.getEmail());
			
			if (userToUpdate.getRole().equals("ADMIN")) {
				String email = headerDecoder.decodeHeader(authorization);
				UserModel requestUser = repo.findByEmail(email);
				
				if (requestUser.getRole().equals("ADMIN"))
					return ResponseEntity.status(403).body("Only owners can update admins");
				
			} else if (userToUpdate.getRole().equals("OWNER"))
				return ResponseEntity.status(403).body("Owners can't be updated"); 
			
			repo.updateUser(dto.getEmail(), dto.getPassword(), dto.getRole());
			return ResponseEntity.status(200).body(dto);
		}
		
		return ResponseEntity.status(404).body("User with forwarded email does not exists");
			
	}
	
	@Override
	public ResponseEntity<?> deleteUser(String authorization, String email) {
		if(repo.findByEmail(email) != null) {
			UserModel userModel = repo.findByEmail(email);
			if(userModel.getRole().equals("USER")) {
				bankAccountProxy.deleteBankAccount(email);
				cryptoWalletProxy.deleteCryptoWallet(email);
			}
			repo.deleteById(userModel.getId());
			return ResponseEntity.status(200).body("User with email: " + email + " was successfully deleted");
		} else
			return ResponseEntity.status(404).body("User with forwarded email: " + email + " does not exists");
	}
	
	public UserModel convertDtoToModel(UserDto dto) {
		return new UserModel(dto.getEmail(), dto.getPassword(), dto.getRole());
	}
	
	public UserDto convertModelToDto(UserModel user) {
		return new UserDto(user.getEmail(), user.getPassword(), user.getRole());
	}

	
}
