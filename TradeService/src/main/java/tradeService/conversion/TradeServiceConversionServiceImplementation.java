package tradeService.conversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.decoder.HeaderDecoder;
import api.dtos.BankAccountDto;
import api.dtos.CryptoWalletDto;
import api.dtos.CurrencyConversionDto;
import api.dtos.CurrencyExchangeDto;
import api.dtos.TradeServiceConversionDto;
import api.dtos.TradeServiceExchangeDto;
import api.feignProxies.BankAccountProxy;
import api.feignProxies.CryptoWalletProxy;
import api.feignProxies.CurrencyExchangeProxy;
import api.feignProxies.TradeServiceExchangeProxy;
import api.response.ApiResponse;
import api.services.TradeServiceConversionService;
import feign.FeignException;
import util.exceptions.NoDataFoundException;
import util.exceptions.ServiceUnavailableException;

@RestController
public class TradeServiceConversionServiceImplementation implements TradeServiceConversionService{
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Autowired
	private TradeServiceExchangeProxy tradeServiceExchangeProxy;
	
	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;
	
	@Autowired
	private HeaderDecoder headerDecoder;
	
	TradeServiceExchangeDto tradeServiceExchange;

	@Override
	public ResponseEntity<?> getConversion(String from, String to, BigDecimal quantity, String authorization) {
		
		String email = headerDecoder.decodeHeader(authorization);
		
		CryptoWalletDto cryptoWallet = cryptoWalletProxy.getCryptoWallet(email).getBody();
		if (cryptoWallet == null) 
			return ResponseEntity.status(404).body("Crypto wallet doesn't exist");
		
		BankAccountDto bankAccount = bankAccountProxy.getBankAccount(email).getBody();
		if (bankAccount == null) 
			return ResponseEntity.status(404).body("Bank account doesn't exist");
		
		if (from.toUpperCase().equals("BTC") || from.toUpperCase().equals("ETH") || from.toUpperCase().equals("XRP")) {
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
			
			if (to.toUpperCase().equals("EUR") || to.toUpperCase().equals("USD")) {
				tradeServiceExchange = (TradeServiceExchangeDto) tradeServiceExchangeProxy.getExchange(from, to).getBody();
				TradeServiceConversionDto tradeServiceConversion = new TradeServiceConversionDto(tradeServiceExchange, quantity,
						tradeServiceExchange.getTo(), quantity.multiply(tradeServiceExchange.getExchangeValue()));
				
				cryptoWalletProxy.exchangeCrypto(email, tradeServiceConversion.getExchange().getFrom(), tradeServiceConversion.getExchange().getTo(),
						tradeServiceConversion.getQuantity(), tradeServiceConversion.getConversionResult().getResult());
				bankAccount = bankAccountProxy.exchangeCurrency(email, tradeServiceConversion.getExchange().getFrom(), tradeServiceConversion.getExchange().getTo(),
						tradeServiceConversion.getQuantity(), tradeServiceConversion.getConversionResult().getResult());
				
				ApiResponse<BankAccountDto> responseBody = new ApiResponse<>("Upesno je izvrsena izmena " + from + ": " + quantity + " za " + to + 
						": " + tradeServiceConversion.getConversionResult().getResult(), bankAccount);
				return ResponseEntity.ok(responseBody);
				
			} else if (to.toUpperCase().equals("RSD") || to.toUpperCase().equals("CHF") || to.toUpperCase().equals("GBP")) {
				tradeServiceExchange = (TradeServiceExchangeDto) tradeServiceExchangeProxy.getExchange(from, "USD").getBody();
				TradeServiceConversionDto tradeServiceConversion = new TradeServiceConversionDto(tradeServiceExchange, quantity,
						tradeServiceExchange.getTo(), quantity.multiply(tradeServiceExchange.getExchangeValue()));
				
				cryptoWalletProxy.exchangeCrypto(email, tradeServiceConversion.getExchange().getFrom(), tradeServiceConversion.getExchange().getTo(),
						tradeServiceConversion.getQuantity(), tradeServiceConversion.getConversionResult().getResult());
				bankAccount = bankAccountProxy.exchangeCurrency(email, tradeServiceConversion.getExchange().getFrom(), tradeServiceConversion.getExchange().getTo(),
						tradeServiceConversion.getQuantity(), tradeServiceConversion.getConversionResult().getResult());
				
				CurrencyExchangeDto currencyExchange = currencyExchangeProxy.getExchange("USD", to).getBody();
				CurrencyConversionDto currencyConversion = new CurrencyConversionDto(currencyExchange,
						tradeServiceConversion.getConversionResult().getResult(), currencyExchange.getTo(), 
						tradeServiceConversion.getConversionResult().getResult().multiply(currencyExchange.getExchangeValue()));
				
				bankAccount = bankAccountProxy.exchangeCurrency(email, currencyConversion.getExchange().getFrom(), currencyConversion.getExchange().getTo(),
						currencyConversion.getQuantity(), currencyConversion.getConversionResult().getResult());
				
				ApiResponse<BankAccountDto> responseBody = new ApiResponse<>("Upesno je izvrsena izmena " + from + ": " + quantity + " za " + to + 
						": " + tradeServiceConversion.getConversionResult().getResult(), bankAccount);
				return ResponseEntity.ok(responseBody);
			}
		}
		
		if (from.toUpperCase().equals("EUR") || from.toUpperCase().equals("USD") || from.toUpperCase().equals("RSD")
				|| from.toUpperCase().equals("CHF") || from.toUpperCase().equals("GBP")) {
			
			try {
				if (from.toUpperCase().equals("EUR")) {
					if (bankAccount.getEur().compareTo(quantity) < 0)
						throw new Exception("EUR");
				} else if (from.toUpperCase().equals("USD")) {
					if (bankAccount.getUsd().compareTo(quantity) < 0)
						throw new Exception("USD");
				} else if (from.toUpperCase().equals("RSD")) {
					if (bankAccount.getRsd().compareTo(quantity) < 0) 
						throw new Exception("RSD");
				} else if (from.toUpperCase().equals("CHF")) {
					if (bankAccount.getChf().compareTo(quantity) < 0) 
						throw new Exception("CHF");
				} else if (from.toUpperCase().equals("GBP")) {
					if (bankAccount.getGbp().compareTo(quantity) < 0) 
						throw new Exception("GBP");
				}
				
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
			
			if(from.toUpperCase().equals("EUR") || from.toUpperCase().equals("USD")) {
				tradeServiceExchange = (TradeServiceExchangeDto) tradeServiceExchangeProxy.getExchange(from, to).getBody();
				TradeServiceConversionDto tradeServiceConversion = new TradeServiceConversionDto(tradeServiceExchange, quantity,
						tradeServiceExchange.getTo(), quantity.multiply(tradeServiceExchange.getExchangeValue()));
				
				cryptoWallet = cryptoWalletProxy.exchangeCrypto(email, tradeServiceConversion.getExchange().getFrom(), tradeServiceConversion.getExchange().getTo(),
						tradeServiceConversion.getQuantity(), tradeServiceConversion.getConversionResult().getResult());
				bankAccountProxy.exchangeCurrency(email, tradeServiceConversion.getExchange().getFrom(), tradeServiceConversion.getExchange().getTo(),
						tradeServiceConversion.getQuantity(), tradeServiceConversion.getConversionResult().getResult());
				
				ApiResponse<CryptoWalletDto> responseBody = new ApiResponse<>("Upesno je izvrsena izmena " + from + ": " + quantity + " za " + to + 
						": " + tradeServiceConversion.getConversionResult().getResult(), cryptoWallet);
				return ResponseEntity.ok(responseBody);
				
			} else if (from.toUpperCase().equals("RSD") || from.toUpperCase().equals("CHF") || from.toUpperCase().equals("GBP")) {
				
				CurrencyExchangeDto currencyExchange = currencyExchangeProxy.getExchange(from, "USD").getBody();
				CurrencyConversionDto currencyConversion = new CurrencyConversionDto(currencyExchange, quantity, currencyExchange.getTo(),
						quantity.multiply(currencyExchange.getExchangeValue()));
				
				bankAccountProxy.exchangeCurrency(email, currencyConversion.getExchange().getFrom(), currencyConversion.getExchange().getTo(),
						currencyConversion.getQuantity(), currencyConversion.getConversionResult().getResult());
				
				tradeServiceExchange = (TradeServiceExchangeDto) tradeServiceExchangeProxy.getExchange("USD", to).getBody();
				TradeServiceConversionDto tradeServiceConversion = new TradeServiceConversionDto(tradeServiceExchange, 
						currencyConversion.getConversionResult().getResult(), tradeServiceExchange.getTo(), 
						currencyConversion.getConversionResult().getResult().multiply(tradeServiceExchange.getExchangeValue()));
				
				cryptoWallet = cryptoWalletProxy.exchangeCrypto(email, tradeServiceConversion.getExchange().getFrom(), tradeServiceConversion.getExchange().getTo(),
						tradeServiceConversion.getQuantity(), tradeServiceConversion.getConversionResult().getResult());
				bankAccount = bankAccountProxy.exchangeCurrency(email, tradeServiceConversion.getExchange().getFrom(), tradeServiceConversion.getExchange().getTo(),
						tradeServiceConversion.getQuantity(), tradeServiceConversion.getConversionResult().getResult());
				
				ApiResponse<CryptoWalletDto> responseBody = new ApiResponse<>("Upesno je izvrsena izmena " + from + ": " + quantity + " za " + to + 
						": " + tradeServiceConversion.getConversionResult().getResult(), cryptoWallet);
				return ResponseEntity.ok(responseBody);
			}
		}
		
		return ResponseEntity.status(400).body("You have made a bad request");
	}

}
