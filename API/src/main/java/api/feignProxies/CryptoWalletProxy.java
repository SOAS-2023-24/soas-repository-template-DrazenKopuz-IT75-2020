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

import api.dtos.CryptoWalletDto;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {
	
	@GetMapping("/crypto-wallet")
	ResponseEntity<CryptoWalletDto> getCryptoWallets(@RequestHeader(value = "Authorization") String authorization);
	
	@GetMapping("/crypto-wallet/{email}")
	ResponseEntity<CryptoWalletDto> getCryptoWallet(@PathVariable(value = "email") String email);
	
	@PostMapping("/crypto-wallet")
	ResponseEntity<?> createCryptoWallet(@RequestBody CryptoWalletDto dto);
	
	@PutMapping("/crypto-wallet")
	CryptoWalletDto exchangeCrypto(@RequestParam(value = "email") String email, @RequestParam(value = "from") String from, @RequestParam(value = "to") String to, 
			@RequestParam(value = "quantity") BigDecimal quantity, @RequestParam(value = "quantityConverted") BigDecimal quantityConverted);
	
	@DeleteMapping("/crypto-wallet/{email}")
	ResponseEntity<?> deleteCryptoWallet(@PathVariable(value = "email") String email);

}
