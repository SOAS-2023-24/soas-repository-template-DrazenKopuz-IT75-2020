package bankAccount.implementation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.decoder.HeaderDecoder;
import api.dtos.BankAccountDto;
import api.dtos.UserDto;
import api.feignProxies.UsersServiceProxy;
import api.services.BankAccountService;
import bankAccount.model.BankAccountModel;
import bankAccount.repository.BankAccountRepository;

@RestController
public class BankAccountServiceImplementation implements BankAccountService {
	
	@Autowired
	private BankAccountRepository repo;
	
	@Autowired
	private UsersServiceProxy usersProxy;
	
	@Autowired
	private HeaderDecoder headerDecoder;

	@Override
	public ResponseEntity<?> getBankAccounts(String authorization) {
		String email = headerDecoder.decodeHeader(authorization);
		UserDto user = usersProxy.getUserByEmail(email);
		if (user.getRole().equals("ADMIN")) {
			List<BankAccountModel> models = repo.findAll();
			List<BankAccountDto> dtos = convertModelsToDtos(models);
			return ResponseEntity.ok(dtos);
		}
		
		if (user.getRole().equals("USER")) {
			BankAccountModel model = repo.findByEmail(email);
			BankAccountDto dto = convertModelToDto(model);
			return ResponseEntity.ok(dto);
		}
		
		return ResponseEntity.status(400).body("Bad request");
	}
	
	@Override
	public BankAccountDto getBankAccount(String email) {
		BankAccountModel model = repo.findByEmail(email);
		BankAccountDto dto = convertModelToDto(model);
		return dto;
	}


	@Override
	public ResponseEntity<?> createBankAccount(BankAccountDto dto) {
		if(repo.findByEmail(dto.getEmail()) == null) {
			BankAccountModel model = convertDtoToModel(dto);
			return ResponseEntity.status(201).body(repo.save(model));
		}
		
		return ResponseEntity.status(409).body("Bank account with forwarded email already exists");
	}

	@Override
	public ResponseEntity<?> updateBankAccount(String email, BankAccountDto dto) {
		BankAccountModel bankAccount = repo.findByEmail(email);
		if (bankAccount != null) {
			return ResponseEntity.status(200).body(repo.save(setCurrenciesForBankAccount(bankAccount, dto)));
		}
		return ResponseEntity.status(404).body("Bank account with forwarded email doesn't exist");
	}
	
	@Override
	public ResponseEntity<?> exchangeCurrency(String email, String from, String to, BigDecimal quantity, BigDecimal quantityConverted) {
		BankAccountModel bankAccount = repo.findByEmail(email);
		
		switch(from.toUpperCase()) {
			case "RSD":
				bankAccount.setRsd(bankAccount.getRsd().subtract(quantity));
				break;
			case "EUR":
				bankAccount.setEur(bankAccount.getEur().subtract(quantity));
				break;
			case "USD":
				bankAccount.setUsd(bankAccount.getUsd().subtract(quantity));
				break;
			case "CHF":
				bankAccount.setChf(bankAccount.getChf().subtract(quantity));
				break;
			case "GBP":
				bankAccount.setGbp(bankAccount.getGbp().subtract(quantity));
				break;
		}
		
		switch(to.toUpperCase()) {
			case "RSD":
				bankAccount.setRsd(bankAccount.getRsd().add(quantityConverted));
				break;
			case "EUR":
				bankAccount.setEur(bankAccount.getEur().add(quantityConverted));
				break;
			case "USD":
				bankAccount.setUsd(bankAccount.getUsd().add(quantityConverted));
				break;
			case "CHF":
				bankAccount.setChf(bankAccount.getChf().add(quantityConverted));
				break;
			case "GBP":
				bankAccount.setGbp(bankAccount.getGbp().add(quantityConverted));
				break;
		}
		
		return ResponseEntity.status(200).body(repo.save(bankAccount));
		
	}
	
	
	@Override
	public ResponseEntity<?> deleteBankAccount(String email) {
		if(repo.findByEmail(email) != null) {
			BankAccountModel model = repo.findByEmail(email);
			repo.deleteById(model.getId());
			return ResponseEntity.status(200).body("Bank account with forwarded email is successfully deleted!");
		}
		
		return ResponseEntity.status(404).body("Bank account with forwarded email does not exist");
	}
	
	private List<BankAccountDto> convertModelsToDtos(List<BankAccountModel> models) {
		List<BankAccountDto> dtos = new ArrayList<>();
		for(BankAccountModel model : models) {
			BankAccountDto dto = new BankAccountDto(model.getEmail(), model.getRsd(), model.getEur(), model.getUsd(), model.getChf(), model.getGbp());
			dtos.add(dto);
		}
		return dtos;
	}
	
	private BankAccountDto convertModelToDto(BankAccountModel model) {
		BankAccountDto dto = new BankAccountDto(model.getEmail(), model.getRsd(), model.getEur(), model.getUsd(), model.getChf(), model.getGbp());
		return dto;
	}
	
	private BankAccountModel convertDtoToModel(BankAccountDto dto) {
		BankAccountModel model = new BankAccountModel(dto.getEmail(), dto.getRsd(), dto.getEur(), dto.getUsd(), dto.getChf(), dto.getGbp());
		return model;
	}
	
	private BankAccountModel setCurrenciesForBankAccount(BankAccountModel model, BankAccountDto dto) {
		model.setRsd(dto.getRsd());
		model.setEur(dto.getEur());
		model.setUsd(dto.getUsd());
		model.setChf(dto.getChf());
		model.setGbp(dto.getGbp());
		
		return model;
	}

}
