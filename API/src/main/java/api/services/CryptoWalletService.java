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

import api.dtos.CryptoWalletDto;

public interface CryptoWalletService {
	
	@GetMapping("/crypto-wallet")
	ResponseEntity<?> getCryptoWallets(@RequestHeader String authorization);
	
	@GetMapping("/crypto-wallet/{email}")
	CryptoWalletDto getCryptoWallet(@PathVariable String email);
	
	@PostMapping("/crypto-wallet")
	ResponseEntity<?> createCryptoWallet(@RequestBody CryptoWalletDto dto);
	
	@PutMapping("/crypto-wallet/{email}")
	ResponseEntity<?> updateCryptoWallet(@PathVariable String email, @RequestBody CryptoWalletDto dto);
	
	@PutMapping("/crypto-wallet")
	ResponseEntity<?> exchangeCrypto(@RequestParam String email, @RequestParam String from, @RequestParam String to,
			@RequestParam BigDecimal quantity, @RequestParam BigDecimal quantityConverted);
	
	@DeleteMapping("/crypto-wallet/{email}")
	ResponseEntity<?> deleteCryptoWallet(@PathVariable String email);

}
