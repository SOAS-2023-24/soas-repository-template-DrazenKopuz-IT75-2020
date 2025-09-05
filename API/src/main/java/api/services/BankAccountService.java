package api.services;

import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.BankAccountDto;

public interface BankAccountService {
	
	@GetMapping("/bank-account")
	ResponseEntity<?> getBankAccounts(@RequestHeader String authorization);
	
	@GetMapping("/bank-account/{email}")
	BankAccountDto getBankAccount(@PathVariable String email);
	
	@PostMapping("/bank-account")
	ResponseEntity<?> createBankAccount(@RequestBody BankAccountDto dto);
	
	@PutMapping("/bank-account/{email}")
	ResponseEntity<?> updateBankAccount(@PathVariable String email, @RequestBody BankAccountDto dto);
	
	@PutMapping("/bank-account")
	ResponseEntity<?> exchangeCurrency(@RequestParam String email, @RequestParam String from, @RequestParam String to, 
			@RequestParam BigDecimal quantity, @RequestParam BigDecimal quantityConverted);
	
	@DeleteMapping("/bank-account/{email}")
	ResponseEntity<?> deleteBankAccount(@PathVariable String email);

}
