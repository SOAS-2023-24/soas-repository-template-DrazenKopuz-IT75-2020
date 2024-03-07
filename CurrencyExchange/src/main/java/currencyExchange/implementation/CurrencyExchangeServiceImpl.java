package currencyExchange.implementation;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.RestController;

import api.dto.CurrencyExchangeDto;
import api.services.CurrencyExchangeService;

@RestController
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService{

	@Override
	public CurrencyExchangeDto getExchange() {
		return new CurrencyExchangeDto("EUR", "RSD", BigDecimal.valueOf(117.5));
	}
	
}
