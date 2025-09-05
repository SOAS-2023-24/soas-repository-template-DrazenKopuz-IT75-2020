package currencyConversion;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import api.decoder.HeaderDecoder;
import api.dtos.BankAccountDto;
import api.dtos.CurrencyConversionDto;
import api.dtos.CurrencyExchangeDto;
import api.feignProxies.BankAccountProxy;
import api.feignProxies.CurrencyExchangeProxy;
import api.feignProxies.UsersServiceProxy;
import api.response.ApiResponse;
import api.services.CurrencyConversionService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import util.exceptions.NoDataFoundException;
import util.exceptions.ServiceUnavailableException;

@RestController
public class CurrencyConversionServiceImplementation implements CurrencyConversionService {
	
private RestTemplate template = new RestTemplate();
	
	@Autowired
	private CurrencyExchangeProxy proxy;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Autowired
	private HeaderDecoder headerDecoder;
	
	CurrencyExchangeDto response;
	Retry retry;
	
	public CurrencyConversionServiceImplementation(RetryRegistry registry) {
		this.retry = registry.retry("default");
	}

	@Override
	public ResponseEntity<?> getConversion(String from, String to, BigDecimal quantity) {
		HashMap<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		CurrencyExchangeDto dto = null;
		
		try {
			ResponseEntity<CurrencyExchangeDto> response =
					template.getForEntity("http://localhost:8000/currency-exchange?from={from}&to={to}", CurrencyExchangeDto.class, uriVariables);
			
			dto = response.getBody();
			
		} catch (HttpClientErrorException e) {
			throw new NoDataFoundException(e.getMessage());
		}
		
		return ResponseEntity.ok(exchangeToConversion(dto, quantity));
	}
	
	@Override
	@CircuitBreaker(name = "cb", fallbackMethod = "fallback")
	public ResponseEntity<?> getConversionFeign(String from, String to, BigDecimal quantity, String authorization) {
		
		String email = headerDecoder.decodeHeader(authorization);
		
		BankAccountDto bankAccount = bankAccountProxy.getBankAccount(email).getBody();
		if(bankAccount == null) {
			return ResponseEntity.status(404).body("Bank account doesn't exist");
		}
		try {
			if (from.toUpperCase().equals("RSD")) {
				if (bankAccount.getRsd().compareTo(quantity) < 0)
					throw new Exception("RSD");
			} else if (from.toUpperCase().equals("EUR")) {
				if (bankAccount.getEur().compareTo(quantity) < 0)
					throw new Exception("EUR");
			} else if (from.toUpperCase().equals("USD")) {
				if (bankAccount.getUsd().compareTo(quantity) < 0)
					throw new Exception("USD");
			} else if (from.toUpperCase().equals("CHF")) {
				if (bankAccount.getChf().compareTo(quantity) < 0)
					throw new Exception("CHF");
			} else if (from.toUpperCase().equals("GBP")) {
				if (bankAccount.getGbp().compareTo(quantity) < 0)
					throw new Exception("GBP");
			}
			retry.executeSupplier(() -> response = aquiredExchange(from, to));
		}
		catch (FeignException e) {
			if(e.status() != 404) {
				throw new ServiceUnavailableException("Currency exchange service is unavailable");
			}
			throw new NoDataFoundException(e.getMessage());
		}
		catch (Exception ex) {
			return ResponseEntity.ok("Insufficient amount of " + ex.getMessage());
		}
		
		CurrencyConversionDto currencyConversion = exchangeToConversion(response, quantity);
		
		BankAccountDto bankAccountDto = bankAccountProxy.exchangeCurrency(email, currencyConversion.getExchange().getFrom(), 
				currencyConversion.getExchange().getTo(), currencyConversion.getQuantity(), currencyConversion.getConversionResult().getResult());
		
		ApiResponse<BankAccountDto> responseBody = new ApiResponse<>("Uspesno je izvrsena razmena " + from + ": " + quantity + " za " + to + ": " +
				currencyConversion.getConversionResult().getResult(), bankAccountDto);
		
		return ResponseEntity.ok(responseBody);
	}
	
	private CurrencyExchangeDto aquiredExchange(String from, String to) {
		return proxy.getExchange(from, to).getBody();
	}
	
	private ResponseEntity<?> fallback(CallNotPermittedException ex) {
		return ResponseEntity.status(503).body("Currency conversion service is unavailable");
	}
	
	private CurrencyConversionDto exchangeToConversion(CurrencyExchangeDto exchange, BigDecimal quantity) {
		return new CurrencyConversionDto(exchange, quantity, exchange.getTo(), quantity.multiply(exchange.getExchangeValue()));
	}

}
