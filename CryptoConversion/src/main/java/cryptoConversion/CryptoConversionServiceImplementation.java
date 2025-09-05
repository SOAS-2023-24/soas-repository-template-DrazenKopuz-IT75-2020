package cryptoConversion;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import api.decoder.HeaderDecoder;
import api.dtos.CryptoConversionDto;
import api.dtos.CryptoExchangeDto;
import api.dtos.CryptoWalletDto;
import api.feignProxies.CryptoExchangeProxy;
import api.feignProxies.CryptoWalletProxy;
import api.response.ApiResponse;
import api.services.CryptoConversionService;
import feign.FeignException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.servlet.http.HttpServletRequest;
import util.exceptions.NoDataFoundException;
import util.exceptions.ServiceUnavailableException;

@RestController
public class CryptoConversionServiceImplementation implements CryptoConversionService {
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	
	@Autowired
	private CryptoExchangeProxy cryptoExchangeProxy;
	
	@Autowired
	private HeaderDecoder headerDecoder;
	
	CryptoExchangeDto response;
	Retry retry;
	
	public CryptoConversionServiceImplementation(RetryRegistry registry) {
		this.retry = registry.retry("default");
	}

	@Override
	public ResponseEntity<?> getConversion(String from, String to, BigDecimal quantity, String authorization) {
		
		String email = headerDecoder.decodeHeader(authorization);
		
		CryptoWalletDto cryptoWallet = cryptoWalletProxy.getCryptoWallet(email).getBody();
		if (cryptoWallet == null) {
			return ResponseEntity.status(404).body("Crypto wallet doesn't exist");
		}
		try {
			if (from.toUpperCase().equals("BTC")) {
				if (cryptoWallet.getBtc().compareTo(quantity) < 0)
					throw new Exception("BTC");
			} else if (from.toUpperCase().equals("ETH")) {
				if (cryptoWallet.getEth().compareTo(quantity) < 0)
					throw new Exception("ETH");
			} else if (from.toUpperCase().equals("XRP")) {
				if (cryptoWallet.getXrp().compareTo(quantity) < 0) 
					throw new Exception("XRP");
			}
			retry.executeSupplier(() -> response = aquiredExchange(from, to));
		}
		catch (FeignException e) {
			if (e.status() != 404) {
				throw new ServiceUnavailableException("Crypto exchange service is unavailable");
			}
			throw new NoDataFoundException(e.getMessage());
		}
		catch (Exception ex) {
			return ResponseEntity.ok("Insufficient amount of " + ex.getMessage());
		}
		
		CryptoConversionDto cryptoConversion = exchangeToConversion(response, quantity);
		
		CryptoWalletDto cryptoWalletDto = cryptoWalletProxy.exchangeCrypto(email, cryptoConversion.getExchange().getFrom(), cryptoConversion.getExchange().getTo(), 
				cryptoConversion.getQuantity(), cryptoConversion.getConversionResult().getResult());
		
		ApiResponse<CryptoWalletDto> responseBody = new ApiResponse<>("Upesno je izvrsena izmena " + from + ": " + quantity + " za " + to + 
				": " + cryptoConversion.getConversionResult().getResult(), cryptoWalletDto);
		
		return ResponseEntity.ok(responseBody);
		
	}
	
	
	private CryptoExchangeDto aquiredExchange(String from, String to) {
		return cryptoExchangeProxy.getExchange(from, to).getBody();
	}
	
	private CryptoConversionDto exchangeToConversion(CryptoExchangeDto exchange, BigDecimal quantity) {
		return new CryptoConversionDto(exchange, quantity, exchange.getTo(), quantity.multiply(exchange.getExchangeValue()));
	}

}
