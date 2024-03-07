package api.services;

import org.springframework.web.bind.annotation.GetMapping;

import api.dto.CurrencyExchangeDto;

public interface CurrencyExchangeService {
	@GetMapping("/currency-exchange")
	CurrencyExchangeDto getExchange();
}
