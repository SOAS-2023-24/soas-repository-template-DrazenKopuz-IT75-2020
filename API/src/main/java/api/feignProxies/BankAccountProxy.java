package api.feignProxies;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
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

@FeignClient(name = "bank-account")
public interface BankAccountProxy {
	
	@GetMapping("/bank-account")
	ResponseEntity<BankAccountDto> getBankAccounts(@RequestHeader(value = "Authorization") String authorization);
	
	@GetMapping("/bank-account/{email}")
	ResponseEntity<BankAccountDto> getBankAccount(@PathVariable(value = "email") String email);
	
	@PostMapping("/bank-account")
	ResponseEntity<?> createBankAccount(@RequestBody BankAccountDto dto);
	
	@PutMapping("/bank-account")
	BankAccountDto exchangeCurrency(@RequestParam(value = "email") String email, @RequestParam(value = "from") String from, @RequestParam(value = "to") String to, 
			@RequestParam(value = "quantity") BigDecimal quantity, @RequestParam(value = "quantityConverted") BigDecimal quantityConverted);
	
	@DeleteMapping("/bank-account/{email}")
	ResponseEntity<?> deleteBankAccount(@PathVariable(value = "email") String email);

	

}
